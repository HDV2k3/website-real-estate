import { api, apiNonToken } from "./api";

export const roomService = {
  fetchRooms: async (page: number, size: number) => {
    const response = await api.get(`/post/all`, {
      params: { page, size },
    });
    return response.data;
  },

  deleteRoom: async (roomId: string) => {
    return api.delete(`/post/${roomId}`);
  },

  deletePromotional: async (roomId: string) => {
    return api.delete(`/promotional/${roomId}`);
  },

  addPromotional: async (roomId: string, fixPrice: number) => {
    return api.post("/promotional/create", { roomId, fixPrice });
  },
  fetchFeaturedRooms: async (page: number, size: number) => {
    const response = await apiNonToken.get(
      `/featured/list-featured`,
      {
        params: { page, size },
      }
    );
    return response.data;
  },

  addFeaturedRoom: async (roomId: string) => {
    const response = await api.post(`/featured/create`, { roomId });
    return response.data;
  },

  removeFeaturedRoom: async (roomId: string) => {
    const response = await api.delete(`/featured/${roomId}`);
    return response.data;
  },
};
