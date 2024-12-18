
package com.example.payment.controller;

import com.example.payment.configuration.EnvConfig;
import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.services.UserPaymentService;
import com.example.payment.services.VNPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VNPay Controller", description = "API để quản lý VNPay Payment.")
@Slf4j
@RestController
@RequestMapping("/vnPay")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayController {
    VNPayService vnPayService;
    UserPaymentService userPaymentService; // Inject UserPaymentService để cập nhật balance
    private static final  String BASE_URL = EnvConfig.get("BASE_URL");
    private static final String SUCCESS = EnvConfig.get("SUCCESS");
    @Operation(
            summary = "Tạo đơn hàng VNPay",
            description = "API này tạo một đơn hàng mới với thông tin đơn hàng và số tiền được cung cấp, và trả về URL để thanh toán trên VNPay."
            , security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL thanh toán thành công",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Lỗi đầu vào không hợp lệ",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi trong quá trình thanh toán",
                    content = @Content)
    })
    @PostMapping("/submitOrder")
    public GenericApiResponse<String> submitOrder(
            @RequestParam("amount") int orderTotal,
            HttpServletRequest request) {
        try {
            String vnpayUrl = vnPayService.createOrder(request, orderTotal,  BASE_URL);

            return GenericApiResponse.success(vnpayUrl);
        } catch (Exception e) {
            log.error("Error creating VNPay order: ", e);
            return GenericApiResponse.error("Lỗi trong quá trình thanh toán");
        }
    }

    @Operation(
            summary = "Xử lý kết quả thanh toán từ VNPay",
            description = "API này xử lý phản hồi từ VNPay sau khi thanh toán, trả về trạng thái thanh toán."
    , security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xử lý thành công trạng thái thanh toán",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Lỗi trong quá trình xử lý phản hồi",
                    content = @Content)

    }
    )
    @GetMapping("/vnpay-payment-return")
    public GenericApiResponse<String> paymentReturn(HttpServletRequest request) {
        try {
            String transactionToken = request.getParameter("vnp_TxnRef");
            String paymentStatus = vnPayService.orderReturn(request);
            if (paymentStatus.equals(SUCCESS)) {
                userPaymentService.updateUserBalance(transactionToken);
            }
            return GenericApiResponse.success(paymentStatus);
        } catch (Exception e) {
            log.error("Error processing payment return: ", e);
            return GenericApiResponse.error("Failed to process payment return");
        }
    }
}
