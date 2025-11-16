## 📝 TODO Service – Spring Boot 기반 개인/관리자용 Todo 관리 시스템
<p align="left"> <img src="https://img.shields.io/badge/Java-17-007396?logo=openjdk" /> <img src="https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?logo=springboot" /> <img src="https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql" /> <img src="https://img.shields.io/badge/Redis-DC382D?logo=redis" /> <img src="https://img.shields.io/badge/Spring%20Security-6.5.6-6DB33F?logo=springsecurity" /> <img src="https://img.shields.io/badge/JWT-000000?logo=jsonwebtokens" /> <img src="https://img.shields.io/badge/Swagger-85EA2D?logo=swagger" /> <img src="https://img.shields.io/badge/Gradle-02303A?logo=gradle" /> </p>
사용자가 개인의 할 일을 생성하고 정리하며, 관리자는 정책에 따라 모든 사용자의 ToDo를 관리할 수 있는 Todo 관리 서비스입니다.
Spring Boot 기반으로 구현되었으며, JWT 기반 인증/인가, 태그, 순서 정렬, ToDo 공유 등 다양한 기능을 제공합니다.

### ✨ 개발 목표
- 사용자가 직관적으로 ToDo를 생성/관리할 수 있는 시스템 구축
- 유저별 권한 관리, 관리자 정책을 설정할 수 있는 구조 확립
- 확장 가능하고 유지 보수 용이한 아키텍처 설계
- 추후 요구사항(삭제 복구, 공유 고도화 등)을 쉽게 반영할 수 있는 구조 구성

### ERD 
<img width="1221" height="280" alt="image" src="https://github.com/user-attachments/assets/779493bf-e0a5-47ac-b8e8-f9fa4d9a1cb2" />


### 🚀 Tech Stack
| 카테고리                    | 기술                                                                   |
| ----------------------- | -------------------------------------------------------------------- |
| **Backend**             | Spring Boot (Java 17+), Spring Web, Spring Security, Spring Data JPA |
| **Database**            | PostgreSQL                                                           |
| **Cache**     | Redis (토큰 캐싱 및 주기적 관리)                                               |
| **Authentication**      | JWT (Access Token / Refresh Token)                                   |
| **API 문서화**             | Swagger / Springdoc-openapi                                          |
| **Build & Tools**       | Gradle, Lombok                                                       |
| **Deployment-ready 구조** | 계정 초기화 Runner, 정책 기반 권한 처리                                           |

### 📚 주요 기능 요약
**1. ToDo CRUD**
- ToDo 생성 / 조회 / 수정 / 삭제
- 생성자(Member) 기준으로 관리
- Soft-delete 방식 적용하여 복구 확장 가능

**2. 태그 기반 분류**
- ToDo에 여러 태그 등록 가능
- 태그 기반 필터링 지원

**3. ToDo 순서(Seq) 변경**
- 사용자별 Seq 관리
- 순서 수정 시 자동 Shift 처리
- 안정된 정렬 보장

**4. ToDo 공유 기능**
- 다른 사용자에게 조회만 가능하도록 공유
- 수정/삭제는 원 작성자만 가능

**5. 권한 관리 (관리자 포함)**
- 일반 사용자: 본인 ToDo만 수정/삭제 가능
- 관리자:
    - MANAGE_ALL 정책 → 모든 사용자 ToDo 수정/삭제 가능
    - MANAGE_OWN_ONLY 정책 → 관리자도 본인 것만 수정/삭제
- 정책은 application.yml에서 간단히 변경 가능
```yml
# 모든 사용자 ToDo 수정/삭제 가능
todo:
  admin-policy: MANAGE_ALL

# 관리자 본인 것만 가능
todo:
  admin-policy: MANAGE_OWN_ONLY
```

**6. JWT 기반 인증/인가**
- AccessToken + RefreshToken 구조
- Redis에서 Refresh Token 캐싱
- Spring Security를 활용한 인증 처리


### 🧱 프로젝트 구조 & 헥사고날 아키텍처

본 프로젝트는 Hexagonal Architecture (Ports & Adapters Architecture) 를 기반으로 설계되었습니다.
비즈니스 규칙이 위치한 Domain을 중심으로, Application 서비스와 Adapter를 계층적으로 분리하여
확장성과 유지보수성을 고려한 구조를 지향합니다.

                        +-------------------------+
                        |      WEB / SECURITY     |
                        |    (Controllers, JWT)   |
                        +-----------▲-------------+
                                    |
                                    | Adapter Layer
                                    |
                     +--------------┴---------------+
                     |       Application Layer       |
                     |  (UseCase, Services, DTO)     |
                     +--------------▲---------------+
                                    |
                                    | Domain 객체 조작
                                    |
                      +-------------┴-------------+
                      |        Domain Layer        |
                      |  (Entity, VO, Rule)        |
                      +----------------------------+

📌 DTO 배치를 Application 레이어로 통합함으로써 얻은 이점
- Domain의 순수성 보존 (HTTP/JSON/Swagger와 전혀 무관)
- Application을 중심으로 Web Adapter와 유스케이스의 관계가 단순해짐
- DTO 중복 제거 → 유지보수 부담 감소


---
### 👨‍💻 Developer
Jihyeon An (안지현)
Back-end Developer
