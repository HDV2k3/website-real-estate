import React, { useState } from "react";
import {
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import { Download, TrendingUp, Users, Home, DollarSign } from "lucide-react";
import { Button, Select, DatePicker, Card, Col, Row, Table } from "antd";

import dayjs from "dayjs";
import {
  AgentPerformance,
  PropertyTypeMetrics,
  RevenueData,
} from "@/types/report";

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042", "#8884D8"];

const StatCard = ({
  title,
  value,
  change,
  icon: Icon,
}: {
  title: string;
  value: string;
  change: number;
  icon: React.ElementType;
}) => (
  <Card bordered={false} className="p-6 rounded-lg shadow-sm">
    <div className="flex justify-between items-center">
      <div>
        <p className="text-gray-500 text-sm">{title}</p>
        <p className="text-2xl font-bold mt-2">{value}</p>
      </div>
      <div className="bg-blue-50 p-3 rounded-full">
        <Icon className="w-6 h-6 text-blue-600" />
      </div>
    </div>
    <div className="mt-4">
      <span
        className={`text-sm ${change >= 0 ? "text-green-500" : "text-red-500"}`}
      >
        {change >= 0 ? "+" : ""}
        {change}%
      </span>
      <span className="text-gray-500 text-sm ml-2">vs last period</span>
    </div>
  </Card>
);

const ReportPage: React.FC = () => {
  const [dateRange, setDateRange] = useState<[Date, Date]>([
    new Date(),
    new Date(),
  ]);
  const [selectedPropertyTypes, setSelectedPropertyTypes] = useState<string[]>(
    []
  );
  const [selectedLocations, setSelectedLocations] = useState<string[]>([]);
  const [selectedAgents, setSelectedAgents] = useState<string[]>([]);
  const isValid = (date: Date) => !isNaN(date.getTime());
  // Sample data - replace with real data
  const revenueData: RevenueData[] = [
    {
      period: "Jan",
      revenue: 320000,
      transactions: 28,
      averagePrice: 450000,
      growth: 5.2,
    },
    {
      period: "Feb",
      revenue: 380000,
      transactions: 32,
      averagePrice: 460000,
      growth: 6.8,
    },
    {
      period: "Mar",
      revenue: 420000,
      transactions: 35,
      averagePrice: 470000,
      growth: 7.5,
    },
  ];

  const agentPerformance: AgentPerformance[] = [
    {
      agentId: "1",
      name: "John Doe",
      totalSales: 12,
      totalRevenue: 5400000,
      avgDaysToSell: 45,
      activeListings: 8,
      commission: 135000,
    },
  ];

  const propertyMetrics: PropertyTypeMetrics[] = [
    {
      type: "Residential",
      totalSales: 45,
      averagePrice: 450000,
      inventory: 120,
      avgDaysOnMarket: 35,
    },
  ];

  const handleExportReport = () => {
    // Implement export functionality
    console.log("Exporting report...");
  };

  const columns = [
    { title: "Agent", dataIndex: "name", key: "name" },
    { title: "Total Sales", dataIndex: "totalSales", key: "totalSales" },
    {
      title: "Revenue",
      dataIndex: "totalRevenue",
      key: "totalRevenue",
      render: (text: number) => `$${text.toLocaleString()}`,
    },
    {
      title: "Avg Days to Sell",
      dataIndex: "avgDaysToSell",
      key: "avgDaysToSell",
    },
    {
      title: "Active Listings",
      dataIndex: "activeListings",
      key: "activeListings",
    },
    {
      title: "Commission",
      dataIndex: "commission",
      key: "commission",
      render: (text: number) => `$${text.toLocaleString()}`,
    },
  ];

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-2xl font-bold">Sales Reports & Analytics</h1>
          <Button
            type="primary"
            icon={<Download />}
            onClick={handleExportReport}
            className="flex items-center gap-2"
          >
            Export Report
          </Button>
        </div>

        {/* Filters */}
        <div className="bg-white p-6 rounded-lg shadow-sm mb-8">
          <Row gutter={16}>
            <Col span={6}>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Date Range
              </label>
              <DatePicker.RangePicker
                value={[dayjs(dateRange[0]), dayjs(dateRange[1])]} // convert to dayjs
                onChange={(dates) => {
                  if (dates && dates[0] && dates[1]) {
                    const startDate = dates[0].toDate();
                    const endDate = dates[1].toDate();
                    if (isValid(startDate) && isValid(endDate)) {
                      setDateRange([startDate, endDate]);
                    }
                  }
                }}
              />
            </Col>
            <Col span={6}>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Property Type
              </label>
              <Select
                mode="multiple"
                placeholder="Select property type"
                value={selectedPropertyTypes}
                onChange={setSelectedPropertyTypes}
                className="w-full"
              >
                <Select.Option value="residential">Residential</Select.Option>
                <Select.Option value="commercial">Commercial</Select.Option>
                <Select.Option value="industrial">Industrial</Select.Option>
              </Select>
            </Col>
            <Col span={6}>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Location
              </label>
              <Select
                mode="multiple"
                placeholder="Select location"
                value={selectedLocations}
                onChange={setSelectedLocations}
                className="w-full"
              >
                <Select.Option value="downtown">Downtown</Select.Option>
                <Select.Option value="suburban">Suburban</Select.Option>
                <Select.Option value="rural">Rural</Select.Option>
              </Select>
            </Col>
            <Col span={6}>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Agent
              </label>
              <Select
                mode="multiple"
                placeholder="Select agent"
                value={selectedAgents}
                onChange={setSelectedAgents}
                className="w-full"
              >
                <Select.Option value="1">John Doe</Select.Option>
                <Select.Option value="2">Jane Smith</Select.Option>
              </Select>
            </Col>
          </Row>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <StatCard
            title="Total Revenue"
            value="$1,420,500"
            change={12.5}
            icon={DollarSign}
          />
          <StatCard
            title="Total Sales"
            value="95"
            change={8.2}
            icon={TrendingUp}
          />
          <StatCard
            title="Active Agents"
            value="24"
            change={5.0}
            icon={Users}
          />
          <StatCard
            title="Available Properties"
            value="142"
            change={-2.5}
            icon={Home}
          />
        </div>

        {/* Charts Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          {/* Revenue Trend */}
          <div className="bg-white p-6 rounded-lg shadow-sm">
            <h2 className="text-lg font-semibold mb-4">Revenue Trend</h2>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={revenueData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="period" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line
                  type="monotone"
                  dataKey="revenue"
                  stroke="#0088FE"
                  name="Revenue"
                />
                <Line
                  type="monotone"
                  dataKey="transactions"
                  stroke="#00C49F"
                  name="Transactions"
                />
              </LineChart>
            </ResponsiveContainer>
          </div>

          {/* Property Type Distribution */}
          <div className="bg-white p-6 rounded-lg shadow-sm">
            <h2 className="text-lg font-semibold mb-4">
              Property Type Distribution
            </h2>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={propertyMetrics}
                  dataKey="totalSales"
                  nameKey="type"
                  cx="50%"
                  cy="50%"
                  outerRadius={80}
                  label
                >
                  {propertyMetrics.map((entry, index) => (
                    <Cell key={index} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Agent Performance Table */}
        <div className="bg-white p-6 rounded-lg shadow-sm mb-8">
          <h2 className="text-lg font-semibold mb-4">Agent Performance</h2>
          <Table
            columns={columns}
            dataSource={agentPerformance}
            rowKey="agentId"
            pagination={false}
            bordered
          />
        </div>
      </div>
    </div>
  );
};

export default ReportPage;
