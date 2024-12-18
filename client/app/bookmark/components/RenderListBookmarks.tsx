'use client';
import { getFavoriteBookmarkAction } from "@/service/actions/FavoriteAction";
import { useState } from "react";
import RenderItemBookmark from "./RenderItemBookmark";
import React from "react";

type Props = {
    data: any;
    page?: number;
    size?: number;
}

export default function RenderListBookmarks({ data, page, size = 5 }: Props) {
    const [dataList, setDataList] = useState<any>(data);
    const [isPage, setIsPage] = useState<number>(page || 1);
    const [hasMore, setHasMore] = useState<boolean>(true);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const fetchData = async () => {
        const newPage = isPage + 1;
        const data = await getFavoriteBookmarkAction(newPage, size);
        if (data) {
            const dataRooms = data?.data;
            setDataList((prev: any) => [
                ...prev,
                ...dataRooms,
            ])
            setIsPage(newPage);
        }
    }
    const loadMore = () => {
        fetchData();
    };

    return (
        <div className="max-h-[810px]">
            <div className="h-[30px]">
                <span className="text-lg font-semibold">Tin đăng đã lưu ({dataList.length} / 100)</span>
            </div>
            <div className="max-h-[750px] overflow-y-scroll">
                {dataList.length > 0 ? (
                    <div className="space-y-4">
                        {dataList.map((item: any, index: number) => (
                            <React.Fragment key={index}>
                                <RenderItemBookmark data={item} />
                            </React.Fragment>
                        ))}
                    </div>
                ) : (
                    <p className="text-center text-gray-500">No data</p>
                )}
            </div>
            {dataList.length > 0 &&
                <div className="h-[20px]">
                    <button
                        onClick={loadMore}
                        className="mt-4 px-6 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 disabled:bg-gray-400"
                        disabled={isLoading}
                    >
                        {isLoading ? "Loading..." : "Load More"}
                    </button>
                </div>
            }
        </div>
    );
}
