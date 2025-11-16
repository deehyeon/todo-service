package com.example.todo.application.todo.dto;

import com.example.todo.domain.todo.enumerate.TodoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record TodoUpdateRequest(
        @Schema(description = "변경할 태그 목록 (null 이면 태그는 수정하지 않음)", example = "[\"공부\", \"운동\"]")
        @Size(max = 10, message = "태그는 최대 10개까지 가능합니다.")
        List<@Size(max = 30, message = "각 태그는 최대 30자까지 가능합니다.") String> tags,

        @Schema(description = "변경할 종료일 (null 이면 종료일은 수정하지 않음)", example = "2026-10-07")
        LocalDate endAt,

        @Schema(description = "변경할 내용 (null 이면 내용은 수정하지 않음)", example = "운동 30분 하기")
        @Size(max = 100, message = "내용은 100자 이내여야 합니다.")
        String content,

        @Schema(description = "변경할 정렬 순서 (1부터 시작, null 이면 순서는 수정하지 않음)", example = "3")
        @Min(value = 1, message = "순서는 1 이상이어야 합니다.")
        Long seq,

        @Schema(description = "변경할 상태 (null 이면 상태는 수정하지 않음)", example = "COMPLETED", allowableValues = {"PENDING", "COMPLETED"})
        TodoStatus status
) {}