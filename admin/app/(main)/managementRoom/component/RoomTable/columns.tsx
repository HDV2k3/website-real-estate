"use client";
import { ColumnsType } from "antd/es/table";

import { Space, Button, Dropdown, Menu } from "antd";
import {
  MoreOutlined,
  EditOutlined,
  DeleteFilled,
  CheckOutlined,
  CloseOutlined,
} from "@ant-design/icons";
import Image from "next/image";
import { Room } from "@/types/IssueTracker";

export const createColumns = (
  handleEdit: (room: Room) => void,
  handleDelete: (room: Room) => void,
  handleViewDetail: (room: Room) => void,
  handleAction: (action: string, room: Room) => void,
  loadingDelete: boolean,
  deleteRoomId: string | null
): ColumnsType<Room> => [
  {
    title: "Ảnh",
    dataIndex: "image",
    key: "image",
    render: (image: string) => (
      <Image
        src={image}
        alt="room"
        width={50}
        height={50}
        style={{ objectFit: "cover" }}
      />
    ),
  },
  {
    title: "ID",
    dataIndex: "roomId",
    key: "roomId",
    render: (text: string) => (
      <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
        {text}
        <span style={{ color: "#1890ff" }}>📎</span>
      </div>
    ),
  },
  {
    title: "Trạng thái",
    dataIndex: "status",
    key: "status",
    render: (status: string) => (
      <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
        <span
          style={{
            width: "6px",
            height: "6px",
            backgroundColor: status === "pending" ? "#ff4d4f" : "#52c41a",
            borderRadius: "50%",
            display: "inline-block",
          }}
        />
        <span>
          {status === "pending" ? "Chưa giải quyết" : "Đang hoạt động"}
        </span>
      </div>
    ),
  },
  {
    title: "Nhãn",
    dataIndex: "label",
    key: "label",
    render: (label: string) => (
      <span
        style={{
          padding: "2px 8px",
          borderRadius: "2px",
          fontSize: "12px",
          backgroundColor: label === "bug" ? "#fff1f0" : "#f6ffed",
          color: label === "bug" ? "#ff4d4f" : "#52c41a",
          border: `1px solid ${label === "bug" ? "#ffccc7" : "#b7eb8f"}`,
        }}
      >
        {label}
      </span>
    ),
  },
  {
    title: "Thời gian tạo",
    dataIndex: "createdAt",
    key: "createdAt",
  },
  {
    title: "Giá gốc và khuyến mãi",
    dataIndex: "fixPrice",
    key: "fixPrice",
    render: (fixPrice: number | null, record: Room) => (
      <span style={{ display: "flex", flexDirection: "column" }}>
        {/* Hiển thị giá gốc */}
        <span
          style={{
            color: "#8c8c8c",
            textDecoration: fixPrice && fixPrice > 0 ? "line-through" : "none",
            fontWeight: "normal",
          }}
        >
          {record.basePrice} VNĐ {/* Giá gốc */}
        </span>

        {/* Hiển thị giá khuyến mãi */}
        <span
          style={{
            color: fixPrice && fixPrice > 0 ? "#52c41a" : "#ff4d4f",
            fontWeight: "bold",
            display: "flex",
            alignItems: "center",
          }}
        >
          {fixPrice && fixPrice > 0 ? (
            <>
              {fixPrice} VNĐ Có khuyến mãi &nbsp;
              <CheckOutlined style={{ marginRight: 5, color: "#52c41a" }} />
            </>
          ) : (
            <>
              <CloseOutlined style={{ marginRight: 5, color: "#ff4d4f" }} />
              Không có khuyến mãi
            </>
          )}
        </span>
      </span>
    ),
  },
  {
    title: "Thao tác",
    key: "action",
    render: (record: Room) => (
      <Space>
        <Button
          type="link"
          onClick={(event: React.MouseEvent<HTMLElement>) => {
            event.preventDefault();
            handleEdit(record);
          }}
          style={{ padding: 0 }}
        >
          Sửa
        </Button>
        <Button
          loading={loadingDelete && deleteRoomId === record.id}
          onClick={() => handleDelete(record)}
          type="link"
          style={{ padding: 0 }}
        >
          Xóa
        </Button>
        <Button
          type="link"
          style={{ padding: 0 }}
          onClick={(event: React.MouseEvent<HTMLElement>) => {
            event.preventDefault();
            handleViewDetail(record);
          }}
        >
          Chi tiết
        </Button>
        {/* Dropdown for More Actions */}
        <Dropdown
          overlay={
            <Menu onClick={({ key }) => handleAction(key, record)}>
              <Menu.Item key="addPromotional">
                <EditOutlined /> Thêm vào khuyến mãi
              </Menu.Item>
              <Menu.Item key="deletePromotional">
                <DeleteFilled /> Xóa phòng khuyến mãi
              </Menu.Item>
            </Menu>
          }
          trigger={["click"]}
        >
          <Button type="link" style={{ padding: 0 }}>
            <MoreOutlined />
          </Button>
        </Dropdown>
      </Space>
    ),
  },
];
