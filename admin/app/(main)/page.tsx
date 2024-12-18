"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import HomePage from "./home/page";

const RootPage: React.FC = () => {
  const router = useRouter();
  const [isAuthorized, setIsAuthorized] = useState(false);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const response = await fetch("/api/auth/check", {
          credentials: "include",
        });

        if (!response.ok) {
          router.push("/login");
          return;
        }

        const data = await response.json();
        if (data.authorized) {
          setIsAuthorized(true);
        } else {
          router.push("/login");
        }
      } catch (error) {
        console.error("Auth check failed:", error);
        router.push("/login");
      }
    };

    checkAuth();
  }, [router]);

  if (!isAuthorized) {
    return null; // or loading spinner
  }

  return <HomePage />;
};

export default RootPage;
