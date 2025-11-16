package com.example.todo.application.todo.dto;

import com.example.todo.domain.todo.enumerate.TodoStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TodoSearchCondition(
        @Schema(
                description = "조회할 사용자 ID (선택). null이면 모든 사용자의 TODO 조회",
                example = "1"
        )
        Long memberId,

        @Schema(
                description = """
                        태그 필터링 (선택).
                        여러 개 전달하려면 다음과 같이 입력합니다:
                        ["공부", "운동"]
                        
                        Swagger에서는 'Add item' 버튼을 눌러 여러 개의 값을 추가하면
                        자동으로 ?tags=공부&tags=운동 형태로 전송됩니다.
                        """,
                example = "[\"공부\"]"
        )
        List<String> tags,

        @Schema(
                description = """
                        TODO 상태 필터링 (선택).
                        가능한 값: PENDING, COMPLETED
                        """,
                example = "PENDING"
        )
        TodoStatus status
) {}