// types.ts
export interface User {
  id: string;
  name: string;
  email: string;
  status: "active" | "pending" | "resolved";
  priority: "low" | "medium" | "high";
  category: string;
  createdAt: string;
  lastUpdate: string;
  description: string;
}

export interface SupportTicket {
  id: string;
  userId: string;
  title: string;
  description: string;
  status: "active" | "pending" | "resolved";
  priority: "low" | "medium" | "high";
  category: string;
  createdAt: string;
  messages: Message[];
}

export interface Message {
  id: string;
  userId: string;
  content: string;
  createdAt: string;
  isStaff: boolean;
}
