package com.example.todo.domain.todo;

import com.example.todo.domain.global.AbstractEntity;
import com.example.todo.domain.member.Member;
import com.example.todo.domain.todo.enumerate.TodoStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends AbstractEntity {
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "tag", nullable = true)
    @ElementCollection
    @CollectionTable(
            name = "todo_tag",
            joinColumns = @JoinColumn(name = "todo_id")
    )
    private List<String> tags = new ArrayList<>();

    @Column(name = "end_at", nullable = false)
    private LocalDate endAt;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "seq", nullable = false)
    private Long seq;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TodoStatus status = TodoStatus.PENDING;

    private Todo(Member member, List<String> tags, LocalDate endAt, String content, Long seq) {
        this.member = member;
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        this.endAt = endAt;
        this.content = content;
        this.seq = seq;
    }

    public static Todo create(Member member, List<String> tags, LocalDate endAt, String content, Long seq) {
        return new Todo(member, tags, endAt, content, seq);
    }

    public void addTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        this.tags.addAll(tags);
    }

    public void removeTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        this.tags.removeAll(tags);
    }

    public void modifyEndAt(LocalDate endAt) {
        this.endAt = endAt;
    }

    public void modifyContent(String content) {
        this.content = content;
    }

    public void modifySeq(Long seq) {
        this.seq = seq;
    }

    public void modifyStatus(TodoStatus status) {
        this.status = status;
    }

    public void clearTags() {
        this.tags.clear();
    }

    public void softDelete() {
        markDeleted();
    }

    public void restore() {
        markRestored();
    }
}
