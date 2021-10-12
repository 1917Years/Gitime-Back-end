package capstone.gitime.api.controller;

import capstone.gitime.api.common.annotation.Token;
import capstone.gitime.api.controller.dto.MemberModifyRequestDto;
import capstone.gitime.api.service.dto.MemberInfoResponseDto;
import capstone.gitime.api.controller.dto.ResultResponseDto;
import capstone.gitime.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

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
}
