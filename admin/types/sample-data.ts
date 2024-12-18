// sample-data.ts
export const sampleData = {
  metrics: {
    totalListings: 1245,
    averagePrice: 450000,
    totalRevenue: 12500000,
    occupancyRate: 92,
  },
  trends: [
    { month: "Jan", sales: 45, listings: 120, revenue: 2200000 },
    { month: "Feb", sales: 52, listings: 115, revenue: 2400000 },
    { month: "Mar", sales: 48, listings: 125, revenue: 2300000 },
    { month: "Apr", sales: 55, listings: 130, revenue: 2600000 },
    { month: "May", sales: 60, listings: 122, revenue: 2800000 },
    { month: "Jun", sales: 58, listings: 128, revenue: 2750000 },
  ],
  propertyTypes: [
    { type: "Residential", count: 450, percentage: 45 },
    { type: "Commercial", count: 250, percentage: 25 },
    { type: "Industrial", count: 150, percentage: 15 },
    { type: "Land", count: 100, percentage: 10 },
    { type: "Other", count: 50, percentage: 5 },
  ],
  locationData: [
    { area: "Downtown", salesVolume: 150, averagePrice: 550000, growth: 8.5 },
    { area: "Suburbs", salesVolume: 200, averagePrice: 420000, growth: 12.3 },
    { area: "Waterfront", salesVolume: 80, averagePrice: 750000, growth: 15.7 },
    {
      area: "Business District",
      salesVolume: 120,
      averagePrice: 680000,
      growth: 10.2,
    },
    {
      area: "Historic District",
      salesVolume: 90,
      averagePrice: 620000,
      growth: 9.8,
    },
  ],
};
