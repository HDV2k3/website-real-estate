import React, { useEffect } from "react";
import { Form, Input, Button, Space, Checkbox } from "antd";
import { CloseOutlined } from "@ant-design/icons";

interface RoomUtility {
  furnitureAvailability: Record<string, boolean>;
  amenitiesAvailability: Record<string, boolean>;
}

interface RoomUtilityFormProps {
  roomUtility?: RoomUtility;
}

const RoomUtilitiesSection: React.FC<RoomUtilityFormProps> = ({
  roomUtility,
}) => {
  const [form] = Form.useForm();

  // Set initial values for the form when the component mounts
  useEffect(() => {
    if (roomUtility) {
      form.setFieldsValue({
        furnitureAvailability: Object.keys(
          roomUtility.furnitureAvailability
        ).map((key) => ({
          key,
          value: roomUtility.furnitureAvailability[key],
        })),
        amenitiesAvailability: Object.keys(
          roomUtility.amenitiesAvailability
        ).map((key) => ({
          key,
          value: roomUtility.amenitiesAvailability[key],
        })),
      });
    }
  }, [roomUtility, form]);

  return (
    <>
      {/* Furniture Availability List */}
      <Form.List name="furnitureAvailability">
        {(subFields, subOpt) => (
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              rowGap: 16,
            }}
          >
            {subFields.map((subField) => (
              <Space key={subField.key} align="center">
                <Form.Item
                  name={[subField.name, "key"]}
                  noStyle
                  rules={[{ required: true, message: "Name required" }]}
                >
                  <Input
                    placeholder="Furniture name"
                    defaultValue={subField.key}
                  />
                </Form.Item>
                <Form.Item
                  name={[subField.name, "value"]}
                  valuePropName="checked"
                  noStyle
                >
                  <Checkbox>có sẵn</Checkbox>
                </Form.Item>
                <CloseOutlined
                  onClick={() => {
                    subOpt.remove(subField.name);
                  }}
                />
              </Space>
            ))}
            <Button type="dashed" onClick={() => subOpt.add()} block>
              + Thêm nội thất
            </Button>
          </div>
        )}
      </Form.List>

      {/* Amenities Availability List */}
      <Form.List name="amenitiesAvailability">
        {(subFields, subOpt) => (
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              rowGap: 16,
            }}
          >
            {subFields.map((subField) => (
              <Space key={subField.key} align="center">
                <Form.Item
                  name={[subField.name, "key"]}
                  noStyle
                  rules={[{ required: true, message: "Name required" }]}
                >
                  <Input placeholder="Amenity name" />
                </Form.Item>
                <Form.Item
                  name={[subField.name, "value"]}
                  valuePropName="checked"
                  noStyle
                >
                  <Checkbox>có sẵn</Checkbox>
                </Form.Item>
                <CloseOutlined
                  onClick={() => {
                    subOpt.remove(subField.name);
                  }}
                />
              </Space>
            ))}
            <Button type="dashed" onClick={() => subOpt.add()} block>
              + Thêm tiện nghi
            </Button>
          </div>
        )}
      </Form.List>
    </>
  );
};

export default RoomUtilitiesSection;
