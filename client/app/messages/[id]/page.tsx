// "use client";
// import React, { useEffect, useRef, useState } from "react";
// import {
//   Button,
//   Form,
//   Input,
//   List,
//   Avatar,
//   Spin,
//   Typography,
//   message,
// } from "antd";
// import {
//   CloseOutlined,
//   EyeInvisibleOutlined,
//   EyeOutlined,
//   UserOutlined,
// } from "@ant-design/icons";
// import WebSocketConnection from "../../../service/Websocket";
// import { ChatMessage } from "../../../types/ChatType";
// import axios from "axios";
// import EncryptionService from "../../../service/EncryptionService";
// import { useParams, useRouter } from "next/navigation";
// import { getChatHistory, markMessagesAsDelivered } from "@/service/ChatService";
// import { API_URL_CHATTING, API_USER } from "@/service/constants";
// const { Title } = Typography;

// interface MessageResponse {
//   data: {
//     id: number;
//     senderId: number;
//     receiverId: number;
//     messageEncryptForSender: string;
//     messageEncryptForReceiver: string;
//     messageType: string;
//     sentAt: string;
//     chatStatus: {
//       status: string;
//       deliveredAt: string;
//       readAt: string | null;
//     };
//   }[];
//   message: string;
//   responseCode: number;
// }

// const ChatDetail: React.FC = () => {
//   const [form] = Form.useForm();
//   const [webSocketService, setWebSocketService] =
//     useState<WebSocketConnection | null>(null);
//   const [chatHistory, setChatHistory] = useState<ChatMessage[]>([]);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState<string | null>(null);
//   const [newMessage, setNewMessage] = useState<string>("");
//   const [isTyping, setIsTyping] = useState<boolean>(false);
//   const chatContainerRef = useRef<HTMLDivElement>(null);
//   const [fullName, setFullName] = useState<string>("Unknown User");
//   const [showChat, setShowChat] = useState(true);
//   const router = useRouter();
//   const { id: receiverId } = useParams() as { id: string };
//   const handleGoBack = () => {
//     router.push("/messages"); // Điều hướng về trang messages mà không tải lại trang
//   };

//   const toggleChatVisibility = () => {
//     setShowChat((prevState) => !prevState); // Đảo ngược trạng thái hiển thị
//   };
//   const senderId = Number(localStorage.getItem("userId"));
//   const token = localStorage.getItem("token");
//   useEffect(() => {
//     const fetchUserDetails = async () => {
//       try {
//         const response = await axios.get(
//           `${API_USER}/users/get-by-id/${receiverId}`
//         );
//         const firstName = response.data.data.firstName;
//         // const { firstName, lastName } = response.data;
//         const lastName = response.data.data.lastName;
//         setFullName(`${firstName} ${lastName}`);
//       } catch (error) {
//         console.error("Error fetching user details:", error);
//         setFullName("Unknown User");
//       }
//     };

//     fetchUserDetails();
//   }, [receiverId]);
//   const scrollToBottom = () => {
//     if (chatContainerRef.current) {
//       setTimeout(() => {
//         chatContainerRef.current?.scrollTo({
//           top: chatContainerRef.current.scrollHeight,
//           behavior: "smooth",
//         });
//       }, 100);
//     }
//   };

//   // eslint-disable-next-line react-hooks/exhaustive-deps
//   const decryptMessage = async (chat: any): Promise<ChatMessage> => {
//     try {
//       const encryptedMessage =
//         chat.senderId === senderId
//           ? chat.messageEncryptForSender
//           : chat.messageEncryptForReceiver;
//       if (!encryptedMessage) throw new Error("No encrypted message available");

