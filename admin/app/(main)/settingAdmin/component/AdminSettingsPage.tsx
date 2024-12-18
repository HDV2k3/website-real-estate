/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState } from "react";
import {
  Layout,
  Menu,
  Form,
  Input,
  Button,
  Switch,
  Checkbox,
  Space,
} from "antd";
import {
  UserOutlined,
  SettingOutlined,
  NotificationOutlined,
} from "@ant-design/icons";

const { Sider, Content } = Layout;

const AdminSettingsPage = () => {
  const [form] = Form.useForm();
  const [notificationsEnabled, setNotificationsEnabled] = useState(true);
  const [darkMode, setDarkMode] = useState(false);

  const handleFormSubmit = (values: any) => {
    console.log("Form Submitted", values);
  };

  return (
    <Layout className="min-h-screen bg-gray-100">
      <Sider width={250} className="bg-white">
        <div className="text-center py-6">
          <h2 className="text-xl font-semibold">Admin Settings</h2>
        </div>
        <Menu mode="inline" defaultSelectedKeys={["1"]}>
          <Menu.Item key="1" icon={<UserOutlined />}>
            User Settings
          </Menu.Item>
          <Menu.Item key="2" icon={<SettingOutlined />}>
            General Settings
          </Menu.Item>
          <Menu.Item key="3" icon={<NotificationOutlined />}>
            Notifications
          </Menu.Item>
        </Menu>
      </Sider>

      <Layout>
        <Content className="p-8">
          <div className="max-w-4xl mx-auto bg-white p-6 rounded-lg shadow-md">
            <h1 className="text-2xl font-semibold mb-4">General Settings</h1>
            <Form form={form} onFinish={handleFormSubmit}>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 mb-6">
                <Form.Item
                  name="siteName"
                  label="Site Name"
                  rules={[
                    { required: true, message: "Please enter the site name" },
                  ]}
                >
                  <Input placeholder="Enter site name" />
                </Form.Item>

                <Form.Item
                  name="adminEmail"
                  label="Admin Email"
                  rules={[
                    { required: true, message: "Please enter the admin email" },
                  ]}
                >
                  <Input placeholder="Enter admin email" />
                </Form.Item>

                <Form.Item name="darkMode" label="Dark Mode">
                  <Switch
                    checked={darkMode}
                    onChange={() => setDarkMode(!darkMode)}
                    className="mt-2"
                  />
                </Form.Item>

                <Form.Item name="notifications" label="Enable Notifications">
                  <Switch
                    checked={notificationsEnabled}
                    onChange={() =>
                      setNotificationsEnabled(!notificationsEnabled)
                    }
                    className="mt-2"
                  />
                </Form.Item>
              </div>

              <div className="mb-6">
                <Form.Item name="roles" label="Roles">
                  <Checkbox.Group>
                    <Space direction="vertical">
                      <Checkbox value="admin">Admin</Checkbox>
                      <Checkbox value="editor">Editor</Checkbox>
                      <Checkbox value="viewer">Viewer</Checkbox>
                    </Space>
                  </Checkbox.Group>
                </Form.Item>
              </div>

              <Button type="primary" htmlType="submit" className="w-full">
                Save Settings
              </Button>
            </Form>
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default AdminSettingsPage;
