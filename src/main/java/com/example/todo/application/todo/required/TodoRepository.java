package com.example.todo.application.todo.required;

import com.example.todo.domain.todo.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {
    @Query("SELECT COALESCE(MAX(t.seq), 0) FROM Todo t WHERE t.member.id = :memberId")
    Long findLastSeqByMemberId(@Param("memberId") Long memberId);

    List<Todo> findAllByMemberIdOrderBySeq(Long memberId);
    Optional<Todo> findByIdAndIsDeletedFalse(Long id);

    Page<Todo> findByMember_IdAndIsDeletedTrue(Long memberId, Pageable pageable);

    Optional<Todo> findByIdAndMember_IdAndIsDeletedTrue(Long id, Long memberId);
}