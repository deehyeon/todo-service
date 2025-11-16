package com.example.todo.application.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberLoginRequest(
        @Schema(description = "이메일", example = "test@test.com")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @Schema(description = "비밀번호", example = "test1234!")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+|\\-=\\[\\]{};:',.<>/?])[A-Za-z\\d!@#$%^&*()_+|\\-=\\[\\]{};:',.<>/?]{8,20}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다."
        )
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
    public static MemberLoginRequest of(String email, String password) {
        return new MemberLoginRequest(email, password);
    }
}
