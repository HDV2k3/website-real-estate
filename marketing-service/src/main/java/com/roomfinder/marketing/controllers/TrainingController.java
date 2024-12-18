package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.GenericResponseAI;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.TrainingFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Training Controller", description = "API training chatbot.")
@Slf4j
@RestController
@RequestMapping("/train")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrainingController {
    TrainingFacade trainingFacade;

    @Operation(
            summary = "Lấy danh sách tên các loại phòng",
            description = "API này trả về thông tin tất cả các loại phòng hiện có. Thông tin được sử dụng để huấn luyện chatbot nhận biết các tên phòng."
    )
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = GenericApiResponse.class)))
    @GetMapping("/roomInfoName")
    public GenericApiResponse<GenericResponseAI> getRoomIntents() {
        var result = trainingFacade.getRoomIntents();
        return  GenericApiResponse.success(result);
    }

    @Operation(
            summary = "Lấy danh sách địa chỉ của các phòng",
            description = "API này cung cấp địa chỉ tương ứng với từng loại phòng. Dữ liệu được dùng để huấn luyện chatbot về thông tin địa chỉ."
    )
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = GenericApiResponse.class)))
    @GetMapping("/address")
    public GenericApiResponse<GenericResponseAI> getRoomIntentsWithAddress() {
        var result = trainingFacade.getRoomIntentsWithAddress();
        return GenericApiResponse.success(result);
    }

    @Operation(
            summary = "Lấy trạng thái hiện tại của các phòng",
            description = "API trả về thông tin về trạng thái của các phòng (ví dụ: còn trống hay đã thuê)."
    )
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = GenericApiResponse.class)))
    @GetMapping("/status")
    public GenericApiResponse<GenericResponseAI> getRoomIntentsStatus() {
        var result = trainingFacade.getRoomIntentsStatus();
        return GenericApiResponse.success(result);
    }

    @Operation(
            summary = "Lấy diện tích của các phòng",
            description = "API cung cấp thông tin về chiều dài, chiều rộng và tổng diện tích của các phòng để huấn luyện chatbot về kích thước phòng."
    )
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = GenericApiResponse.class)))
    @GetMapping("/area")
    public GenericApiResponse<GenericResponseAI> getRoomIntentsTotalArea() {
        var result = trainingFacade.getRoomIntentsTotalArea();
        return GenericApiResponse.success(result);
    }

    @Operation(
            summary = "Lấy thông tin của chủ phòng",
            description = "API cung cấp các thông tin cơ bản về chủ phòng (người đại diện, số điện thoại) để chatbot có thể cung cấp thông tin này cho người dùng."
    )
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = GenericApiResponse.class)))
    @GetMapping("/infoOwner")
    public GenericApiResponse<GenericResponseAI> getRoomIntentsInfoUser() {
        var result = trainingFacade.getRoomIntentsInfoUser();
        return  GenericApiResponse.success(result);
    }

    @Operation(
            summary = "Lấy thông tin tiện ích của các phòng",
            description = "API trả về danh sách tiện ích có trong từng phòng, bao gồm nội thất và các tiện nghi khác."
    )
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = GenericApiResponse.class)))
    @GetMapping("/utility")
    public GenericApiResponse<GenericResponseAI> getRoomIntentsUtility() {
        var result = trainingFacade.getRoomIntentsUtility();
        return  GenericApiResponse.success(result);
    }

    @Operation(
            summary = "Lấy thông tin chi tiết về giá cả của các phòng",
            description = "API cung cấp các loại phí liên quan đến từng phòng, bao gồm giá cơ bản, phí điện, nước và các khoản phí bổ sung khác."
    )
    @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = GenericApiResponse.class)))
    @GetMapping("/pricingDetails")
    public GenericApiResponse<GenericResponseAI> getRoomIntentsPricingDetails() {
        var result = trainingFacade.getRoomIntentsPricingDetails();
        return  GenericApiResponse.success(result);
    }
}
