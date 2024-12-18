"use client";
import { sampleData } from "@/types/sample-data";
import AnalyticsDashboard from "./component/AnalyticsDashboard";

export default function AnalyticsPage() {
  return <AnalyticsDashboard {...sampleData} />;
}
