import React from "react";
import "../../styles/globals.css"; // Import global styles
export const metadata = {
  title: "NextRoom",
  description:
    "NextRoom - Your ultimate platform for finding and renting rooms with ease.",
};
// change
const AuthLayout = ({ children }: { children: React.ReactNode }) => {
  return (
    <html lang="en" className="h-full">
      <body className="flex  min-h-screen">
        <main className="flex-grow pt-0 bg-gray-100">{children}</main>
      </body>
    </html>
  );
};

export default AuthLayout;
