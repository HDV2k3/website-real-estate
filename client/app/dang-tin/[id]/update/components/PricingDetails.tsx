// components/RoomListingForm/RoomDetailsSection.tsx
import React from "react";
import { Form, Input, InputNumber, Space, Button } from "antd";
import { CloseOutlined } from "@ant-design/icons";
interface PricingDetails {
  basePrice: number;
  electricityCost: number;
  waterCost: number;
  additionalFees: { type: string; amount: number }[];
  fixPrice: number;
}

interface PricingDetailsFormProps {
  pricingDetails?: PricingDetails;
}
const RoomPricingSectionUpdate: React.FC<PricingDetailsFormProps> = ({
  pricingDetails,
}) => {
  return (
    <div className="bg-gray-100 p-6 rounded-lg mt-6">
      <h3 className="text-xl font-semibold mb-4">Pricing</h3>
      <div className="grid md:grid-cols-4 gap-5">
        <Form.Item
          name={["pricingDetails", "basePrice"]}
          label="Giá tiền cơ bản"
          rules={[{ required: true, type: "number", min: 0 }]}
        >
          <InputNumber
            className="w-full"
            formatter={(value) =>
              `₫ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",")
            }
            defaultValue={pricingDetails?.basePrice}
          />
        </Form.Item>
        <Form.Item
          name={["pricingDetails", "electricityCost"]}
          label="Giá tiền điện"
        >
          <InputNumber
            className="w-full"
            formatter={(value) =>
              `₫ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",")
            }
            defaultValue={pricingDetails?.electricityCost}
          />
        </Form.Item>
        <Form.Item name={["pricingDetails", "waterCost"]} label="Giá tiền nước">
          <InputNumber
            className="w-full"
            formatter={(value) =>
              `₫ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",")
            }
            defaultValue={pricingDetails?.waterCost}
          />
        </Form.Item>
        <Form.List name={["pricingDetails", "additionalFees"]}>
          {(fields, { add, remove }) => (
            <div
              style={{
                display: "flex",
                flexDirection: "column",
                rowGap: 16,
              }}
            >
              {fields.map((field) => (
                <Space key={field.key} align="center">
                  <Form.Item
                    name={[field.name, "type"]}
                    noStyle
                    rules={[
                      { required: true, message: "Loại phí là bắt buộc" },
                    ]}
                  >
                    <Input
                      defaultValue={
                        pricingDetails?.additionalFees.find(
                          (fee) => fee.type === field.name.toString()
                        )?.type
                      }
                    />
                  </Form.Item>
                  <Form.Item
                    name={[field.name, "amount"]}
                    noStyle
                    rules={[{ required: true, message: "Số tiền là bắt buộc" }]}
                  >
                    <InputNumber
                      min={0}
                      placeholder="Số tiền"
                      style={{ width: "100%" }}
                      defaultValue={
                        pricingDetails?.additionalFees.find(
                          (fee) => fee.type === field.name.toString()
                        )?.amount
                      }
                    />
                  </Form.Item>
                  <CloseOutlined
                    onClick={() => remove(field.name)}
                    style={{ color: "red", cursor: "pointer" }}
                  />
                </Space>
              ))}
              <Button type="dashed" onClick={() => add()} block>
                + Thêm Phí Phát Sinh
              </Button>
            </div>
          )}
        </Form.List>
      </div>
    </div>
  );
};

export default RoomPricingSectionUpdate;
