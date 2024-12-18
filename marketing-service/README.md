<!DOCTYPE html>
<html lang="vi">

<body>

<div class="container mt-5">
    <h1 class="text-center mb-4">Hướng Dẫn Triển Khai Server Trên EC2</h1>
    <div class="row">
        <div class="col-lg-8 offset-lg-2">
            <h2 class="mt-4">Bước 1: Chuẩn Bị</h2>
            <ul>
                <li>Chuẩn bị server local, Tài Khoản Docker Hub, Hệ điều hành Ubuntu (Termius).</li>
                <li>Tài khoản AWS sử dụng EC2, Tài khoản MongoDB Atlas.</li>
            </ul>
            <h2 class="mt-4">Bước 2: Thay Đổi Database</h2>
            <ul>
                <li>Đăng nhập vào tài khoản MongoDB Atlas của bạn.</li>
                <li>Chọn cluster mà bạn muốn kết nối.</li>
                <li>Trong thanh điều hướng bên trái, chọn "Network Access".</li>
                <li>Click vào "Add IP Address".</li>
                <li>Thêm địa chỉ IP công cộng của EC2 instance vào danh sách.</li>
                <li>Thay đổi database local sang database online đường link có dạng:
                    <code>mongodb+srv://&lt;username&gt;:&lt;password&gt;@cluster0.ueypw.mongodb.net/&lt;databasename&gt;?retryWrites=true&w=majority&appName=Cluster0</code>
                </li>
                <li>Nếu có sử dụng dịch vụ thứ 3 có private key thì upload lên S3 của AWS. Đặt biến môi trường cho S3 bucket và đường dẫn config trong Dockerfile.</li>
            </ul>
            <h2 class="mt-4">Bước 3: Tạo Dockerfile</h2>
            <pre><code>
