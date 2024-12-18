"use client";
import { Layout, Menu } from "antd";
import {
  DashboardOutlined,
  MessageOutlined,
  HomeOutlined,
} from "@ant-design/icons";
import Link from "next/link";

const { Sider } = Layout;

const DashboardSidebar: React.FC = () => {
  const menuItems = [
    {
      key: "1",
      icon: <DashboardOutlined />,
      label: <Link href="/">Dash Board</Link>,
    },

    {
      key: "3",
      icon: <MessageOutlined />,
      label: <Link href="/support">Support</Link>,
    },

    {
      key: "6",
      icon: <HomeOutlined />,
      label: <Link href="/managementRoom">Management Room</Link>,
    },
  ];

  return (
    <Sider
      width={200}
      className="site-layout-background"
      style={{ minHeight: "100vh" }}
    >
      <Menu
        mode="inline"
        defaultSelectedKeys={["1"]}
        style={{ height: "100%" }}
        items={menuItems}
      />
    </Sider>
  );
};

export default DashboardSidebar;
