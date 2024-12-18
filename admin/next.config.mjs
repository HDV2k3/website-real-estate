
/**
 * @type {import('next').NextConfig}
 * @description Configuration options for the Next.js application.
 */
const nextConfig = {
  /* config options here */
  images: {
    domains: [
      'media.istockphoto.com', 
      'firebasestorage.googleapis.com', 
      'storage.googleapis.com',
      'cdn.chotot.com',
      'images.unsplash.com'
    ],
  },
  env: {
    NEXT_PUBLIC_API_URL_SOCKET: process.env.NEXT_PUBLIC_API_URL_SOCKET,
    NEXT_PUBLIC_API_URL_CHATTING: process.env.NEXT_PUBLIC_API_URL_CHATTING,
    NEXT_PUBLIC_API_URL_MARKETING: process.env.NEXT_PUBLIC_API_URL_MARKETING,
    NEXT_PUBLIC_API_URL_USER: process.env.NEXT_PUBLIC_API_URL_USER,
    NEXT_PUBLIC_API_URL_CHAT_BOT: process.env.NEXT_PUBLIC_API_URL_CHAT_BOT,
  },
};

export default nextConfig;
