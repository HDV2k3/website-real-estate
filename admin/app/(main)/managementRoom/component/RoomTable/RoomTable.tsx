"use client";
import React from "react";
import { Table } from "antd";
import { createColumns } from "./columns";
import { Room } from "@/types/IssueTracker";

interface RoomTableProps {
  data: Room[];
  currentPage: number;
  totalItems: number;
  pageSize: number;
  onPageChange: (page: number) => void;
  handleEdit: (room: Room) => void;
  handleDelete: (room: Room) => void;
  handleViewDetail: (room: Room) => void;
  handleAction: (action: string, room: Room) => void;
  loadingDelete: boolean;
  deleteRoomId: string | null;
}

export const RoomTable: React.FC<RoomTableProps> = ({
  data,
  currentPage,
  totalItems,
  pageSize,
  onPageChange,
  ...handlers
}) => {
  const columns = createColumns(
    handlers.handleEdit,
    handlers.handleDelete,
    handlers.handleViewDetail,
    handlers.handleAction,
    handlers.loadingDelete,
    handlers.deleteRoomId
  );

  return (
    <Table
      columns={columns}
      dataSource={data}
      pagination={{
        current: currentPage,
        total: totalItems,
        pageSize: pageSize,
        onChange: onPageChange,
        showSizeChanger: false,
      }}
    />
  );
};
