"use client";
import { Layout, Avatar, Dropdown, Menu } from "antd";
import { UserOutlined, LogoutOutlined } from "@ant-design/icons";
import { useRouter } from "next/navigation";
import React from "react";

const { Header } = Layout;

const DashboardHeader: React.FC = () => {
  const router = useRouter();

  const handleLogout = () => {
    // Xóa token từ localStorage
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("firstName");
    localStorage.removeItem("lastName");

    // Xóa token từ cookie
    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

    // Chuyển hướng đến trang đăng nhập
    router.push("/login");
  };

  const menu = (
    <Menu
      items={[
        { key: "1", icon: <UserOutlined />, label: "Profile" },
        {
          key: "2",
          icon: <LogoutOutlined />,
          label: "Logout",
          onClick: handleLogout,
        },
      ]}
    />
  );

  return (
    <Header className="site-layout-background" style={{ padding: "0 16px" }}>
      <div className="flex justify-between items-center">
        <h2 style={{ color: "#fff", margin: 0 }}>Dashboard</h2>
        <Dropdown overlay={menu} placement="bottomRight">
          <Avatar icon={<UserOutlined />} style={{ cursor: "pointer" }} />
        </Dropdown>
      </div>
    </Header>
  );
};

export default DashboardHeader;
