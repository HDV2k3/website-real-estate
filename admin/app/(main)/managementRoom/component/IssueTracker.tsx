"use client";
import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { Room } from "@/types/IssueTracker";
import { roomService } from "@/service/roomService";
import { SearchBar } from "./SearchBar/SearchBar";
import { ActionBar } from "./ActionBar/ActionBar";
import { PriceModal } from "./PriceModal/PriceModal";
import { RoomTable } from "./RoomTable/RoomTable";
import axios from "axios";
import { notificationService } from "@/service/notificationService";

const IssueTracker: React.FC = () => {
  const [data, setData] = useState<Room[]>([]);
  const [size, setPageSize] = useState(5);
  const [totalItems, setTotalItems] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [loadingDelete, setLoadingDelete] = useState(false);
  const [deleteRoomId] = useState<string | null>(null);
  const [visibleModal, setVisibleModal] = useState(false);
  const [fixPrice, setFixPrice] = useState<number | null>(null);
  const [selectedRoom, setSelectedRoom] = useState<Room | null>(null);
  const router = useRouter();

  const handleViewDetail = (room: Room) => {
    router.push(`/managementRoom/${room.id}`);
  };

  const handleEdit = (room: Room) => {
    router.push(`/managementRoom/${room.id}/edit`);
  };

  const handleCreate = () => {
    router.push(`/managementRoom/create`);
  };
  const handleModalOk = () => {
    if (fixPrice && fixPrice > 0) {
      if (selectedRoom) {
        addRoomToPromotional(selectedRoom.roomId, fixPrice);
        setVisibleModal(false);
      }
    } else {
      notificationService.room.promotional.invalidPrice();
    }
  };
  const handleFixPriceChange = (value: string) => {
    setFixPrice(Number(value));
  };

  const handleDelete = async (room: Room) => {
    notificationService.room.deleteConfirm(room.name, async () => {
      setLoadingDelete(true);
      try {
        await roomService.deleteRoom(room.id);
        notificationService.room.deleteSuccess(room.name);
        setData((prevData) => prevData.filter((item) => item.id !== room.id));
      } catch (error) {
        console.error("Error deleting room:", error);
        notificationService.room.deleteError();
      } finally {
        setLoadingDelete(false);
      }
    });
  };
  const handleDeletePromotional = async (room: Room) => {
    notificationService.room.promotional.deleteConfirm(room.name, async () => {
      setLoadingDelete(true);
      try {
        await roomService.deletePromotional(room.roomId);
        notificationService.room.promotional.deleteSuccess(room.name);
        handleReload();
      } catch (error) {
        console.error("Error deleting promotional:", error);
        notificationService.room.promotional.deleteError();
      } finally {
        setLoadingDelete(false);
      }
    });
  };
  const addRoomToPromotional = async (roomId: string, fixPrice: number) => {
    try {
      const response = await roomService.addPromotional(roomId, fixPrice);

      if (response.data.message === "Success") {
        notificationService.room.promotional.addSuccess(
          data.find((room) => room.roomId === roomId)?.name || roomId
        );
        handleReload();
      } else {
        notificationService.room.promotional.addError();
      }
    } catch (error) {
      console.error("Error:", error);
      notificationService.room.promotional.addError();
    }
  };

  const fetchRoomData = async (page: number = 1) => {
    try {
      const response = await axios.get(
        `${process.env.NEXT_PUBLIC_API_URL_MARKETING}/post/all`,
        { params: { page, size } }
      );
      const responseData = response.data;
      console.log("Dữ liệu từ API:", response.data); // Kiểm tra dữ liệu trả về

      if (responseData.responseCode === 101000 && responseData.data) {
        setTotalItems(responseData.data.totalItems); // Set total item count for pagination
        setPageSize(responseData.data.pageSize);

        const fetchedData = responseData.data.data.map(
          (item: RoomFinal, index: number) => ({
            key: `${index + (page - 1) * size}`,
            id: item.id,
            roomId: item.roomId,
            status: item.status,
            label: item.roomInfo.type,
            createdAt: item.created,
            image: item.roomInfo.postImages?.[0]?.urlImagePost, // Get the first image URL if available
            fixPrice: item.fixPrice,
            basePrice: item.pricingDetails.basePrice,
            name: item.roomInfo.name,
          })
        );

        setData(fetchedData);

        setTotalItems(responseData.data.totalElements);
      } else {
        console.error("Unexpected response format", responseData);
      }
    } catch (error) {
      console.error("Error fetching room data:", error);
    }
  };
  const handleReload = () => {
    fetchRoomData(currentPage);
  };

  useEffect(() => {
    fetchRoomData(currentPage);
  }, [currentPage]);
  const handleAction = (action: string, room: Room) => {
    switch (action) {
      case "addPromotional":
        setSelectedRoom(room);
        setFixPrice(null);
        setVisibleModal(true);
        break;
      case "deletePromotional":
        handleDeletePromotional(room);
        break;
      default:
        break;
    }
  };

  const handleSearch = (searchParams: {
    title: string;
    status: string;
    dateRange: [string, string] | null;
  }) => {
    console.log("Tham số tìm kiếm:", searchParams);
    const { title, status, dateRange } = searchParams;

    // Chuyển đổi dateRange về định dạng ngày (không có giờ)
    const normalizedDateRange = dateRange
      ? [
          // Chuyển ngày bắt đầu về dạng 'YYYY-MM-DD' (chỉ lấy phần ngày)
          new Date(dateRange[0]).toISOString().split("T")[0],
          // Chuyển ngày kết thúc về dạng 'YYYY-MM-DD' (chỉ lấy phần ngày)
          new Date(dateRange[1]).toISOString().split("T")[0],
        ]
      : null;

    // Lọc dữ liệu
    const filteredData = data.filter((item) => {
      // Kiểm tra tiêu đề
      const matchesTitle = title
        ? item.name.toLowerCase().includes(title.toLowerCase()) ||
          item.id.toString().includes(title) ||
          item.roomId.toString().includes(title) ||
          item.status.toLowerCase().includes(title.toLowerCase()) ||
          item.label.toLowerCase().includes(title.toLowerCase()) ||
          item.createdAt.includes(title) ||
          item.fixPrice?.toString().includes(title) ||
          item.basePrice.toString().includes(title)
        : true;

      // Kiểm tra trạng thái
      const matchesStatus =
        status && status !== "Trạng Thái"
          ? item.status.toLowerCase() === status.toLowerCase()
          : true;

      // Kiểm tra ngày tháng
      const matchesDateRange =
        normalizedDateRange && normalizedDateRange.length === 2
          ? // So sánh ngày (chỉ phần ngày, không có giờ)
            item.createdAt.split("T")[0] >= normalizedDateRange[0] &&
            item.createdAt.split("T")[0] <= normalizedDateRange[1]
          : true;

      return matchesTitle && matchesStatus && matchesDateRange;
    });

    console.log("Kết quả tìm kiếm:", filteredData); // Kiểm tra kết quả lọc

    // Cập nhật dữ liệu sau khi lọc
    setData(filteredData);
  };

  const handleReset = () => {
    fetchRoomData(currentPage); // Đặt lại dữ liệu khi reset
  };
  return (
    <div
      style={{ padding: "24px", backgroundColor: "#fff", borderRadius: "8px" }}
    >
      <SearchBar onSearch={handleSearch} onReset={handleReset} />
      <ActionBar
        onCreate={handleCreate}
        onReload={() => fetchRoomData(currentPage)}
      />
      <PriceModal
        visible={visibleModal}
        fixPrice={fixPrice}
        onOk={handleModalOk}
        onCancel={() => setVisibleModal(false)}
        onChange={handleFixPriceChange}
      />
      <RoomTable
        data={data}
        currentPage={currentPage}
        totalItems={totalItems}
        pageSize={size}
        onPageChange={setCurrentPage}
        handleEdit={handleEdit}
        handleDelete={handleDelete}
        handleViewDetail={handleViewDetail}
        handleAction={handleAction}
        loadingDelete={loadingDelete}
        deleteRoomId={deleteRoomId}
      />
    </div>
  );
};
export default IssueTracker;
