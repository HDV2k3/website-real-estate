package com.roomfinder.marketing.controllers;
import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.HospitalRoomRequest;
import com.roomfinder.marketing.controllers.dto.response.HostpitalRoomResponse;
import com.roomfinder.marketing.controllers.dto.response.PostImageResponse;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.facade.HospitalRoomFacade;
import com.roomfinder.marketing.facade.MediaFacade;
import com.roomfinder.marketing.repositories.entities.PostImage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Tag(name = "Hospital Room Controller", description = "API create and getAll room near hospital.")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/hospital-room")
public class HospitalRoomController {

    HospitalRoomFacade hospitalRoomFacade;
    MediaFacade mediaFacade;

    @PostMapping("/create")
    public GenericApiResponse<HostpitalRoomResponse> create(@RequestBody HospitalRoomRequest request) {
        var result = hospitalRoomFacade.createHospitalRoom(request);
        return GenericApiResponse.success(result);
    }

    @GetMapping("/all")
    public GenericApiResponse<PageResponse<HostpitalRoomResponse>> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var result = hospitalRoomFacade.getAllHospitalRoom(page, size);
        return GenericApiResponse.success(result);
    }

    @PostMapping("/upload-images")
    public GenericApiResponse<Set<PostImageResponse>> uploadImage(@RequestParam String id, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        Set<PostImage> uploadedImages = mediaFacade.uploadImagesHospitalRoom(id, files);
        Set<PostImageResponse> responseImages = uploadedImages.stream()
                .map(image -> new PostImageResponse(image.getName(), image.getType(), image.getUrlImagePost()))
                .collect(Collectors.toSet());
        return GenericApiResponse.success(responseImages);
    }
}
