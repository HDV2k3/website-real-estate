"use client";
import "antd/dist/reset.css";
import { useParams, useSearchParams } from "next/navigation";
import "../component/detail/component/style/info-detail-wrapper.css";
import { useRoomDetail } from "@/hooks/useRoomDetail";

import SquareAndRectanglesImageDetail from "../component/detail/component/ImageDetail/SquareAndRectanglesImageDetail";
import InfoDetail from "../component/detail/component/InfoDetail/InfoDetail";
import { useRoomPromotionalDetail } from "@/hooks/useRoomPromotionalDetail";
// Loading and Error States
function Loading() {
  return <div className="text-center py-10">Loading...</div>;
}

function ErrorMessage({ message }: { readonly message: string }) {
  return <div className="text-center py-10">{message}</div>;
}

// Room Detail Content Component
// eslint-disable-next-line @typescript-eslint/no-explicit-any
function RoomDetailContent({ roomData }: { readonly roomData: any }) {
  return (
    <div className="container mx-auto px-4 md:px-6 lg:px-8">
      <div className="flex flex-col md:flex-row md:gap-6 lg:gap-8">
        {/* Image Section */}
        <div className="w-full md:w-1/2 lg:w-2/3 mb-2">
          <SquareAndRectanglesImageDetail room={roomData} />
        </div>

        {/* Info Section */}
        <div className="w-full md:w-1/2 lg:w-1/3 mb-6 info-detail-wrapper">
          <InfoDetail room={roomData} />
        </div>
      </div>

     
    </div>
  );
}

// Detail Fetching Components
function RegularRoomDetail({ id }: { readonly id: string }) {
  const { roomData, isLoading, isError } = useRoomDetail(id);

  if (isLoading) return <Loading />;
  if (isError) return <ErrorMessage message="Error loading room details" />;
  if (!roomData) return <ErrorMessage message="Room details not available" />;

  return <RoomDetailContent roomData={roomData} />;
}

function PromotionalRoomDetail({ id }: { readonly id: string }) {
  const { roomData, isLoading, isError } = useRoomPromotionalDetail(id);

  if (isLoading) return <Loading />;
  if (isError)
    return <ErrorMessage message="Error loading promotional room details" />;
  if (!roomData)
    return <ErrorMessage message="Promotional room details not available" />;

  return <RoomDetailContent roomData={roomData} />;
}

// Main Component
function RoomDetail() {
  const params = useParams();
  const searchParams = useSearchParams();
  const id = params?.id as string | undefined;
  const roomType = searchParams?.get("type");

  if (!id) return <ErrorMessage message="Invalid room ID" />;

  return roomType === "promotional" ? (
    <PromotionalRoomDetail id={id} />
  ) : (
    <RegularRoomDetail id={id} />
  );
}

export default RoomDetail;
