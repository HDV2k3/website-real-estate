// UploadImagesPage.tsx
"use client";

import React, { useEffect, useState } from "react";
import { useRouter, useParams } from "next/navigation";
import { notification, UploadFile } from "antd";
import axios from "axios";
import RoomImageUpload from "../../component/edit_create/component/UploadImages";

const UploadImagesPage: React.FC = () => {
  const { id: roomId } = useParams();
  const router = useRouter();
  const [initialImages, setInitialImages] = useState([]); // Initial images if any

  useEffect(() => {
    // Fetch any existing images for the room to display
    const fetchRoomImages = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          `${process.env.NEXT_PUBLIC_API_URL_MARKETING}/post/${roomId}/images`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.status === 200) {
          setInitialImages(response.data.images || []);
        }
      } catch (error) {
        console.error("Error fetching room images:", error);
      }
    };

    if (roomId) {
      fetchRoomImages();
    }
  }, [roomId]);

  const handleImageUpload = (uploadedImages: UploadFile[]) => {
    // Handle the images once uploaded
    console.log("Uploaded Images:", uploadedImages);
    notification.success({
      message: "Images uploaded successfully!",
    });
  };

  if (!roomId) {
    return <p>Loading...</p>; // Wait for room ID to be available
  }

  return (
    <div className="p-6">
      <h1>Upload Images for Room</h1>
      <RoomImageUpload
        postId={roomId.toString()}
        initialImages={initialImages}
        onChange={handleImageUpload}
      />
      <button onClick={() => router.push(`/managementRoom/${roomId}`)}>
        Back to Room Details
      </button>
    </div>
  );
};

export default UploadImagesPage;