//       const decryptedContent = await EncryptionService.decryptMessage(
//         encryptedMessage,
//         senderId,
//         +receiverId
//       );
//       // console.log("debug", decryptedContent);
//       return {
//         id: chat.id,
//         senderId: chat.senderId,
//         receiverId: chat.receiverId,
//         message: decryptedContent || "No message content",
//         messageType: chat.messageType,
//         sentAt: chat.sentAt,
//         status: chat.chatStatus?.status || "SENT",
//         deliveredAt: chat.chatStatus?.deliveredAt || "",
//         readAt: chat.chatStatus?.readAt || "",
//         messageSender: fullName,
//         content: decryptedContent || "No message content",
//         urlFile: "",
//       };
//     } catch (error) {
//       console.error("Error decrypting message:", error);
//       return {
//         id: chat.id,
//         senderId: chat.senderId,
//         receiverId: chat.receiverId,
//         message: "Decryption failed",
//         messageType: chat.messageType,
//         sentAt: chat.sentAt,
//         status: chat.chatStatus?.status || "SENT",
//         deliveredAt: chat.chatStatus?.deliveredAt || "",
//         readAt: chat.chatStatus?.readAt || "",
//         messageSender: fullName,
//         content: "Decryption failed",
//         urlFile: "",
//       };
//     }
//   };

//   useEffect(() => {
//     const fetchChatHistory = async () => {
//       if (!senderId || !receiverId || !token) {
//         setError("Missing required authentication data");
//         setLoading(false);
//         return;
//       }

//       try {
//         const response = await axios.get<MessageResponse>(
//           `${API_URL_CHATTING}/api/v1/chat/history`,
//           {
//             params: { senderId, receiverId },
//             headers: { Authorization: `Bearer ${token}` },
//           }
//         );

//         if (!response.data.data) {
//           setChatHistory([]);
//           setLoading(false);
//           return;
//         }

//         const decryptedChatHistory = await Promise.all(
//           response.data.data.map(decryptMessage)
//         );

//         setChatHistory(decryptedChatHistory);
//         scrollToBottom();
//       } catch (error) {
//         console.error("Error fetching chat history:", error);
//         setError("Failed to fetch chat history");
//       } finally {
//         setLoading(false);
//       }
//     };

//     fetchChatHistory();
//   }, [senderId, receiverId, token, decryptMessage]);

//   useEffect(() => {
//     const wsService = new WebSocketConnection();
//     wsService
//       .connect()
//       .then(() => setWebSocketService(wsService))
//       .catch((error) => {
//         console.error("WebSocket connection error:", error);
//         setError("Failed to connect to chat service");
//       });

//     return () => wsService.disconnect();
//   }, []);

//   useEffect(() => {
//     if (webSocketService) {
//       const messageSubscription = webSocketService.subscribeToPrivateMessages(
//         senderId,
//         +receiverId,
//         async (encryptedMessage) => {
//           try {
//             const decryptedMessage = await decryptMessage(encryptedMessage);
//             setChatHistory((prevHistory) => [...prevHistory, decryptedMessage]);
//             scrollToBottom();
//           } catch (error) {
//             console.error("Error processing incoming message:", error);
//             message.error("Failed to process incoming message");
//           }
//         }
//       );

//       const typingSubscription = webSocketService.subscribeToTyping(
//         senderId,
//         +receiverId,
//         () => setIsTyping(true)
//       );

//       return () => {
//         messageSubscription.unsubscribe();
//         typingSubscription.unsubscribe();
//       };
//     }
//   }, [senderId, receiverId]);

//   const handleSendMessage = async () => {
//     if (!newMessage.trim()) return;

//     if (!webSocketService) {
//       console.error("WebSocketService is not initialized.");
//       return;
//     }

//     try {
//       await webSocketService.sendMessage(
//         senderId,
//         +receiverId,
//         newMessage,
//         "TEXT",
//         null
//       );
//       setNewMessage("");
//       scrollToBottom();
//       await markMessagesAsDelivered(senderId, token || "");
//       const updatedHistory = await getChatHistory(
//         senderId,
//         +receiverId,
//         token || ""
//       );
//       const decryptedHistory = await Promise.all(
//         updatedHistory.map(decryptMessage)
//       );
//       setChatHistory(decryptedHistory as ChatMessage[]);
//     } catch (error) {
//       console.error("Error sending message:", error);
//     }
//   };

