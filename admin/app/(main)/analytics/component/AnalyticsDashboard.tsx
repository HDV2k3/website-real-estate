import React, { useState } from "react";
import {
  LineChart,
  Line,
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";
import {
  TrendingUp,
  DollarSign,
  Home,
  PieChart as PieChartIcon,
} from "lucide-react";

// Import types from types.ts
type TimeRange = "7days" | "30days" | "90days" | "1year";

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042", "#8884D8"];

const StatCard = ({
  title,
  value,
  icon: Icon,
  change,
}: {
  title: string;
  value: string;
  icon: React.ElementType;
  change: number;
}) => (
  <div className="bg-white p-6 rounded-lg shadow-sm">
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
  </div>
);

const AnalyticsDashboard: React.FC<AnalyticsProps> = ({
  metrics = {
    totalListings: 0,
    averagePrice: 0,
    totalRevenue: 0,
    occupancyRate: 0,
  }, // Default values if metrics are undefined
  trends = [],
  propertyTypes = [],
  locationData = [],
}) => {
  const [timeRange, setTimeRange] = useState<TimeRange>("30days");

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-2xl font-bold">Real Estate Analytics</h1>
          <select
            value={timeRange}
            onChange={(e) => setTimeRange(e.target.value as TimeRange)}
            className="bg-white border rounded-md px-4 py-2"
          >
            <option value="7days">Last 7 Days</option>
            <option value="30days">Last 30 Days</option>
            <option value="90days">Last 90 Days</option>
            <option value="1year">Last Year</option>
          </select>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <StatCard
            title="Total Listings"
            value={metrics.totalListings.toString()}
            icon={Home}
            change={5.2}
          />
          <StatCard
            title="Average Price"
            value={`$${metrics.averagePrice.toLocaleString()}`}
            icon={DollarSign}
            change={3.8}
          />
          <StatCard
            title="Total Revenue"
            value={`$${metrics.totalRevenue.toLocaleString()}`}
            icon={TrendingUp}
            change={7.5}
          />
          <StatCard
            title="Occupancy Rate"
            value={`${metrics.occupancyRate}%`}
            icon={PieChartIcon}
            change={-2.1}
          />
        </div>

        {/* Charts Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          {/* Trends Chart */}
          <div className="bg-white p-6 rounded-lg shadow-sm">
            <h2 className="text-lg font-semibold mb-4">Market Trends</h2>
            <LineChart width={600} height={300} data={trends}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="sales" stroke="#0088FE" />
              <Line type="monotone" dataKey="listings" stroke="#00C49F" />
              <Line type="monotone" dataKey="revenue" stroke="#FFBB28" />
            </LineChart>
          </div>

          {/* Property Types Distribution */}
          <div className="bg-white p-6 rounded-lg shadow-sm">
            <h2 className="text-lg font-semibold mb-4">
              Property Types Distribution
            </h2>
            <PieChart width={600} height={300}>
              <Pie
                data={propertyTypes}
                cx={300}
                cy={150}
                outerRadius={100}
                fill="#8884d8"
                dataKey="percentage"
                label
              >
                {propertyTypes.map((entry, index) => (
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

        {/* Location Performance */}
        <div className="bg-white p-6 rounded-lg shadow-sm">
          <h2 className="text-lg font-semibold mb-4">Location Performance</h2>
          <div className="overflow-x-auto">
            <BarChart width={1000} height={300} data={locationData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="area" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="salesVolume" fill="#0088FE" name="Sales Volume" />
              <Bar dataKey="averagePrice" fill="#00C49F" name="Average Price" />
              <Bar dataKey="growth" fill="#FFBB28" name="Growth %" />
            </BarChart>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AnalyticsDashboard;
