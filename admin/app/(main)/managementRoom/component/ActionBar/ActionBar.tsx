"use client";
import React from "react";
import { Button, Space } from "antd";
import {
  PlusOutlined,
  ReloadOutlined,
  EditOutlined,
  SettingOutlined,
} from "@ant-design/icons";

interface ActionBarProps {
  onCreate: () => void;
  onReload: () => void;
}

export const ActionBar: React.FC<ActionBarProps> = ({ onCreate, onReload }) => {
  return (
    <div
      style={{
        marginBottom: "16px",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
      }}
    >
      <h2 style={{ margin: 0, fontSize: "16px", fontWeight: 500 }}>
        Danh sách vấn đề
      </h2>
      <Space>
        <Button onClick={onCreate} type="primary" icon={<PlusOutlined />}>
          Mới
        </Button>
        <Button type="text" icon={<ReloadOutlined />} onClick={onReload} />
        <Button type="text" icon={<EditOutlined />} />
        <Button type="text" icon={<SettingOutlined />} />
      </Space>
    </div>
  );
};
