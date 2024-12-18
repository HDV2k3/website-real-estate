// app/Unauthorized.tsx
"use client";
import React from "react";
import { Button } from "antd";

const Unauthorized: React.FC = () => {
  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="text-center">
        <h1 className="text-4xl font-bold text-red-600">Unauthorized</h1>
        <p className="mt-4 text-lg text-gray-700">
          You do not have permission to access this page.
        </p>
        <p className="mt-2 text-sm text-gray-500">
          Please check your account permissions or contact support.
        </p>
        <Button
          type="primary"
          className="mt-6"
          onClick={() => (window.location.href = "/login")}
        >
          Go to Home
        </Button>
      </div>
    </div>
  );
};

export default Unauthorized;
