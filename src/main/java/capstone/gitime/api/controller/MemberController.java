package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.MemberModifyRequestDto;
import capstone.gitime.api.exception.exception.member.NotFoundMemberException;
import capstone.gitime.api.service.dto.MemberInfoResponseDto;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.api.service.MemberService;
import capstone.gitime.domain.member.entity.Member;
import capstone.gitime.domain.member.repository.MemberRepository;
import capstone.gitime.domain.memberteam.service.MemberTeamService;
import capstone.gitime.domain.memberteam.service.dto.InvitationListRequestDto;
import capstone.gitime.domain.team.service.dto.InviteTeamRequestDto;
import capstone.gitime.domain.uploadfile.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberTeamService memberTeamService;
    private final ImageFileService imageFileService;

    // 유저 정보 가져오기
    @GetMapping
    public ResultResponseDto<MemberInfoResponseDto> memberInfo(@Token Long memberId) {
        return new ResultResponseDto<MemberInfoResponseDto>(200, "OK", Arrays.asList(memberService.getMemberInfo(memberId)));
    }

    @PatchMapping
    public ResultResponseDto<String> modifyMemberInfo(@RequestBody MemberModifyRequestDto requestDto, @Token Long memberId) {
        memberService.modifyMemberInfo(memberId, requestDto);
        return new ResultResponseDto<>(200, "OK", Collections.emptyList());
    }

    @PostMapping("/profile-img")
    public ResultResponseDto<String> updateProfileImg(@RequestParam("imageFile") MultipartFile multipartFile,
                                                      @Token Long memberId) throws IOException {

        memberService.updateProfileImg(memberId, imageFileService.preStoreProfileImageFile(multipartFile, memberId));
        return new ResultResponseDto<>(200, "프로필 이미지 업데이트가 성공적으로 완료되었습니다.", Collections.emptyList());
    }

    @GetMapping("/invite/accept/{acceptCode}")
    public ResultResponseDto<String> acceptInviteMemberToTeam(@Token Long memberId, @PathVariable("acceptCode") String acceptCode) {

        memberTeamService.modifyInviteTeamRequest(acceptCode, memberId);

        return new ResultResponseDto<>(200, "OK!", Collections.emptyList());
    }

    @GetMapping("/invite")
    public ResultResponseDto<InvitationListRequestDto> getAllInvitationToTeam(@Token Long memberId) {

        return new ResultResponseDto<>(200, "OK!", memberTeamService.getAllInvitationToTeam(memberId));

    }
}
