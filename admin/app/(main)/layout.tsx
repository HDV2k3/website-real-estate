
import React from "react";
import { AntdRegistry } from "@ant-design/nextjs-registry";
import "../../styles/globals.css";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import DashboardSidebar from "@/component/DashboardSidebar";
import DashboardHeader from "@/component/DashboardHeader";


export const metadata = {
  title: "NextRoom",
  description:
    "NextRoom - Your ultimate platform for finding and renting rooms with ease.",
};

async function getUser() {
  const cookieStore = cookies();
  const token = cookieStore.get("token");

  if (!token) {
    return null;
  }

  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL_USER}/users/me`,
      {
        headers: {
          Authorization: `Bearer ${token.value}`,
        },
      }
    );

    if (!response.ok) {
      return null;
    }

    const userData = await response.json();
    return userData;
  } catch (error) {
    console.error("Error fetching user:", error);
    return null;
  }
}

export default async function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const userData = await getUser();

  if (!userData) {
    redirect("/login");
  }

  // Check if user has admin role
  const userRoles = userData?.data?.roles || [];
  const isAdmin = userRoles.some(
    (role: { name: string }) => role.name === "ADMIN"
  );

  if (!isAdmin) {
    redirect("/unauthorized");
  }

  return (
    <html lang="en" className="h-full">
      <body className="flex min-h-screen">
        <AntdRegistry>
          <DashboardSidebar />
          <main className="flex-grow pt-0 bg-gray-100">
            <DashboardHeader />
            {children}
          </main>
        </AntdRegistry>
      </body>
    </html>
  );
}
