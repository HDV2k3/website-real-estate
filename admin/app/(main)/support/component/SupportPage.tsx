import React, { useState } from "react";
import { Button, Input, Modal, Select, Card } from "antd";
import { MessageCircle, Search, Plus } from "lucide-react";
import { SupportTicket } from "@/types/message";

// Define the priority and status colors for styling
interface TicketModalProps {
  isOpen: boolean;
  onClose: () => void;
  ticket: SupportTicket | null;
  onSubmit: (message: string) => void;
}
const priorityColors = {
  low: "bg-blue-100 text-blue-800",
  medium: "bg-yellow-100 text-yellow-800",
  high: "bg-red-100 text-red-800",
};

const statusColors = {
  active: "bg-green-100 text-green-800",
  pending: "bg-yellow-100 text-yellow-800",
  resolved: "bg-gray-100 text-gray-800",
};

const TicketModal: React.FC<TicketModalProps> = ({
  isOpen,
  onClose,
  ticket,
  onSubmit,
}) => {
  const [message, setMessage] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(message);
    setMessage("");
  };

  if (!isOpen || !ticket) return null;

  return (
    <Modal
      visible={isOpen}
      onCancel={onClose}
      footer={null}
      title={ticket.title}
      width={800}
    >
      <div className="space-y-4">
        {ticket.messages.map((message) => (
          <div
            key={message.id}
            className={`flex ${
              message.isStaff ? "justify-end" : "justify-start"
            }`}
          >
            <div
              className={`max-w-[70%] p-3 rounded-lg ${
                message.isStaff
                  ? "bg-blue-500 text-white"
                  : "bg-gray-100 text-gray-900"
              }`}
            >
              <p>{message.content}</p>
              <p className="text-xs mt-1 opacity-70">{message.createdAt}</p>
            </div>
          </div>
        ))}
      </div>
      <form onSubmit={handleSubmit} className="flex gap-2 mt-4">
        <Input
          type="text"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Type your message..."
          className="flex-1"
        />
        <Button type="primary" htmlType="submit">
          Send
        </Button>
      </form>
    </Modal>
  );
};

const SupportPage = () => {
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedStatus, setSelectedStatus] = useState<string>("all");
  const [selectedPriority, setSelectedPriority] = useState<string>("all");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedTicket, setSelectedTicket] = useState<SupportTicket | null>(
    null
  );

  const tickets: SupportTicket[] = [
    {
      id: "1",
      userId: "1",
      title: "Property Listing Issue",
      description: "Unable to upload property images",
      status: "active",
      priority: "high",
      category: "Technical",
      createdAt: "2024-03-14T10:00:00Z",
      messages: [
        {
          id: "1",
          userId: "1",
          content: "I cannot upload any images to my property listing.",
          createdAt: "2024-03-14T10:00:00Z",
          isStaff: false,
        },
        {
          id: "2",
          userId: "2",
          content:
            "Could you please provide more details about the error message you're seeing?",
          createdAt: "2024-03-14T10:05:00Z",
          isStaff: true,
        },
      ],
    },
    // Add more sample tickets as needed
  ];

  const handleTicketClick = (ticket: SupportTicket) => {
    setSelectedTicket(ticket);
    setIsModalOpen(true);
  };

  const handleSubmitMessage = (message: string) => {
    console.log("Sending message:", message);
    // Add logic to submit message
  };

  const filteredTickets = tickets.filter((ticket) => {
    const matchesSearch =
      ticket.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      ticket.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus =
      selectedStatus === "all" || ticket.status === selectedStatus;
    const matchesPriority =
      selectedPriority === "all" || ticket.priority === selectedPriority;
    return matchesSearch && matchesStatus && matchesPriority;
  });

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8 flex justify-between items-center">
          <h1 className="text-2xl font-bold">Support Tickets</h1>
          <Button
            type="primary"
            icon={<Plus />}
            onClick={() => {}}
            className="flex items-center gap-2"
          >
            New Ticket
          </Button>
        </div>

        {/* Filters */}
        <div className="mb-6 grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
            <Input
              placeholder="Search tickets..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border rounded-lg focus:outline-none"
            />
          </div>
          <Select
            value={selectedStatus}
            onChange={(value) => setSelectedStatus(value)}
            className="w-full"
          >
            <Select.Option value="all">All Status</Select.Option>
            <Select.Option value="active">Active</Select.Option>
            <Select.Option value="pending">Pending</Select.Option>
            <Select.Option value="resolved">Resolved</Select.Option>
          </Select>
          <Select
            value={selectedPriority}
            onChange={(value) => setSelectedPriority(value)}
            className="w-full"
          >
            <Select.Option value="all">All Priority</Select.Option>
            <Select.Option value="low">Low</Select.Option>
            <Select.Option value="medium">Medium</Select.Option>
            <Select.Option value="high">High</Select.Option>
          </Select>
        </div>

        {/* Tickets List */}
        <div className="grid grid-cols-1 gap-4">
          {filteredTickets.map((ticket) => (
            <Card
              key={ticket.id}
              onClick={() => handleTicketClick(ticket)}
              className="cursor-pointer"
              hoverable
            >
              <div className="flex justify-between items-start">
                <div>
                  <h3 className="font-semibold">{ticket.title}</h3>
                  <p className="text-sm text-gray-500 mt-1">
                    {ticket.description}
                  </p>
                </div>
                <div className="flex gap-2">
                  <span
                    className={`px-2 py-1 rounded-full text-xs ${
                      priorityColors[ticket.priority]
                    }`}
                  >
                    {ticket.priority}
                  </span>
                  <span
                    className={`px-2 py-1 rounded-full text-xs ${
                      statusColors[ticket.status]
                    }`}
                  >
                    {ticket.status}
                  </span>
                </div>
              </div>
              <div className="flex justify-between items-center mt-4">
                <div className="flex items-center gap-2 text-sm text-gray-500">
                  <MessageCircle className="w-4 h-4" />
                  <span>{ticket.messages.length} messages</span>
                </div>
                <span className="text-sm text-gray-500">
                  Created: {new Date(ticket.createdAt).toLocaleDateString()}
                </span>
              </div>
            </Card>
          ))}
        </div>

        {/* Ticket Modal */}
        <TicketModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          ticket={selectedTicket}
          onSubmit={handleSubmitMessage}
        />
      </div>
    </div>
  );
};

export default SupportPage;
