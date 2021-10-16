package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.RequestMember;
import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.uploadfile.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class UploadFileController {

    private final ImageFileService imageFileService;

    @GetMapping("/images/{imageFileName}")
    public Resource showImageFile(@PathVariable("imageFileName") String imageFileName) throws MalformedURLException {
        return new UrlResource("file:" + imageFileService.getStoreImageFilePath(imageFileName));
    }

    @PostMapping("/images")
    public ResultResponseDto<String> uploadImageFile(@RequestParam("imageFile") MultipartFile imageFile,
                                                     @Token Long memberId) throws IOException {
        imageFileService.storeImageFile(imageFile, memberId);

        return new ResultResponseDto<>(200, "이미지 파일이 정상적으로 업로드 되었습니다.", Collections.emptyList());
    }
}
