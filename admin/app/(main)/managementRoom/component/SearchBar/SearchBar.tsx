import React, { useState } from "react";
import { Input, Select, DatePicker, Button } from "antd";
const { RangePicker } = DatePicker;
import dayjs from "dayjs";

interface SearchBarProps {
  onSearch: (searchParams: {
    title: string;
    status: string;
    dateRange: [string, string] | null;
  }) => void;
  onReset: () => void;
}

export const SearchBar: React.FC<SearchBarProps> = ({ onSearch, onReset }) => {
  const [title, setTitle] = useState<string>("");
  const [status, setStatus] = useState<string>("Trạng Thái"); // Đặt mặc định là rỗng hoặc giá trị hợp lệ
  const [dateRange, setDateRange] = useState<[string, string] | null>(null);

  const handleSearch = () => {
    console.log("Trước khi gửi tham số tìm kiếm:", {
      title,
      status,
      dateRange,
    });
    onSearch({ title, status, dateRange });
  };

  const handleReset = () => {
    setTitle("");
    setStatus("");
    setDateRange(null);
    onReset();
  };

  return (
    <div style={{ marginBottom: "16px", display: "flex", gap: "16px" }}>
      <Input
        placeholder="Nhập tiêu đề..."
        style={{ flex: 1 }}
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <Select
        value={status}
        style={{ width: 200 }}
        onChange={(value) => setStatus(value)}
        options={[
          { label: "Chưa giải quyết", value: "pending" },
          { label: "Đang hoạt động", value: "active" },
        ]}
      />
      <RangePicker
        style={{ width: 300 }}
        value={dateRange ? [dayjs(dateRange[0]), dayjs(dateRange[1])] : null}
        onChange={(dates) => {
          // Chuyển đổi ngày thành định dạng 'YYYY-MM-DD' (chỉ lấy phần ngày)
          setDateRange(
            dates?.[0] && dates?.[1]
              ? [
                  dates[0].format("YYYY-MM-DD"), // Chỉ lấy phần ngày
                  dates[1].format("YYYY-MM-DD"), // Chỉ lấy phần ngày
                ]
              : null
          );
        }}
      />
      <Button onClick={handleReset}>Đặt lại</Button>
      <Button type="primary" onClick={handleSearch}>
        Tìm kiếm
      </Button>
    </div>
  );
};
