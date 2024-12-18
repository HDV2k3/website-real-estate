// "use client";
// import React from "react";
// import {
//   Form,
//   Input,
//   InputNumber,
//   Row,
//   Col,
//   DatePicker,
//   ConfigProvider,
// } from "antd";
// import dayjs from "dayjs";
// import buddhistEra from "dayjs/plugin/buddhistEra";
// import type { DatePickerProps } from "antd";
// import en from "antd/es/date-picker/locale/en_US";
// import enUS from "antd/es/locale/en_US";

// dayjs.extend(buddhistEra);

// interface roomInfo {
//   name: string;
//   description: string;
//   address: string;
//   type: string;
//   style: string;
//   floor: string;
//   width: number;
//   height: number;
//   totalArea: number;
//   capacity: number;
//   numberOfBedrooms: number;
//   numberOfBathrooms: number;
//   availableFromDate?: string;
// }

// interface roomInfoFormProps {
//   roomInfo?: roomInfo;
// }

// // Component-level locale for DatePicker
// const buddhistLocale: typeof en = {
//   ...en,
//   lang: {
//     ...en.lang,
//     dateFormat: "BBBB-MM-DD",
//     dateTimeFormat: "BBBB-MM-DD HH:mm:ss",
//     yearFormat: "BBBB",
//     cellYearFormat: "BBBB",
//   },
// };

// // ConfigProvider-level locale
// const globalBuddhistLocale: typeof enUS = {
//   ...enUS,
//   DatePicker: {
//     ...enUS.DatePicker!,
//     lang: buddhistLocale.lang,
//   },
// };

// const RoomInfoForm: React.FC<roomInfoFormProps> = ({ roomInfo }) => {
//   // Convert availableFromDate to dayjs Buddhist Era format
//   const availableFromDate = roomInfo?.availableFromDate
//     ? dayjs(roomInfo.availableFromDate).format("BBBB-MM-DD") // Format date to Buddhist Era
//     : null;

//   const handleDateChange: DatePickerProps["onChange"] = (_, dateStr) => {
//     console.log("onChange:", dateStr);
//   };

//   return (
//     <>
//       <Form.Item
//         label="Tên Phòng"
//         name={["roomInfo", "name"]}
//         rules={[{ required: true, message: "Room name is required" }]}
//       >
//         <Input
//           min={0}
//           style={{ width: "100%" }}
//           placeholder={"vui lặng nhập tên phòng"}
//         />
//       </Form.Item>

//       <Form.Item label="Room Description" name={["roomInfo", "description"]}>
//         <Input.TextArea placeholder={"Enter room description"} />
//       </Form.Item>

//       <Form.Item label="Address" name={["roomInfo", "address"]}>
//         <Input placeholder={"Enter room address"} />
//       </Form.Item>

//       <Form.Item label="Room Type" name={["roomInfo", "type"]}>
//         <Input placeholder={"Enter room type"} />
//       </Form.Item>

//       <Form.Item label="Room Style" name={["roomInfo", "style"]}>
//         <Input placeholder={"Enter room style"} />
//       </Form.Item>

//       <Form.Item label="Floor" name={["roomInfo", "floor"]}>
//         <Input placeholder={"Enter floor"} />
//       </Form.Item>

//       <Row gutter={16}>
//         <Col span={12}>
//           <Form.Item label="Width" name={["roomInfo", "width"]}>
//             <InputNumber
//               min={1}
//               style={{ width: "100%" }}
//               placeholder={"Enter width"}
//             />
//           </Form.Item>
//         </Col>
//         <Col span={12}>
//           <Form.Item label="Height" name={["roomInfo", "height"]}>
//             <InputNumber
//               min={1}
//               style={{ width: "100%" }}
//               placeholder={"Enter height"}
//             />
//           </Form.Item>
//         </Col>
//       </Row>

//       <Row gutter={16}>
//         <Col span={12}>
//           <Form.Item label="Total Area (m²)" name={["roomInfo", "totalArea"]}>
//             <InputNumber
//               min={1}
//               style={{ width: "100%" }}
//               placeholder={"Enter total area"}
//             />
//           </Form.Item>
//         </Col>
//         <Col span={12}>
//           <Form.Item label="Capacity" name={["roomInfo", "capacity"]}>
//             <InputNumber
//               min={1}
//               style={{ width: "100%" }}
//               placeholder={"Enter capacity"}
//             />
//           </Form.Item>
//         </Col>
//       </Row>

//       <Row gutter={16}>
//         <Col span={12}>
//           <Form.Item
//             label="Number of Bedrooms"
//             name={["roomInfo", "numberOfBedrooms"]}
//           >
//             <InputNumber
//               min={1}
//               style={{ width: "100%" }}
//               placeholder={"Enter number of bedrooms"}
//             />
//           </Form.Item>
//         </Col>
//         <Col span={12}>
//           <Form.Item
//             label="Number of Bathrooms"
//             name={["roomInfo", "numberOfBathrooms"]}
//           >
//             <InputNumber
//               min={1}
//               style={{ width: "100%" }}
//               placeholder={"Enter number of bathrooms"}
//             />
//           </Form.Item>
//         </Col>
//       </Row>
//       <ConfigProvider locale={globalBuddhistLocale}>
//         <Form.Item
//           label="Available From Date"
//           name={["roomInfo", "availableFromDate"]}
//         >
//           <DatePicker
//             placeholder={"Select available from date"}
//             style={{ width: "100%" }}
//             locale={buddhistLocale}
//             defaultValue={
//               availableFromDate ? dayjs(availableFromDate, "BBBB-MM-DD") : null
//             }
//             onChange={handleDateChange}
//           />
//         </Form.Item>
//       </ConfigProvider>
//     </>
//   );
// };

