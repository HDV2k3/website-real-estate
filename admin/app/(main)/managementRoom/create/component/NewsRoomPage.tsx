import React from "react";
import { Form, Input, Button, Select, ConfigProvider, DatePicker } from "antd";
import RoomInfoForm from "../../component/edit_create/component/RoomInfoEdit";
import PricingDetailsForm from "../../component/edit_create/component/PricingDetails";
import RoomUtilityForm from "../../component/edit_create/component/RoomUtilities";
import dayjs from "dayjs";
import type { DatePickerProps } from "antd";
import { notificationService } from "@/service/notificationService";

interface RoomFormProps {
  roomData?: RoomFinal;
  onSubmit: (data: RoomFinal) => void;
}

const NewRoomForm: React.FC<RoomFormProps> = ({ roomData, onSubmit }) => {
  const [form] = Form.useForm();

  const furnitures = [];
  const amenities = [];
  const statusOptions = [
    { label: "ĐANG HOẠT ĐỘNG", value: "active" },
    { label: "KHÔNG HOẠT ĐỘNG", value: "unactive" },
    { label: "CHỜ XỬ LÝ", value: "pending" },
  ];

  if (roomData?.roomUtility.furnitureAvailability) {
    furnitures.push(
      ...Object.entries(roomData?.roomUtility.furnitureAvailability).map(
        ([key, value]) => ({
          key,
          value,
        })
      )
    );
  }

  if (roomData?.roomUtility.amenitiesAvailability) {
    amenities.push(
      ...Object.entries(roomData?.roomUtility.amenitiesAvailability).map(
        ([key, value]) => ({
          key,
          value,
        })
      )
    );
  }

  const handleFinish = (values: RoomFinal) => {
    const submissionValues = { ...values };

    submissionValues.roomInfo = values.roomInfo ?? {
      name: roomData?.roomInfo.name || "",
      description: roomData?.roomInfo.description || "",
      address: roomData?.roomInfo.address || "",
      type: roomData?.roomInfo.type || "",
      style: roomData?.roomInfo.style || "",
      floor: roomData?.roomInfo.floor || "",
      width: roomData?.roomInfo.width || 0,
      height: roomData?.roomInfo.height || 0,
      totalArea: roomData?.roomInfo.totalArea || 0,
      capacity: roomData?.roomInfo.capacity || 0,
      numberOfBedrooms: roomData?.roomInfo.numberOfBedrooms || 0,
      numberOfBathrooms: roomData?.roomInfo.numberOfBathrooms || 0,
    };

    if (values.roomInfo?.availableFromDate) {
      submissionValues.roomInfo.availableFromDate = dayjs(
        values.roomInfo.availableFromDate
      ).toISOString(); // ISO 8601 format
    }

    submissionValues.availableFromDate = dayjs(
      values.availableFromDate
    ).toISOString(); // ISO 8601 format
    submissionValues.status = values.status || roomData?.status || "available";

    if (values.pricingDetails?.additionalFees) {
      submissionValues.pricingDetails.additionalFees =
        values.pricingDetails.additionalFees.map((fee) => ({
          type: fee.type,
          amount: fee.amount,
        }));
    }

    const furnituresObj = {};
    const amenitiesObj = {};

    if (submissionValues.furnitureAvailability) {
      submissionValues.furnitureAvailability.forEach((item) => {
        Object.assign(furnituresObj, { [item.key]: item.value });
      });
    }
    if (submissionValues.amenitiesAvailability) {
      submissionValues.amenitiesAvailability.forEach((item) => {
        Object.assign(amenitiesObj, { [item.key]: item.value });
      });
    }

    submissionValues.roomUtility = {
      furnitureAvailability: furnituresObj,
      amenitiesAvailability: amenitiesObj,
    };

    // Giả sử onSubmit là hàm gửi dữ liệu đến server
    try {
      onSubmit(submissionValues);
      // Nếu gửi thành công
      if (roomData) {
        notificationService.room.saveSuccess(values.title); // Đã lưu thành công
      } else {
        notificationService.room.createSuccess(values.title); // Đã tạo mới thành công
      }
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
    } catch (error) {
      // Nếu có lỗi xảy ra khi lưu
      notificationService.room.saveError(); // Lưu thất bại
    }

    console.log("Form values:", submissionValues);
  };

  const handleDateChange: DatePickerProps["onChange"] = (_, dateStr) => {
    console.log("onChange:", dateStr);
  };

  return (
    <div className="p-6 bg-gray-50 max-w-7xl mx-auto shadow-lg rounded-lg">
      <h1 className="text-3xl font-bold text-center text-blue-600 mb-8">
        Tạo Phòng Mới
      </h1>
      <Form
        form={form}
        layout="vertical"
        onFinish={handleFinish}
        initialValues={{
          ...roomData,
          amenitiesAvailability: amenities,
          furnitureAvailability: furnitures,
        }}
        className="space-y-6"
      >
        {/* Title */}
        <Form.Item label="Tiêu đề" name="title">
          <Input
            placeholder={roomData?.title || "Nhập tiêu đề phòng"}
            className="rounded-md"
          />
        </Form.Item>

        {/* Description */}
        <Form.Item label="Mô tả" name="description">
          <Input.TextArea
            placeholder={roomData?.description || "Nhập mô tả phòng"}
            className="rounded-md"
          />
        </Form.Item>

        {/* Room Info */}
        <RoomInfoForm />

        {/* Room Utility */}
        <RoomUtilityForm />

        {/* Pricing Details */}
        <PricingDetailsForm />

        {/* Contact Info */}
        <Form.Item label="Thông tin liên hệ" name="contactInfo">
          <Input
            placeholder={
              roomData?.contactInfo ||
              "Nhập thông tin liên hệ (ví dụ: số điện thoại)"
            }
            className="rounded-md"
          />
        </Form.Item>

        {/* Additional Details */}
        <Form.Item label="Chi tiết thêm" name="additionalDetails">
          <Input.TextArea
            placeholder={roomData?.additionalDetails || "Nhập chi tiết khác"}
            className="rounded-md"
          />
        </Form.Item>

        {/* Available From Date */}
        <ConfigProvider>
          <Form.Item label="Ngày có sẵn" name={["availableFromDate"]}>
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

        {/* Status Select */}
        <Form.Item
          label="Trạng thái"
          name="status"
          rules={[{ required: true, message: "Vui lòng chọn trạng thái" }]}
        >
          <Select
            placeholder="Chọn trạng thái phòng"
            options={statusOptions}
            style={{ width: "100%" }}
            className="rounded-md"
          />
        </Form.Item>

        {/* Submit Button */}
        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            className="w-full py-3 text-lg bg-blue-500 hover:bg-blue-600 text-white rounded-md"
          >
            Lưu
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default NewRoomForm;
