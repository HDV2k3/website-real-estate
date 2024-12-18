// types.ts
interface PropertyMetrics {
  totalListings: number;
  averagePrice: number;
  totalRevenue: number;
  occupancyRate: number;
}

interface PropertyTrend {
  month: string;
  sales: number;
  listings: number;
  revenue: number;
}

interface PropertyType {
  type: string;
  count: number;
  percentage: number;
}

interface LocationData {
  area: string;
  salesVolume: number;
  averagePrice: number;
  growth: number;
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
interface AnalyticsProps {
  metrics: PropertyMetrics;
  trends: PropertyTrend[];
  propertyTypes: PropertyType[];
  locationData: LocationData[];
}