// export default RoomInfoForm;
"use client";
import React from "react";
import {
  Form,
  Input,
  InputNumber,
  Row,
  Col,
  DatePicker,
  ConfigProvider,
} from "antd";
import dayjs from "dayjs";
import buddhistEra from "dayjs/plugin/buddhistEra";
import type { DatePickerProps } from "antd";

dayjs.extend(buddhistEra);

interface roomInfo {
  name: string;
  description: string;
  address: string;
  type: string;
  style: string;
  floor: string;
  width: number;
  height: number;
  totalArea: number;
  capacity: number;
  numberOfBedrooms: number;
  numberOfBathrooms: number;
  availableFromDate?: string;
}

interface roomInfoFormProps {
  roomInfo?: roomInfo;
}

// Component-level locale for DatePicker

// ConfigProvider-level locale

const RoomInfoForm: React.FC<roomInfoFormProps> = ({}) => {
  // Convert availableFromDate to dayjs Buddhist Era format

  const handleDateChange: DatePickerProps["onChange"] = (_, dateStr) => {
    console.log("onChange:", dateStr);
  };

  return (
    <>
      <Form.Item
        label="Tên Phòng"
        name={["roomInfo", "name"]}
        rules={[{ required: true, message: "Tên phòng là bắt buộc" }]}
      >
        <Input
          min={0}
          style={{ width: "100%" }}
          placeholder={"Vui lòng nhập tên phòng"}
        />
      </Form.Item>

      <Form.Item label="Mô Tả Phòng" name={["roomInfo", "description"]}>
        <Input.TextArea placeholder={"Nhập mô tả phòng"} />
      </Form.Item>

      <Form.Item label="Địa Chỉ" name={["roomInfo", "address"]}>
        <Input placeholder={"Nhập địa chỉ phòng"} />
      </Form.Item>

      <Form.Item label="Loại Phòng" name={["roomInfo", "type"]}>
        <Input placeholder={"Nhập loại phòng"} />
      </Form.Item>

      <Form.Item label="Phong Cách Phòng" name={["roomInfo", "style"]}>
        <Input placeholder={"Nhập phong cách phòng"} />
      </Form.Item>

      <Form.Item label="Tầng" name={["roomInfo", "floor"]}>
        <Input placeholder={"Nhập tầng phòng"} />
      </Form.Item>

      <Row gutter={16}>
        <Col span={12}>
          <Form.Item label="Chiều Rộng" name={["roomInfo", "width"]}>
            <InputNumber
              min={1}
              style={{ width: "100%" }}
              placeholder={"Nhập chiều rộng"}
            />
          </Form.Item>
        </Col>
        <Col span={12}>
          <Form.Item label="Chiều Cao" name={["roomInfo", "height"]}>
            <InputNumber
              min={1}
              style={{ width: "100%" }}
              placeholder={"Nhập chiều cao"}
            />
          </Form.Item>
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={12}>
          <Form.Item
            label="Diện Tích Tổng (m²)"
            name={["roomInfo", "totalArea"]}
          >
            <InputNumber
              min={1}
              style={{ width: "100%" }}
              placeholder={"Nhập diện tích tổng"}
            />
          </Form.Item>
        </Col>
        <Col span={12}>
          <Form.Item label="Sức Chứa" name={["roomInfo", "capacity"]}>
            <InputNumber
              min={1}
              style={{ width: "100%" }}
              placeholder={"Nhập sức chứa"}
            />
          </Form.Item>
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={12}>
          <Form.Item
            label="Số Phòng Ngủ"
            name={["roomInfo", "numberOfBedrooms"]}
          >
            <InputNumber
              min={1}
              style={{ width: "100%" }}
              placeholder={"Nhập số phòng ngủ"}
            />
          </Form.Item>
        </Col>
        <Col span={12}>
          <Form.Item
            label="Số Phòng Tắm"
            name={["roomInfo", "numberOfBathrooms"]}
          >
            <InputNumber
              min={1}
              style={{ width: "100%" }}
              placeholder={"Nhập số phòng tắm"}
            />
          </Form.Item>
        </Col>
      </Row>
      <ConfigProvider>
        <Form.Item label="Ngày có sẵn" name={["roomInfo", "availableFromDate"]}>
          <DatePicker
            placeholder={"Chọn ngày có sẵn"}
            style={{ width: "100%" }}
            onChange={handleDateChange}
            format="YYYY-MM-DD"
            defaultValue={dayjs()}
            className="rounded-md"
          />
        </Form.Item>
      </ConfigProvider>
    </>
  );
};

export default RoomInfoForm;
