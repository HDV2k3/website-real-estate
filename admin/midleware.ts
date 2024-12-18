// middleware.ts
import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import axios from "axios";

// Define protected routes with role requirements
const protectedRoutes = {
  admin: ["/", "/home", "/dashboard"],
  user: ["/user-dashboard", "/profile"],
  public: ["/login", "/unauthorized", "/register"],
};

export async function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;

  // Skip middleware for static files and API routes
  if (
    pathname.startsWith("/_next") ||
    pathname.startsWith("/api") ||
    pathname.includes("/assets") ||
    pathname.includes(".")
  ) {
    return NextResponse.next();
  }

  const token = request.cookies.get("token")?.value;

  // Handle public routes
  if (protectedRoutes.public.includes(pathname)) {
    if (token && pathname === "/login") {
      return NextResponse.redirect(new URL("/home", request.url));
    }
    return NextResponse.next();
  }

  // Redirect to login if no token on protected routes
  if (!token) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  // Verify token and check roles
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL_USER}/users/me`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    const userData = response.data;
    const userRoles = userData?.data?.roles || [];
    const isAdmin = userRoles.some(
      (role: { name: string }) => role.name === "ADMIN"
    );

    // Check if current path requires admin role
    const requiresAdmin = protectedRoutes.admin.includes(pathname);
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const requiresUser = protectedRoutes.user.includes(pathname);

    if (requiresAdmin && !isAdmin) {
      console.log("Access denied: Admin role required");
      return NextResponse.redirect(new URL("/unauthorized", request.url));
    }

    // Allow access if user has appropriate role
    if (!requiresAdmin || (requiresAdmin && isAdmin)) {
      return NextResponse.next();
    }

    // Default to unauthorized if no conditions are met
    return NextResponse.redirect(new URL("/unauthorized", request.url));
  } catch (error) {
    console.error("Auth error:", error);
    const response = NextResponse.redirect(new URL("/login", request.url));
    response.cookies.delete("token");
    return response;
  }
}

export const config = {
  matcher: ["/((?!api|_next|assets|[\\w-]+\\.\\w+).*)"],
};
