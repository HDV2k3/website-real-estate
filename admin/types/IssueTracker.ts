export interface Room {
    key: string;
    id: string;
    roomId: string;
    name: string;
    status: string;
    label: string;
    createdAt: string;
    image?: string;
    basePrice: number;
    fixPrice?: number;
    isFeatured: boolean;
  }