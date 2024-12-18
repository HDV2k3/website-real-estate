// types.ts
export interface SalesReport {
  id: string;
  propertyId: string;
  propertyTitle: string;
  salePrice: number;
  saleDate: string;
  agent: string;
  commission: number;
  location: string;
  propertyType: string;
}

export interface RevenueData {
  period: string;
  revenue: number;
  transactions: number;
  averagePrice: number;
  growth: number;
}

export interface AgentPerformance {
  agentId: string;
  name: string;
  totalSales: number;
  totalRevenue: number;
  avgDaysToSell: number;
  activeListings: number;
  commission: number;
}

export interface PropertyTypeMetrics {
  type: string;
  totalSales: number;
  averagePrice: number;
  inventory: number;
  avgDaysOnMarket: number;
}

export interface ReportFilters {
  dateRange: [Date, Date];
  propertyTypes: string[];
  locations: string[];
  agents: string[];
  priceRange: [number, number];
}
