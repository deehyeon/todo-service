package com.example.todo.adapter.todo;

import com.example.todo.adapter.security.AuthDetails;
import com.example.todo.application.todo.dto.TodoResponse;
import com.example.todo.application.todo.dto.TodoSearchCondition;
import com.example.todo.application.todo.provided.TodoFinder;
import com.example.todo.application.todo.provided.TodoSaver;
import com.example.todo.domain.global.apiPayload.response.ApiResponse;
import com.example.todo.domain.todo.dto.TodoCreateRequest;
import com.example.todo.domain.todo.dto.TodoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/todo")
@Tag(name = "TODO", description = "TODO 관련 API")
@RequiredArgsConstructor
public class TodoController {
    private final TodoFinder todoFinder;
    private final TodoSaver todoSaver;

    @Operation(
            summary = "TODO 생성",
            description = """
                    새로운 TODO 항목을 생성합니다.
                    
                    - 태그(tags)는 선택 입력이며 최대 10개까지 가능합니다.
                    - 종료일(endAt)은 존재하는 날짜여야 하며 YYYY-MM-DD 형식입니다.
                    - seq(순서)는 서버에서 자동으로 결정됩니다.
                    
                    - 생성된 TODO는 요청한 사용자에게 소속됩니다.
                    - 완료 여부는 기본값(PENDING)으로 설정됩니다.
                    - 응답으로는 생성된 TODO의 ID가 반환됩니다.
                    """
    )
    @PostMapping()
    public ApiResponse<Long> makeTodo(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody @Valid TodoCreateRequest request) {
        Long todoId = todoSaver.save(authDetails.getMemberId(), request);
        return ApiResponse.success(todoId);
    }

    @Operation(
            summary = "TODO 수정",
            description = """
                    기존 TODO 항목을 수정합니다.
                    
                    - 태그, 종료일, 내용, 순서(seq), 상태(status) 등을 선택적으로 수정할 수 있습니다.
                    - seq를 변경하면 해당 사용자의 TODO 목록에서 순서가 자동으로 재정렬됩니다.
                    -  TODO는 생성한 사용자만 수정할 수 있습니다.
                    
                    - 수정하지 않는 필드는 요청에서 제외하면 됩니다.
                    - 성공 시 특별한 데이터 없이 성공 여부만 반환합니다.
                    """
    )
    @PatchMapping("/{todoId}")
    public ApiResponse<?> updateTodo(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long todoId,
            @RequestBody @Valid TodoUpdateRequest request) {
        todoSaver.update(todoId, authDetails.getMemberId(), request);
        return ApiResponse.success();
    }

    @Operation(
            summary = "TODO 검색",
            description = """
                    다양한 조건으로 TODO 목록을 조회합니다.
                    
                    - 가능한 검색 조건:
                      - memberId: 특정 사용자 TODO만 조회
                      - tags: 태그 포함 검색
                      - status: PENDING / COMPLETED
                    
                    - 정렬은 기본적으로 seq(순서) 기준으로 이루어집니다.
                    
                    - 페이징(pageable) 파라미터를 함께 사용할 수 있습니다.
                    - 검색 조건이 없으면 전체 TODO 목록을 조회합니다.
                    """
    )
    @GetMapping()
    public ApiResponse<Page<TodoResponse>> getTodos(
            @Valid @ParameterObject TodoSearchCondition searchCondition,
            @ParameterObject @PageableDefault(size = 20, sort = "seq", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<TodoResponse> todoResponses = todoFinder.searchTodos(searchCondition, pageable);
        return ApiResponse.success(todoResponses);
    }

    @Operation(
            summary = "내 TODO 전체 조회",
            description = """
                    로그인한 사용자가 생성한 모든 TODO를 조회합니다.
                    
                    - 사용자의 모든 TODO를 seq(순서) 기준으로 정렬하여 반환합니다.
                    - D-Day 정보(restDate)도 자동으로 포함됩니다.
                    
                    - 페이징(pageable) 파라미터 지원
                    - 다른 사람의 TODO는 조회할 수 없습니다.
                    """
    )
    @GetMapping("/me")
    public ApiResponse<Page<TodoResponse>> getMyTodos(
            @AuthenticationPrincipal AuthDetails authDetails,
            @ParameterObject @PageableDefault(size = 20, sort = "seq", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<TodoResponse> myAllTodos = todoFinder.getMyAllTodos(authDetails.getMemberId(), pageable);
        return ApiResponse.success(myAllTodos);
    }

    @Operation(
            summary = "TODO 단건 조회",
            description = """
                    todoId로 todo를 단건 조회합니다.
                    """
    )
    @GetMapping("/{todoId}")
    public ApiResponse<TodoResponse> getTodo(
            @PathVariable Long todoId) {
        TodoResponse todoResponse = todoFinder.findById(todoId);
        return ApiResponse.success(todoResponse);
    }

    @Operation(
            summary = "삭제된 TODO 목록 조회",
            description = """
                    삭제된 TODO 목록을 조회합니다.
                    내가 작성했던 TODO의 삭제된 목록만 볼 수 있습니다. 
                    """
    )
    @GetMapping("/me/deleted")
    public Page<TodoResponse> getMyDeletedTodos(
            @AuthenticationPrincipal AuthDetails authDetails,
            @ParameterObject @PageableDefault(size = 20, sort = "seq", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return todoFinder.getMyDeletedTodos(authDetails.getMemberId(), pageable);
    }

    @Operation(
            summary = "TODO softDelete",
            description = """
                    TODO를 삭제합니다.
                    softDelete로 나중에 복구가 가능합니다.
                    """
    )
    @DeleteMapping("/{todoId}")
    public ApiResponse<?> deleteTodo(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long todoId) {
        todoSaver.delete(todoId, authDetails.getMemberId());
        return ApiResponse.success();
    }

    @Operation(
            summary = "삭제된 TODO를 복구",
            description = """
                    삭제된 TODO를 복구합니다.
                    내가 작성했던 TODO에 대해서만 복구가 가능합니다.
                    """
    )
    @PostMapping("/{id}/restore")
    public ApiResponse<Void> restoreMyTodo(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {
        Long memberId = authDetails.getMemberId();
        todoSaver.restore(id, memberId);
        return ApiResponse.success(null);
    }
}
