import axios from "axios";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL_MARKETING;

export const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});
export const apiNonToken = axios.create({
  baseURL: BASE_URL,
});
