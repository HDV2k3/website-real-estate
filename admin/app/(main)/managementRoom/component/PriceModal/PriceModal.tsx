"use client";
import React from "react";
import { Modal, Input } from "antd";

interface PriceModalProps {
  visible: boolean;
  fixPrice: number | null;
  onOk: () => void;
  onCancel: () => void;
  onChange: (value: string) => void;
}

export const PriceModal: React.FC<PriceModalProps> = ({
  visible,
  fixPrice,
  onOk,
  onCancel,
  onChange,
}) => {
  return (
    <Modal
      title="Nhập giá khuyến mãi"
      visible={visible}
      onOk={onOk}
      onCancel={onCancel}
    >
      <Input
        type="number"
        value={fixPrice !== null ? fixPrice : ""}
        onChange={(e) => onChange(e.target.value)}
        placeholder="Nhập giá khuyến mãi"
      />
    </Modal>
  );
};