//   const handleTyping = () => {
//     if (newMessage.trim() && webSocketService) {
//       webSocketService.sendTypingEvent(senderId, +receiverId);
//     }
//   };

//   return (
//     <div className="flex flex-col h-[500px] max-h-[600px] p-4 bg-gray-100 rounded-lg shadow-lg">
//       <div className="flex justify-between items-center mb-4">
//         <Title
//           level={4}
//           className="text-center"
//         >{`Chat with ${fullName}`}</Title>

//         {/* Icon buttons */}
//         <Button
//           type="default"
//           onClick={handleGoBack}
//           className="ml-2 text-sm p-0"
//           icon={<CloseOutlined />}
//         />
//         <Button
//           type="default"
//           onClick={toggleChatVisibility}
//           className="ml-2 text-sm p-0"
//           icon={showChat ? <EyeInvisibleOutlined /> : <EyeOutlined />}
//         />
//       </div>

//       {showChat && (
//         <>
//           {loading ? (
//             <div className="flex justify-center items-center h-full">
//               <Spin size="large" tip="Loading chat history..." />
//             </div>
//           ) : error ? (
//             <div className="text-center text-red-500">{error}</div>
//           ) : (
//             <>
//               <div
//                 ref={chatContainerRef}
//                 className="flex-1 overflow-y-auto p-4 space-y-4 bg-white rounded-lg shadow-md max-h-[400px]"
//               >
//                 <List
//                   dataSource={chatHistory}
//                   renderItem={(message) => (
//                     <List.Item
//                       key={message.id}
//                       className={`flex ${message.senderId === senderId ? "justify-end" : "justify-start"}`}
//                     >
//                       {/* Left side: Avatar for incoming messages */}
//                       {message.senderId !== senderId && (
//                         <Avatar
//                           icon={<UserOutlined />}
//                           className="bg-gray-400 mr-1"
//                         />
//                       )}

//                       {/* Message content */}
//                       <div className="flex flex-col items-start space-y-1">
//                         <div
//                           className={`p-3 rounded-lg max-w-xs text-sm ${message.senderId === senderId ? "bg-blue-500 text-white" : "bg-gray-200 text-gray-800"}`}
//                         >
//                           {message.message}
//                         </div>
//                         <div className="text-xs text-gray-500 self-end">
//                           {new Date(message.sentAt).toLocaleString()}
//                         </div>
//                       </div>

//                       {/* Right side: Avatar for outgoing messages */}
//                       {message.senderId === senderId && (
//                         <Avatar
//                           icon={<UserOutlined />}
//                           className="bg-blue-600 ml-1"
//                         />
//                       )}
//                     </List.Item>
//                   )}
//                 />
//               </div>

//               {isTyping && (
//                 <div className="text-sm text-gray-500 text-center">
//                   {`${fullName} is typing...`}
//                 </div>
//               )}

//               <Form form={form} className="flex items-center mt-4 space-x-2">
//                 <Input
//                   type="text"
//                   placeholder="Type a message"
//                   value={newMessage}
//                   onChange={(e) => setNewMessage(e.target.value)}
//                   onKeyDown={handleTyping}
//                   onPressEnter={handleSendMessage}
//                   className="flex-grow p-2 rounded-lg border border-gray-300 focus:outline-none focus:ring focus:border-blue-500"
//                 />
//                 <Button
//                   type="primary"
//                   onClick={handleSendMessage}
//                   className="flex-shrink-0 rounded-lg bg-blue-600 hover:bg-blue-700"
//                 >
//                   Send
//                 </Button>
//               </Form>
//             </>
//           )}
//         </>
//       )}
//     </div>
//   );
// };

