package capstone.gitime.domain.uploadfile.service;

import capstone.gitime.api.exception.exception.member.NotFoundMemberException;
import capstone.gitime.domain.uploadfile.entity.UploadFile;
import capstone.gitime.domain.uploadfile.repository.UploadFileRepository;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageFileService {

    private final UploadFileRepository uploadFileRepository;
    private final MemberRepository memberRepository;

    private String fileDir;

    @Transactional
    public String storeImageFile(MultipartFile multipartFile, Long memberId) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException());

        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);

        // 로컬에 파일 저장
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        // DB 저장
        UploadFile newFile = UploadFile.createFileEntity()
                .storeFileName(storeFileName)
                .uploadFileName(originalFileName)
                .member(findMember)
                .build();

        uploadFileRepository.save(newFile);

        return storeFileName;

    }

    @Transactional
    public String preStoreProfileImageFile(MultipartFile multipartFile,Long memberId) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException());
        String storeUploadName = findMember.getProfileImgName();

        if (!StringUtils.hasText(storeUploadName)) {
            return storeImageFile(multipartFile, memberId);
        }

        UploadFile uploadFile = uploadFileRepository.findByStoreFileName(storeUploadName)
                .orElseThrow(() -> new RuntimeException());

        uploadFileRepository.delete(uploadFile);

        new File(getFullPath(storeUploadName)).delete();

        return storeImageFile(multipartFile, memberId);
    }

    public String getStoreImageFilePath(String storeFileName) {
        return getFullPath(storeFileName);
    }

    private String createStoreFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString(); // 랜덤 UUID 생성
        int pos = originalFileName.lastIndexOf("."); // 파일 확장자 추출 index
        String ext = originalFileName.substring(pos + 1); // 파일 확장자
        return uuid + "." + ext;
    }

    private String getFullPath(String storeFileName) {
        return fileDir + storeFileName;
    }
}
