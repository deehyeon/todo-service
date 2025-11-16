package com.example.todo.adapter.member;

import com.example.todo.adapter.member.dto.MemberLoginInfo;
import com.example.todo.adapter.member.dto.RefreshTokenRequest;
import com.example.todo.application.member.provided.Auth;
import com.example.todo.domain.global.apiPayload.response.ApiResponse;
import com.example.todo.domain.member.dto.MemberLoginRequest;
import com.example.todo.domain.member.dto.MemberSignupRequest;
import com.example.todo.domain.member.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "AUTH", description = "회원가입/로그인 관련 API")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final Auth auth;

    @Operation(summary = "회원가입", description = """
    ## 회원 가입을 수행합니다.
    - 이메일, 비밀번호 등의 정보를 입력하여 회원가입합니다.
    - 회원가입 완료 시 로그인도 완료됩니다.
    """)
    @PostMapping("/signup")
    public ApiResponse<MemberLoginInfo> signup(@RequestBody MemberSignupRequest request) {
        MemberLoginInfo loginInfo = auth.signup(request);
        return ApiResponse.success(loginInfo);
    }

    @Operation(summary = "로그인", description = """
    ## 로그인을 수행합니다.
    - 이메일, 비밀번호를 입력하여 로그인합니다.
   """)
    @PostMapping("/login")
    public  ApiResponse<MemberLoginInfo> login(@RequestBody MemberLoginRequest request) {
        MemberLoginInfo loginInfo = auth.login(request);
        return ApiResponse.success(loginInfo);
    }

    @Operation(
            summary = "토큰 갱신",
            description = """
        ## 리프레시 토큰을 사용해 새로운 Access/Refresh 토큰을 발급합니다.
        - 입력: { "refreshToken": "기존_리프레시_토큰" }
        - 반환: accessToken, refreshToken, accessTokenExpiresIn, refreshTokenExpiresIn
        """
    )
    @PostMapping("/refresh")
    public ApiResponse<TokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        TokenResponse tokenResponse = auth.refresh(request.refreshToken());
        return ApiResponse.success(tokenResponse);
    }
}