// export default ChatDetail;
"use client";
import React, { useEffect, useRef, useState } from "react";
import {
  Button,
  Form,
  Input,
  List,
  Avatar,
  Spin,
  Typography,
  message,
} from "antd";
import {
  CloseOutlined,
  EyeInvisibleOutlined,
  EyeOutlined,
  UserOutlined,
} from "@ant-design/icons";
import WebSocketConnection from "../../../service/Websocket";
import { ChatMessage } from "../../../types/ChatType";
import axios from "axios";
import EncryptionService from "../../../service/EncryptionService";
import { useParams, useRouter } from "next/navigation";
import { getChatHistory, markMessagesAsDelivered } from "@/service/ChatService";

const { Title } = Typography;

interface MessageResponse {
  data: {
    id: number;
    senderId: number;
    receiverId: number;
    messageEncryptForSender: string;
    messageEncryptForReceiver: string;
    messageType: string;
    sentAt: string;
    chatStatus: {
      status: string;
      deliveredAt: string;
      readAt: string | null;
    };
  }[];
  message: string;
  responseCode: number;
}

const ChatDetail: React.FC = () => {
  const [form] = Form.useForm();
  const [webSocketService, setWebSocketService] =
    useState<WebSocketConnection | null>(null);
  const [chatHistory, setChatHistory] = useState<ChatMessage[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [newMessage, setNewMessage] = useState<string>("");
  const [isTyping, setIsTyping] = useState<boolean>(false);
  const chatContainerRef = useRef<HTMLDivElement>(null);
  const [fullName, setFullName] = useState<string>("Unknown User");
  const [showChat, setShowChat] = useState(true);
  const typingTimeoutRef = useRef<NodeJS.Timeout>();
  const lastTypingTimeRef = useRef<number>(0);
  const router = useRouter();
  const { id: receiverId } = useParams() as { id: string };

  const handleGoBack = () => {
    router.push("/messages");
  };

  const toggleChatVisibility = () => {
    setShowChat((prevState) => !prevState);
  };

  const senderId = Number(localStorage.getItem("userId"));
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const response = await axios.get(
          `${process.env.NEXT_PUBLIC_API_URL_USER}/users/get-by-id/${receiverId}`
        );
        const firstName = response.data.data.firstName;
        const lastName = response.data.data.lastName;
        setFullName(`${firstName} ${lastName}`);
      } catch (error) {
        console.error("Error fetching user details:", error);
        setFullName("Unknown User");
      }
    };

    fetchUserDetails();
  }, [receiverId]);

  const scrollToBottom = () => {
    if (chatContainerRef.current) {
      setTimeout(() => {
        chatContainerRef.current?.scrollTo({
          top: chatContainerRef.current.scrollHeight,
          behavior: "smooth",
        });
      }, 100);
    }
  };

  const decryptMessage = async (chat: any): Promise<ChatMessage> => {
    try {
      const encryptedMessage =
        chat.senderId === senderId
          ? chat.messageEncryptForSender
          : chat.messageEncryptForReceiver;
      if (!encryptedMessage) throw new Error("No encrypted message available");

      const decryptedContent = await EncryptionService.decryptMessage(
        encryptedMessage,
        senderId,
        +receiverId
      );

      return {
        id: chat.id,
        senderId: chat.senderId,
        receiverId: chat.receiverId,
        message: decryptedContent || "No message content",
        messageType: chat.messageType,
        sentAt: chat.sentAt,
        status: chat.chatStatus?.status || "SENT",
        deliveredAt: chat.chatStatus?.deliveredAt || "",
        readAt: chat.chatStatus?.readAt || "",
        messageSender: fullName,
        content: decryptedContent || "No message content",
        urlFile: "",
      };
    } catch (error) {
      console.error("Error decrypting message:", error);
      return {
        id: chat.id,
        senderId: chat.senderId,
        receiverId: chat.receiverId,
        message: "Decryption failed",
        messageType: chat.messageType,
        sentAt: chat.sentAt,
        status: chat.chatStatus?.status || "SENT",
        deliveredAt: chat.chatStatus?.deliveredAt || "",
        readAt: chat.chatStatus?.readAt || "",
        messageSender: fullName,
        content: "Decryption failed",
        urlFile: "",
      };
    }
  };

  useEffect(() => {
    const fetchChatHistory = async () => {
      if (!senderId || !receiverId || !token) {
        setError("Missing required authentication data");
        setLoading(false);
        return;
      }

      try {
        const response = await axios.get<MessageResponse>(
          `${process.env.NEXT_PUBLIC_API_URL_CHATTING}/api/v1/chat/history`,
          {
            params: { senderId, receiverId },
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        if (!response.data.data) {
          setChatHistory([]);
          setLoading(false);
          return;
        }

        const decryptedChatHistory = await Promise.all(
          response.data.data.map(decryptMessage)
        );

        setChatHistory(decryptedChatHistory);
        scrollToBottom();
      } catch (error) {
        console.error("Error fetching chat history:", error);
        setError("Failed to fetch chat history");
      } finally {
        setLoading(false);
      }
    };

    fetchChatHistory();
  }, [senderId, receiverId, token]);

  useEffect(() => {
    const wsService = new WebSocketConnection();
    wsService
      .connect()
      .then(() => setWebSocketService(wsService))
      .catch((error) => {
        console.error("WebSocket connection error:", error);
        setError("Failed to connect to chat service");
      });

    return () => wsService.disconnect();
  }, []);

  useEffect(() => {
    if (webSocketService) {
      const messageSubscription = webSocketService.subscribeToPrivateMessages(
        senderId,
        +receiverId,
        async (encryptedMessage) => {
          try {
            const decryptedMessage = await decryptMessage(encryptedMessage);
            setChatHistory((prevHistory) => [...prevHistory, decryptedMessage]);
            scrollToBottom();
          } catch (error) {
            console.error("Error processing incoming message:", error);
            message.error("Failed to process incoming message");
          }
        }
      );

      const typingSubscription = webSocketService.subscribeToTyping(
        senderId,
        +receiverId,
        () => {
          setIsTyping(true);
          // Tự động tắt typing indicator sau 3 giây
          if (typingTimeoutRef.current) {
            clearTimeout(typingTimeoutRef.current);
          }
          typingTimeoutRef.current = setTimeout(() => {
            setIsTyping(false);
          }, 3000);
        }
      );

      return () => {
        messageSubscription.unsubscribe();
        typingSubscription.unsubscribe();
        if (typingTimeoutRef.current) {
          clearTimeout(typingTimeoutRef.current);
        }
      };
    }
  }, [senderId, receiverId, webSocketService]);

  // Cleanup effect for typing timeout
  useEffect(() => {
    return () => {
      if (typingTimeoutRef.current) {
        clearTimeout(typingTimeoutRef.current);
      }
    };
  }, []);

  const handleTyping = () => {
    const now = Date.now();
    const TYPING_THROTTLE = 2000; // 2 seconds throttle

    // Only send typing event if enough time has passed since last event
    if (now - lastTypingTimeRef.current > TYPING_THROTTLE) {
      if (newMessage.trim() && webSocketService) {
        webSocketService.sendTypingEvent(senderId, +receiverId);
        lastTypingTimeRef.current = now;
      }
    }

    // Reset typing timeout
    if (typingTimeoutRef.current) {
      clearTimeout(typingTimeoutRef.current);
    }

    // Auto-reset typing state after 3 seconds of no typing
    typingTimeoutRef.current = setTimeout(() => {
      setIsTyping(false);
    }, 3000);
  };

  const handleSendMessage = async () => {
    if (!newMessage.trim()) return;

    if (!webSocketService) {
      console.error("WebSocketService is not initialized.");
      return;
    }

    try {
      await webSocketService.sendMessage(
        senderId,
        +receiverId,
        newMessage,
        "TEXT",
        null
      );
      setNewMessage("");
      setIsTyping(false); // Reset typing state after sending
      if (typingTimeoutRef.current) {
        clearTimeout(typingTimeoutRef.current);
      }
      scrollToBottom();
      await markMessagesAsDelivered(senderId, token || "");
      const updatedHistory = await getChatHistory(
        senderId,
        +receiverId,
        token || ""
      );
      const decryptedHistory = await Promise.all(
        updatedHistory.map(decryptMessage)
      );
      setChatHistory(decryptedHistory as ChatMessage[]);
    } catch (error) {
      console.log("error", error);
      // message.error("Failed to send message");
    }
  };
  return (
    <div className="flex flex-col w-full md:max-w-4xl mx-auto p-2 md:p-4 bg-gray-100 rounded-lg shadow-lg h-[883px]">
      {/* Header */}
      <div className="flex justify-between items-center mb-2 md:mb-4 px-2 md:px-4 py-2 bg-white rounded-lg shadow-sm">
        <Title level={4} className="text-base md:text-lg lg:text-xl m-0">
          {`Chat with ${fullName}`}
        </Title>

        <div className="flex gap-2">
          <Button
            type="default"
            onClick={handleGoBack}
            className="flex items-center justify-center w-8 h-8 md:w-10 md:h-10 p-0"
            icon={<CloseOutlined className="text-sm md:text-base" />}
          />
          <Button
            type="default"
            onClick={toggleChatVisibility}
            className="flex items-center justify-center w-8 h-8 md:w-10 md:h-10 p-0"
            icon={
              showChat ? (
                <EyeInvisibleOutlined className="text-sm md:text-base" />
              ) : (
                <EyeOutlined className="text-sm md:text-base" />
              )
            }
          />
        </div>
      </div>

      {showChat && (
        <>
          {loading ? (
            <div className="flex justify-center items-center flex-1">
              <Spin size="large" tip="Loading chat history..." />
            </div>
          ) : error ? (
            <div className="text-center text-red-500 p-4">{error}</div>
          ) : (
            <div className="flex flex-col flex-1 h-full">
              {/* Chat Container */}
              <div
                ref={chatContainerRef}
                className="flex-1 overflow-y-auto p-2 md:p-4 space-y-2 md:space-y-4 bg-white rounded-lg shadow-md"
              >
                <List
                  dataSource={chatHistory}
                  renderItem={(message) => (
                    <List.Item
                      key={message.id}
                      className={`flex ${
                        message.senderId === senderId
                          ? "justify-end"
                          : "justify-start"
                      } px-2`}
                    >
                      {message.senderId !== senderId && (
                        <Avatar
                          icon={<UserOutlined />}
                          className="bg-gray-400 mr-1 w-6 h-6 md:w-8 md:h-8 flex-shrink-0"
                        />
                      )}

                      <div className="flex flex-col items-start space-y-1 max-w-[75%] md:max-w-[60%]">
                        <div
                          className={`p-2 md:p-3 rounded-lg text-sm break-words ${
                            message.senderId === senderId
                              ? "bg-blue-500 text-white"
                              : "bg-gray-200 text-gray-800"
                          }`}
                        >
                          {message.message}
                        </div>
                        <div className="text-[10px] md:text-xs text-gray-500 self-end">
                          {new Date(message.sentAt).toLocaleString()}
                        </div>
                      </div>

                      {message.senderId === senderId && (
                        <Avatar
                          icon={<UserOutlined />}
                          className="bg-blue-600 ml-1 w-6 h-6 md:w-8 md:h-8 flex-shrink-0"
                        />
                      )}
                    </List.Item>
                  )}
                />
              </div>

              {/* Typing Indicator */}
              {isTyping && (
                <div className="text-xs md:text-sm text-gray-500 text-center py-1">
                  {`${fullName} is typing...`}
                </div>
              )}

              {/* Message Input */}
              <Form
                form={form}
                className="flex items-center mt-2 md:mt-4 space-x-2 p-2 bg-white rounded-lg shadow-sm"
              >
                <Input
                  type="text"
                  placeholder="Type a message"
                  value={newMessage}
                  onChange={(e) => setNewMessage(e.target.value)}
                  onKeyDown={handleTyping}
                  onPressEnter={handleSendMessage}
                  className="flex-grow p-2 text-sm md:text-base rounded-lg border border-gray-300 focus:outline-none focus:ring focus:border-blue-500"
                />
                <Button
                  type="primary"
                  onClick={handleSendMessage}
                  className="flex-shrink-0 h-8 md:h-10 px-3 md:px-4 rounded-lg bg-blue-600 hover:bg-blue-700 text-sm md:text-base"
                >
                  Send
                </Button>
              </Form>
            </div>
          )}
        </>
      )}
    </div>
  );
  // return (
  //   <div className="flex flex-col h-[500px] max-h-[600px] p-4 bg-gray-100 rounded-lg shadow-lg">
  //     <div className="flex justify-between items-center mb-4">
  //       <Title
  //         level={4}
  //         className="text-center"
  //       >{`Chat with ${fullName}`}</Title>

  //       <div className="flex gap-2">
  //         <Button
  //           type="default"
  //           onClick={handleGoBack}
  //           className="ml-2 text-sm p-0"
  //           icon={<CloseOutlined />}
  //         />
  //         <Button
  //           type="default"
  //           onClick={toggleChatVisibility}
  //           className="ml-2 text-sm p-0"
  //           icon={showChat ? <EyeInvisibleOutlined /> : <EyeOutlined />}
  //         />
  //       </div>
  //     </div>

  //     {showChat && (
  //       <>
  //         {loading ? (
  //           <div className="flex justify-center items-center h-full">
  //             <Spin size="large" tip="Loading chat history..." />
  //           </div>
  //         ) : error ? (
  //           <div className="text-center text-red-500">{error}</div>
  //         ) : (
  //           <>
  //             <div
  //               ref={chatContainerRef}
  //               className="flex-1 overflow-y-auto p-4 space-y-4 bg-white rounded-lg shadow-md max-h-[400px]"
  //             >
  //               <List
  //                 dataSource={chatHistory}
  //                 renderItem={(message) => (
  //                   <List.Item
  //                     key={message.id}
  //                     className={`flex ${message.senderId === senderId ? "justify-end" : "justify-start"}`}
  //                   >
  //                     {message.senderId !== senderId && (
  //                       <Avatar
  //                         icon={<UserOutlined />}
  //                         className="bg-gray-400 mr-1"
  //                       />
  //                     )}

  //                     <div className="flex flex-col items-start space-y-1">
  //                       <div
  //                         className={`p-3 rounded-lg max-w-xs text-sm ${message.senderId === senderId ? "bg-blue-500 text-white" : "bg-gray-200 text-gray-800"}`}
  //                       >
  //                         {message.message}
  //                       </div>
  //                       <div className="text-xs text-gray-500 self-end">
  //                         {new Date(message.sentAt).toLocaleString()}
  //                       </div>
  //                     </div>

  //                     {message.senderId === senderId && (
  //                       <Avatar
  //                         icon={<UserOutlined />}
  //                         className="bg-blue-600 ml-1"
  //                       />
  //                     )}
  //                   </List.Item>
  //                 )}
  //               />
  //             </div>

  //             {isTyping && (
  //               <div className="text-sm text-gray-500 text-center">
  //                 {`${fullName} is typing...`}
  //               </div>
  //             )}

  //             <Form form={form} className="flex items-center mt-4 space-x-2">
  //               <Input
  //                 type="text"
  //                 placeholder="Type a message"
  //                 value={newMessage}
  //                 onChange={(e) => setNewMessage(e.target.value)}
  //                 onKeyDown={handleTyping}
  //                 onPressEnter={handleSendMessage}
  //                 className="flex-grow p-2 rounded-lg border border-gray-300 focus:outline-none focus:ring focus:border-blue-500"
  //               />
  //               <Button
  //                 type="primary"
  //                 onClick={handleSendMessage}
  //                 className="flex-shrink-0 rounded-lg bg-blue-600 hover:bg-blue-700"
  //               >
  //                 Send
  //               </Button>
  //             </Form>
  //           </>
  //         )}
  //       </>
  //     )}
  //   </div>
  // );
};

export default ChatDetail;
