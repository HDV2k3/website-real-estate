// "use client";
// import React from "react";
// import { InputNumber, Input, Button, Form, Space } from "antd";
// import { CloseOutlined } from "@ant-design/icons";

// interface PricingDetails {
//   basePrice: number;
//   electricityCost: number;
//   waterCost: number;
//   additionalFees: { type: string; amount: number }[];
// }

// interface PricingDetailsFormProps {
//   pricingDetails?: PricingDetails;
// }

// const PricingDetailsForm: React.FC<PricingDetailsFormProps> = ({}) => {
//   return (
//     <>
//       {/* Base Price */}
//       <Form.Item
//         label="Base Price"
//         name={["pricingDetails", "basePrice"]}
//         rules={[{ required: true, message: "Base price is required" }]}
//       >
//         <InputNumber
//           min={0}
//           style={{ width: "100%" }}
//           placeholder="Enter base price"
//         />
//       </Form.Item>

//       {/* Electricity Cost */}
//       <Form.Item
//         label="Electricity Cost"
//         name={["pricingDetails", "electricityCost"]}
//         rules={[{ required: true, message: "Electricity cost is required" }]}
//       >
//         <InputNumber
//           min={0}
//           style={{ width: "100%" }}
//           placeholder="Enter electricity cost"
//         />
//       </Form.Item>

//       {/* Water Cost */}
//       <Form.Item
//         label="Water Cost"
//         name={["pricingDetails", "waterCost"]}
//         rules={[{ required: true, message: "Water cost is required" }]}
//       >
//         <InputNumber
//           min={0}
//           style={{ width: "100%" }}
//           placeholder="Enter water cost"
//         />
//       </Form.Item>

//       {/* Additional Fees */}
//       <Form.List name={["pricingDetails", "additionalFees"]}>
//         {(fields, { add, remove }) => (
//           <div style={{ display: "flex", flexDirection: "column", rowGap: 16 }}>
//             {fields.map((field) => (
//               <Space key={field.key} align="center">
//                 <Form.Item
//                   name={[field.name, "type"]}
//                   noStyle
//                   rules={[{ required: true, message: "Type is required" }]}
//                 >
//                   <Input placeholder="Fee type" />
//                 </Form.Item>
//                 <Form.Item
//                   name={[field.name, "amount"]}
//                   noStyle
//                   rules={[{ required: true, message: "Amount is required" }]}
//                 >
//                   <InputNumber
//                     min={0}
//                     placeholder="Amount"
//                     style={{ width: "100%" }}
//                   />
//                 </Form.Item>
//                 <CloseOutlined
//                   onClick={() => remove(field.name)}
//                   style={{ color: "red", cursor: "pointer" }}
//                 />
//               </Space>
//             ))}
//             <Button type="dashed" onClick={() => add()} block>
//               + Add Additional Fee
//             </Button>
//           </div>
//         )}
//       </Form.List>
//     </>
//   );
// };

// export default PricingDetailsForm;
"use client";
import React from "react";
import { InputNumber, Input, Button, Form, Space } from "antd";
import { CloseOutlined } from "@ant-design/icons";

interface PricingDetails {
  basePrice: number;
  electricityCost: number;
  waterCost: number;
  additionalFees: { type: string; amount: number }[];
}

interface PricingDetailsFormProps {
  pricingDetails?: PricingDetails;
}

const PricingDetailsForm: React.FC<PricingDetailsFormProps> = ({}) => {
  return (
    <>
      {/* Base Price */}
      <Form.Item
        label="Giá Cơ Bản"
        name={["pricingDetails", "basePrice"]}
        rules={[{ required: true, message: "Giá cơ bản là bắt buộc" }]}
      >
        <InputNumber
          min={0}
          style={{ width: "100%" }}
          placeholder="Nhập giá cơ bản"
        />
      </Form.Item>

      {/* Electricity Cost */}
      <Form.Item
        label="Chi Phí Điện"
        name={["pricingDetails", "electricityCost"]}
        rules={[{ required: true, message: "Chi phí điện là bắt buộc" }]}
      >
        <InputNumber
          min={0}
          style={{ width: "100%" }}
          placeholder="Nhập chi phí điện"
        />
      </Form.Item>

      {/* Water Cost */}
      <Form.Item
        label="Chi Phí Nước"
        name={["pricingDetails", "waterCost"]}
        rules={[{ required: true, message: "Chi phí nước là bắt buộc" }]}
      >
        <InputNumber
          min={0}
          style={{ width: "100%" }}
          placeholder="Nhập chi phí nước"
        />
      </Form.Item>

      {/* Additional Fees */}
      <Form.List name={["pricingDetails", "additionalFees"]}>
        {(fields, { add, remove }) => (
          <div style={{ display: "flex", flexDirection: "column", rowGap: 16 }}>
            {fields.map((field) => (
              <Space key={field.key} align="center">
                <Form.Item
                  name={[field.name, "type"]}
                  noStyle
                  rules={[{ required: true, message: "Loại phí là bắt buộc" }]}
                >
                  <Input placeholder="Loại phí" />
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
    </>
  );
};

export default PricingDetailsForm;
