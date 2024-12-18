// app/api/auth/check/route.ts
import { NextResponse } from "next/server";
import { cookies } from "next/headers";

export async function GET() {
  try {
    const cookieStore = cookies();
    const token = cookieStore.get("token");

    if (!token) {
      return NextResponse.json(
        { authorized: false, message: "No token found" },
        { status: 401 }
      );
    }

    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL_USER}/users/me`,
      {
        headers: {
          Authorization: `Bearer ${token.value}`,
        },
      }
    );

    if (!response.ok) {
      return NextResponse.json(
        { authorized: false, message: "Invalid token" },
        { status: 401 }
      );
    }

    const userData = await response.json();

    return NextResponse.json({
      authorized: true,
      user: userData,
    });
  } catch (error) {
    console.error("Auth check error:", error);
    return NextResponse.json(
      { authorized: false, message: "Server error" },
      { status: 500 }
    );
  }
}
