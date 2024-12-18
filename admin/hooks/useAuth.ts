import { useEffect, useState } from "react";
import axios from "axios";
import { useRouter, usePathname } from "next/navigation";


interface UserData {
  data: {
    roles: Array<{
      name: string;
    }>;
  };
}

export function useAuth() {
  const router = useRouter();
  const pathname = usePathname(); // Sử dụng usePathname thay cho router.pathname
  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const token = localStorage.getItem("token");

        // Kiểm tra token và xử lý chuyển hướng login
        if (!token) {
          if (pathname !== "/login") {
            router.push("/login");
          }
          setIsLoading(false);
          return;
        }

        // Kiểm tra vai trò người dùng
        const response = await axios.get<UserData>(
          `${process.env.NEXT_PUBLIC_API_URL_USER}/users/me`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const userRoles = response.data?.data?.roles || [];
        const adminRole = userRoles.some((role) => role.name === "ADMIN");

        setIsAuthenticated(true);
        setIsAdmin(adminRole);

        // Kiểm tra quyền truy cập các route được bảo vệ
        const protectedRoutes = ["/", "/home", "/dashboard"];
        if (!adminRole && protectedRoutes.includes(pathname)) {
          router.push("/unauthorized");
        }
      } catch (error) {
        console.error("Auth check failed:", error);
        localStorage.removeItem("token"); // Xóa token không hợp lệ
        router.push("/login");
      } finally {
        setIsLoading(false);
      }
    };

    checkAuth();
  }, [pathname, router]);

  // Thêm hàm tiện ích để logout
  const logout = () => {
    localStorage.removeItem("token");
    setIsAuthenticated(false);
    setIsAdmin(false);
    router.push("/login");
  };

  return {
    isLoading,
    isAuthenticated,
    isAdmin,
    logout, // Export thêm hàm logout để sử dụng khi cần
  };
}
