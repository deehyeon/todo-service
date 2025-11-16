package com.example.todo.domain.todo.dto;

import com.example.todo.domain.member.Member;
import com.example.todo.domain.todo.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record TodoCreateRequest(
        @Schema(description = "태그 목록", example = "[\"음악\", \"세계\"]", nullable = true)
        @Size(max = 10, message = "태그는 최대 10개까지 가능합니다.")
        List<@Size(max = 30, message = "각 태그는 최대 30자까지 가능합니다.") String> tags,

        @Schema(description = "종료일 (YYYY-MM-DD 형식)", example = "2026-10-07")
        @NotNull(message = "종료일은 필수입니다.")
        LocalDate endAt,

        @Schema(description = "할 일 내용", example = "음악 듣기")
        @NotBlank(message = "할 일은 필수로 작성해야 합니다.")
        @Size(max = 100, message = "내용은 최대 100자까지 가능합니다.")
        String content

) {}
