package com.example.todo.application.member;

import com.example.todo.application.member.dto.MemberLoginInfo;
import com.example.todo.application.member.dto.MemberResponse;
import com.example.todo.application.member.provided.Auth;
import com.example.todo.application.member.required.MemberRepository;
import com.example.todo.application.security.MemoryMap;
import com.example.todo.application.security.TokenProvider;
import com.example.todo.domain.global.vo.Email;
import com.example.todo.domain.member.Member;
import com.example.todo.application.member.dto.MemberLoginRequest;
import com.example.todo.application.member.dto.MemberSignupRequest;
import com.example.todo.application.member.dto.TokenInfo;
import com.example.todo.application.member.dto.TokenResponse;
import com.example.todo.domain.member.exception.AuthErrorType;
import com.example.todo.domain.member.exception.AuthException;
import com.example.todo.domain.member.exception.MemberErrorType;
import com.example.todo.domain.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements Auth {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemoryMap memoryMap;

    @Override
    public MemberLoginInfo signup(MemberSignupRequest request) {
        checkDuplicateEmail(request);

        String hashedPassword = getHashedPassword(request.password());

        Member newMember = Member.create(request.name(), request.email(), hashedPassword);
        memberRepository.save(newMember);

        return login(MemberLoginRequest.of(request.email(), request.password()));
    }

    @Override
    public MemberLoginInfo login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(new Email(request.email()))
                .orElseThrow(() -> new MemberException(MemberErrorType.INVALID_EMAIL));

        verifyPassword(request.password(), member.getHashedPassword());

        TokenInfo tokenInfo = getTokenInfo(member);
        MemberResponse memberResponse = MemberResponse.from(member);

        return MemberLoginInfo.from(tokenInfo, memberResponse);
    }

    @Override
    public TokenResponse refresh(String refreshToken) {
        Long memberId = tokenProvider.parseRefreshToken(refreshToken);

        String key = "auth:refresh:" + memberId;
        String savedToken = memoryMap.getValue(key);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new AuthException(AuthErrorType.INVALID_REFRESH_TOKEN);
        }

        String newAccess  = tokenProvider.createAccessToken(memberId);
        String newRefresh = tokenProvider.createRefreshToken(memberId);

        long refreshTtl = tokenProvider.getRefreshTokenExpiration();
        memoryMap.setValue(key, newRefresh, refreshTtl);

        long accessTtl  = tokenProvider.getAccessTokenExpiration();

        return new TokenResponse(newAccess, newRefresh, accessTtl, refreshTtl);
    }

    private TokenInfo getTokenInfo(Member member) {
        String accessToken = tokenProvider.createAccessToken(member.getId());
        String refreshToken = tokenProvider.createRefreshToken(member.getId());
        String redisKey     = "auth:refresh:" + member.getId();
        memoryMap.setValue(redisKey, refreshToken, tokenProvider.getRefreshTokenExpiration());

        TokenInfo tokenInfo = TokenInfo.of(accessToken, refreshToken);
        return tokenInfo;
    }

    private String getHashedPassword(String hashedPassword) {
        return passwordEncoder.encode(hashedPassword);
    }

    private void verifyPassword(String password, String hashedPassword) {
        if (!passwordEncoder.matches(password, hashedPassword)) {
            throw new MemberException(MemberErrorType.INVALID_PASSWORD);
        }
    }

    private void checkDuplicateEmail(MemberSignupRequest request) {
        if(memberRepository.findByEmail(new Email(request.email())).isPresent()) {
            throw new MemberException(MemberErrorType.EMAIL_DUPLICATE);}
    }
}
