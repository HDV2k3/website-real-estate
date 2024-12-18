// components/DashboardContent.tsx
"use client";
import React from "react";
import {
  PieChart,
  BarChart,
  Bar,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";
import {
  Building2,
  Home,
  DollarSign,
  Users,
  ArrowUpRight,
  ArrowDownRight,
} from "lucide-react";

// Define types for the DashboardCard props
interface DashboardCardProps {
  title: string;
  value: string;
  icon: React.ElementType; // React component type for the icon
  trend: "up" | "down";
  trendValue: string;
}

const DashboardCard: React.FC<DashboardCardProps> = ({
  title,
  value,
  icon: Icon,
  trend,
  trendValue,
}) => (
  <div className="bg-white p-6 rounded-lg shadow-sm border">
    <div className="flex items-center justify-between">
      <div>
        <p className="text-sm text-gray-500">{title}</p>
        <h3 className="text-2xl font-semibold mt-1">{value}</h3>
      </div>
      <div className="bg-blue-50 p-3 rounded-full">
        <Icon className="w-6 h-6 text-blue-500" />
      </div>
    </div>
    <div className="mt-4 flex items-center">
      {trend === "up" ? (
        <ArrowUpRight className="w-4 h-4 text-green-500" />
      ) : (
        <ArrowDownRight className="w-4 h-4 text-red-500" />
      )}
      <span
        className={`text-sm ${
          trend === "up" ? "text-green-500" : "text-red-500"
        }`}
      >
        {trendValue}%
      </span>
      <span className="text-sm text-gray-500 ml-2">vs last month</span>
    </div>
  </div>
);

const propertyData = [
  { name: "Houses", value: 45 },
  { name: "Apartments", value: 30 },
  { name: "Condos", value: 15 },
  { name: "Land", value: 10 },
];

const revenueData = [
  { month: "Jan", revenue: 35000 },
  { month: "Feb", revenue: 42000 },
  { month: "Mar", revenue: 38000 },
  { month: "Apr", revenue: 45000 },
  { month: "May", revenue: 52000 },
  { month: "Jun", revenue: 48000 },
];

// Define color palette for pie chart cells
const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042"];

const Dashboard: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-2xl font-bold mb-8">Real Estate Dashboard</h1>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <DashboardCard
            title="Total Properties"
            value="324"
            icon={Building2}
            trend="up"
            trendValue="12.5"
          />
          <DashboardCard
            title="Available Properties"
            value="145"
            icon={Home}
            trend="down"
            trendValue="4.2"
          />
          <DashboardCard
            title="Total Revenue"
            value="$842,500"
            icon={DollarSign}
            trend="up"
            trendValue="8.1"
          />
          <DashboardCard
            title="Active Clients"
            value="1,234"
            icon={Users}
            trend="up"
            trendValue="15.3"
          />
        </div>

        {/* Charts Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Property Distribution */}
          <div className="bg-white p-6 rounded-lg shadow-sm border">
            <h2 className="text-lg font-semibold mb-4">
              Property Distribution
            </h2>
            <div className="flex justify-center">
              <PieChart width={400} height={300}>
                <Pie
                  data={propertyData}
                  cx={200}
                  cy={150}
                  outerRadius={100}
                  fill="#8884d8"
                  dataKey="value"
                  label
                >
                  {propertyData.map((entry, index) => (
                    <Cell
                      key={`cell-${index}`}
                      fill={COLORS[index % COLORS.length]}
                    />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </div>
          </div>

          {/* Monthly Revenue */}
          <div className="bg-white p-6 rounded-lg shadow-sm border">
            <h2 className="text-lg font-semibold mb-4">Monthly Revenue</h2>
            <BarChart width={400} height={300} data={revenueData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="revenue" fill="#0088FE" />
            </BarChart>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
