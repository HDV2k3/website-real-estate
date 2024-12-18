"use client";
import React from "react";
import {
  FaCheckCircle,
  FaShower,
  FaPlug,
  FaBed,
  FaUsers,
} from "react-icons/fa";
import { EnvironmentFilled, TagFilled } from "@ant-design/icons";
import { Button } from "antd";
import Link from "next/link";
import { convertToVND } from "@/utils/convertToVND";

// Subcomponent for displaying utility items
const UtilityItem: React.FC<{ label: string }> = ({ label }) => (
  <div className="flex items-center text-gray-500 mr-4 font-semibold text-sm">
    <FaCheckCircle className="mr-2 text-green-600" />
    {label}
  </div>
);

// Subcomponent for displaying service prices
const ServicePrice: React.FC<{
  icon: JSX.Element;
  label: string;
  price: string;
}> = ({ icon, label, price }) => (
  <div className="flex items-center mr-8 whitespace-nowrap text-sm">
    {icon}
    <span className="mr-1 text-gray-600">{label}:</span>
    <span>{price}</span>
  </div>
);

interface IProps {
  room: Room;
}
const InfoDetail: React.FC<IProps> = ({ room }) => {
  const glitterStyle = `
    @keyframes glitter {
      0% { background-position: 0 0; }
      50% { background-position: 400% 0; }
      100% { background-position: 0 0; }
    }
    .glitter-text {
      background: linear-gradient(90deg, #00ff00, #39ff14, #7cfc00, #32cd32);
      background-size: 400% 400%;
      color: transparent;
      -webkit-background-clip: text;
      background-clip: text;
      animation: glitter 10s ease-in-out infinite;
    }
  `;

  const hasPromotionalPrice =
    (room.fixPrice ?? 0) < room.pricingDetails.basePrice;

  // Determine the status message based on room's status
  const roomStatusMessage =
    room.status === "active"
      ? "Phòng hiện đang trống"
      : "Phòng đã có người thuê";

  return (
    <div className="float-right px-2">
      <style>{glitterStyle}</style>
      <div className="text-lg font-bold mb-4">
        <div className="flex">
          <span className="glitter-text">{room.title}</span>
        </div>
      </div>

      {/* Room Address, Type, and Additional Details in Grid Layout */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 mb-4 text-sm">
        {/* Room Address */}
        <div className="flex items-center">
          <EnvironmentFilled className="mr-2" />
          <span>{room.roomInfo.address}</span>
        </div>

        {/* Room Type */}
        <div className="flex items-center">
          <TagFilled className="mr-2" />
          <span>{room.roomInfo.type}</span>
        </div>

        {/* Additional Room Details */}
        <div className="flex items-center">
          <span className="mr-2">Diện tích:</span>
          <span>{room.roomInfo.totalArea} m²</span>
        </div>

        <div className="flex items-center mb-2 text-sm">
          <FaUsers className="mr-2" />
          <span className="mr-2">:</span>
          <span>{room.roomInfo.capacity} người</span>
        </div>

        <div className="flex items-center mb-2 text-sm">
          <FaBed className="mr-2" />
          <span className="mr-2">Phòng ngủ:</span>
          <span>{room.roomInfo.numberOfBedrooms}</span>
        </div>

        <div className="flex items-center mb-2 text-sm">
          <FaShower className="mr-2" />
          <span className="mr-2">Nhà tắm:</span>
          <span>{room.roomInfo.numberOfBathrooms}</span>
        </div>
      </div>
      {/* Display Room Status */}
      <div className="mb-4 text-sm text-gray-600">
        <span
          className={`font-bold ${
            room.status === "available" ? "text-green-600" : "text-red-600"
          }`}
        >
          {roomStatusMessage}
        </span>
      </div>
      {/* Utilities and Pricing */}
      <div className="text-xl font-bold mb-4">
        <h3 className="text-base font-semibold underline mb-2">
          Tiện ích Phòng
        </h3>
        <div className="flex flex-wrap mb-2">
          {Object.entries(room.roomUtility.furnitureAvailability)
            .filter(([, value]) => value)
            .map(([key]) => (
              <UtilityItem key={key} label={key} />
            ))}
        </div>

        {/* Service Prices */}
        <h3 className="text-base font-semibold underline mb-2">Giá dịch vụ</h3>
        <div className="flex flex-wrap mb-4">
          <ServicePrice
            icon={<FaPlug className="mr-2" />}
            label="Giá điện"
            price={`${convertToVND(room.pricingDetails.electricityCost)} / Kwh`}
          />
          <ServicePrice
            icon={<FaShower className="mr-2" />}
            label="Giá nước"
            price={`${convertToVND(room.pricingDetails.waterCost)} / m³`}
          />
        </div>

        {/* Rental Price */}
        <h3 className="text-base font-semibold underline text-gray-600 mb-2">
          Giá thuê Phòng
        </h3>
        <div className="flex items-center">
          {hasPromotionalPrice && room.fixPrice ? (
            <>
              <span className="mr-2 text-xl text-red-600 line-through">
                {convertToVND(room.pricingDetails.basePrice)}
              </span>
              <span className="mr-2 text-xl">chỉ còn</span>
              <span className="mr-2 text-xl text-red-600">
                {convertToVND(room.fixPrice)}
              </span>
              <span className="text-xl">/ tháng</span>
            </>
          ) : (
            <>
              <span className="mr-2 text-xl text-red-600">
                {convertToVND(room.pricingDetails.basePrice)}
              </span>
              <span className="text-xl">/ tháng</span>
            </>
          )}
        </div>
      </div>

      {/* Contact and Zalo Button */}
      <div className="flex justify-between mb-5">
        <div className="flex justify-start">
          <Link href={`/messages`} passHref>
            <Button>liên hệ ngay: {room.createdBy}</Button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default InfoDetail;
