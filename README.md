## 📝 TODO Service – Spring Boot 기반 개인/관리자용 Todo 관리 시스템
사용자가 개인의 할 일을 생성하고 정리하며, 관리자는 정책에 따라 모든 사용자의 ToDo를 관리할 수 있는 Todo 관리 서비스입니다.
Spring Boot 기반으로 구현되었으며, JWT 기반 인증/인가, 태그, 순서 정렬, ToDo 공유 등 다양한 기능을 제공합니다.

### ✨ 개발 목표
- 사용자가 직관적으로 ToDo를 생성/관리할 수 있는 시스템 구축
- 유저별 권한 관리, 관리자 정책을 설정할 수 있는 구조 확립
- 확장 가능하고 유지 보수 용이한 아키텍처 설계
- 추후 요구사항(삭제 복구, 공유 고도화 등)을 쉽게 반영할 수 있는 구조 구성

### 🚀 Tech Stack
| 카테고리                    | 기술                                                                   |
| ----------------------- | -------------------------------------------------------------------- |
| **Backend**             | Spring Boot (Java 17+), Spring Web, Spring Security, Spring Data JPA |
| **Database**            | PostgreSQL                                                           |
| **Cache / Session**     | Redis (토큰 캐싱 및 주기적 관리)                                               |
| **Authentication**      | JWT (Access Token / Refresh Token)                                   |
| **API 문서화**             | Swagger / Springdoc-openapi                                          |
| **Build & Tools**       | Gradle, Lombok                                                       |
| **Deployment-ready 구조** | 계정 초기화 Runner, 정책 기반 권한 처리                                           |

### 📚 주요 기능 요약
1. ToDo CRUD
- ToDo 생성 / 조회 / 수정 / 삭제
- 생성자(Member) 기준으로 관리
- Soft-delete 방식 적용하여 복구 확장 가능

2. 태그 기반 분류
- ToDo에 여러 태그 등록 가능
- 태그 기반 필터링 지원

3. ToDo 순서(Seq) 변경
- 사용자별 Seq 관리
- 순서 수정 시 자동 Shift 처리
- 안정된 정렬 보장

4. ToDo 공유 기능
- 다른 사용자에게 조회만 가능하도록 공유
- 수정/삭제는 원 작성자만 가능

5. 권한 관리 (관리자 포함)
- 일반 사용자: 본인 ToDo만 수정/삭제 가능
- 관리자:
    - MANAGE_ALL 정책 → 모든 사용자 ToDo 수정/삭제 가능
    - MANAGE_OWN_ONLY 정책 → 관리자도 본인 것만 수정/삭제
- 정책은 application.yml에서 간단히 변경 가능

6. JWT 기반 인증/인가
- AccessToken + RefreshToken 구조
- Redis에서 Refresh Token 캐싱
- Spring Security를 활용한 인증 처리
