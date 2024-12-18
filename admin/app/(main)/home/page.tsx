// pages/dashboard/index.tsx
"use client";
import { Layout } from "antd";
import DashboardContent from "./components/Content";

const { Content } = Layout;

const HomePage: React.FC = () => (
  <Layout style={{ minHeight: "100vh" }}>
    <Layout>
      <Content style={{ margin: "16px" }}>
        <DashboardContent />
      </Content>
    </Layout>
  </Layout>
);

export default HomePage;