# Stage 1: Build the application with Maven
FROM maven:3.9.8-amazoncorretto-17 AS build
# Set the working directory
WORKDIR /app
# Copy the Maven POM file and source code
COPY pom.xml ./ 
COPY src ./src 
# Build the application, skipping tests
RUN mvn clean package -DskipTests
# Stage 2: Create a runtime image
FROM amazoncorretto:17.0.8
# Set the working directory
WORKDIR /app
# Install AWS CLI to download files from S3
RUN yum install -y aws-cli
# Create the resources directory in the container
RUN mkdir -p /app/config
# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar
# Set environment variables for S3 bucket and config path
ENV S3_BUCKET_NAME=mybucketmaketing
ENV FIREBASE_CONFIG_S3_PATH=FIREBASE_CONFIG_URI/datpt-ce669-firebase-adminsdk-uh9g3-cb456274d9.json
ENV LOCAL_FIREBASE_CONFIG_PATH=/app/config/firebase-config.json
# Download Firebase config from S3, set permissions, and start the application
ENTRYPOINT ["sh", "-c", "\
echo 'Downloading Firebase config from S3...' && \
if aws s3 cp s3://$S3_BUCKET_NAME/$FIREBASE_CONFIG_S3_PATH $LOCAL_FIREBASE_CONFIG_PATH; then \
echo 'Download successful' && \
chmod 644 $LOCAL_FIREBASE_CONFIG_PATH && \
echo 'File permissions set' && \
echo 'File contents:' && \
cat $LOCAL_FIREBASE_CONFIG_PATH && \
java -DFIREBASE_CONFIG_URI=file:$LOCAL_FIREBASE_CONFIG_PATH -jar app.jar; \
else \
echo 'Download failed' && \
exit 1; \
fi"]
            <span class="copy-icon" onclick="copyToClipboard('...')"><i class="fas fa-copy"></i></span>
            </code></pre>
            <h2 class="mt-4">Bước 4: Build Docker Image</h2>
            <ul>
                <li>Mở terminal trong project:</li>
                <ul>
                    <li><code>docker build -t &lt;username&gt;/&lt;image name&gt;:&lt;tag&gt; .</code></li>
                    <li><code>docker images</code> để kiểm tra image.</li>
                    <li><code>docker image push &lt;username&gt;/&lt;image name&gt;:&lt;tag&gt;</code></li>
                    <li><code>docker image pull &lt;username&gt;/&lt;image name&gt;:&lt;tag&gt;</code></li>
                </ul>
            </ul>
            <h2 class="mt-4">Bước 5: Truy Cập AWS Console</h2>
            <ol>
                <li>Chọn EC2 service.</li>
                <li>Chọn Security Groups và tạo Security Group mới.</li>
                <img src="https://firebasestorage.googleapis.com/v0/b/datpt-ce669.appspot.com/o/logo%2FA%CC%89nh%20chu%CC%A3p%20ma%CC%80n%20hi%CC%80nh%202024-09-20%20152056.png?alt=media&token=34a2f8ad-9c29-41ab-add2-1facc46a28c1" alt="Hướng dẫn EC2" class="img-fluid my-3"/>
                <li>Chọn Launch Instance.</li>
                <li>Điền thông tin:
                    <ul>
                        <li>Name and tags: ...</li>
                        <li>Application and OS Images (Amazon Machine Image): Ubuntu</li>
                        <li>Instance type: t2.micro (Free tier eligible)</li>
                        <li>Key pair (login) -> create new key -> Key pair name: awsKey -> Key pair type: RSA -> Private key file format: .pem</li>
                    </ul>
                </li>
                <li>Chọn nhóm bảo mật vừa tạo.</li>
                <li>Cấu hình storage lên 15GiB.</li>
                <li>Click Launch Instance và xem tất cả instances để kiểm tra trạng thái.</li>
            </ol>
            <h2 class="mt-4">Bước 6: Thiết lập IAM Role</h2>
            <ol>
                <li>Chọn Action -> Security -> Modify IAM Role -> Create new IAM role.</li>
                <li>Create role -> AWS service.</li>
                <li>Use case -> EC2 -> AmazonEC2FullAccess, AmazonS3FullAccess.</li>
                <li>Tạo tên role và hoàn thành.</li>
                <li>Lặp lại để tạo role cho S3 với quyền AmazonS3FullAccess.</li>
                <li>Sau đó quay lại instance -> Modify IAM Role -> ROLE_NAME(EC2).</li>
            </ol>
            <h2 class="mt-4">Bước 7: Tạo Bucket và Cấu Hình Quyền</h2>
            <ol>
                <li>Tạo bucket với tên mong muốn.</li>
                <li>Tạo folder <code>FIREBASE_CONFIG_URI</code>.</li>
                <li>Upload file config Firebase.</li>
                <li>Vào lại my bucket, chọn permission -> Edit bucket policy và paste cấu hình:</li>
            </ol>
            <pre><code>
{
"Version": "2012-10-17",
"Statement": [
{
"Effect": "Allow",
"Principal": {
"AWS": "arn:aws:iam::&lt;Account ID&gt;:role/&lt;ROLE_NAME_S3&gt;"
},
"Action": "s3:GetObject",
"Resource": "arn:aws:s3:::&lt;name bucket&gt;/FIREBASE_CONFIG_URI/*"
}
]
}
            <span class="copy-icon" onclick="copyToClipboard('...')"><i class="fas fa-copy"></i></span>
            </code></pre>
            <h2 class="mt-4">Bước 8: Kết Nối với EC2</h2>
            <ol>
                <li>Mở ứng dụng Termius.</li>
                <li>Thêm SSH key <code>key.pem</code> vừa tạo ở EC2.</li>
                <li>Tạo host với public IP của EC2.</li>
                <li>Username mặc định: ubuntu.</li>
                <li>Thực hiện các bước để vào giao diện của Ubuntu.</li>
                <li>Chạy các lệnh sau:</li>
            </ol>
            <pre><code>
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
echo \
"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo docker run hello-world # (test)
sudo docker pull &lt;username&gt;/&lt;image name&gt;:&lt;tag&gt;
sudo docker run -p 8083:8083 &lt;username&gt;/&lt;image name&gt;:&lt;tag&gt; # (để kiểm tra)
            <span class="copy-icon" onclick="copyToClipboard('...')"><i class="fas fa-copy"></i></span>
            </code></pre>
            <p class="mt-3">Cuối cùng, kiểm tra lại API với public IP của instance:</p>
            <a href="http://3.106.241.206:8083/marketing/post/all">http://3.106.241.206:8083/marketing/post/all</a>
            <p class="mt-3">Quy trình tiếp theo CI/CD tự động hóa kiểm thử và build ứng dụng</p>
        </div>
    </div>
</div>
</body>
</html>
