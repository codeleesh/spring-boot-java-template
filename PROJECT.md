# 문서 작성 주제 구성

## 1. 프로젝트 개요

### 1.1 프로젝트 목적 및 범위

**목적**
- 초기 개발 생산성을 극대화하기 위한 Spring Boot 기반 멀티모듈 템플릿 제공
- 서비스 성장에 따라 모듈 구조가 확장 가능한 기본 아키텍처 구성
- 도메인별 서비스 분리를 통한 유지보수성 향상

**범위**
- 도메인 서비스별 모듈화 구조 (Core)
- 외부 시스템 통합 모듈 (Clients) 
- 저장소 연동 모듈 (Storage)
- 지원 기능 모듈 (Support)
- 테스트 편의성 모듈 (Tests)

## 2. 기술 스택 및 버전 정보

### 2.1 언어 및 프레임워크 버전표

| 구분 | 기술 | 버전 |
|------|------|------|
| 언어 | Java | 17 |
| 프레임워크 | Spring Boot | 3.2.0 |
| 빌드 도구 | Gradle | 8.5 |
| 패키지 관리 | Spring Dependency Management | 1.1.4 |

### 2.2 Spring 생태계 버전표

| 구분 | 기술 | 버전 |
|------|------|------|
| Cloud Dependencies | Spring Cloud | 2023.0.0 |
| Java Format | Spring Java Format | 0.0.40 |
| REST Docs | Asciidoctor Convert | 3.3.2 |

### 2.3 데이터베이스 및 메시징 기술표

| 구분 | 기술 | 버전 |
|------|------|------|
| RDBMS | MySQL | 8.1.0 (connector) |
| 메모리 DB | H2 | 2.2.224 |
| 메시징 | Apache Kafka | 3.6.0 |
| ORM | Hibernate Core | 6.3.1.Final |
| JPA | Spring Data JPA | 3.2.0 |

### 2.4 모니터링 및 로깅 기술표

| 구분 | 기술 | 버전 |
|------|------|------|
| 메트릭 | Micrometer | 1.12.0 |
| 추적 | Brave Tracing | 5.16.0 |
| 로깅 | Logback | 1.4.11 |
| 에러 추적 | Sentry | 7.0.0 |
| 프로메테우스 | Micrometer Prometheus | 1.12.0 |

### 2.5 HTTP 클라이언트 및 테스트 기술표

| 구분 | 기술 | 버전 |
|------|------|------|
| HTTP 클라이언트 | OpenFeign | 4.1.0 |
| 테스트 프레임워크 | JUnit Jupiter | 5.10.1 |
| 모킹 | Mockito | 5.7.0 |
| REST 테스트 | REST Assured | 5.3.2 |
| 문서화 | Spring REST Docs | 3.0.1 |

## 3. 프로젝트 아키텍처

### 3.1 전체 모듈 구조 다이어그램

```
spring-boot-java-template/
├── core/                    # 핵심 도메인 모듈
│   ├── core-api/           # 실행 가능한 API 모듈
│   └── core-enum/          # 공통 열거형 모듈
├── clients/                # 외부 시스템 통합 모듈
│   └── client-example/     # HTTP 통신 예제
├── storage/                # 데이터 저장소 모듈
│   ├── db-core/           # 데이터베이스 연동
│   └── kafka-core/        # 카프카 연동
├── support/               # 지원 기능 모듈
│   ├── logging/           # 로깅 및 추적
│   └── monitoring/        # 모니터링
└── tests/                 # 테스트 지원 모듈
    └── api-docs/          # API 문서화
```

### 3.2 레이어드 아키텍처 설계

**Presentation Layer**
- REST Controllers
- Request/Response DTOs
- Exception Handlers

**Business Layer**
- Service Classes
- Domain Logic
- Business Rules

**Persistence Layer**
- JPA Repositories
- Entity Classes
- Database Configurations

**Integration Layer**
- Feign Clients
- Kafka Producers/Consumers
- External API Integrations

## 4. 주요 기능 설명


## 5. 설정 및 프로파일

### 5.1 런타임 프로파일 정의

| 프로파일 | 목적 | 설명 |
|----------|------|------|
| local | 로컬 개발 | 네트워크 연결 없이 개발 가능한 환경 설정 |
| local-dev | 로컬-개발서버 연결 | 로컬에서 개발 환경에 접속하여 작업 |
| dev | 개발 환경 | 개발 환경 배포용 설정 |
| staging | 스테이징 환경 | 운영 전 검증 환경 설정 |
| live | 운영 환경 | 실제 서비스 운영 환경 설정 |

### 5.2 테스트 태스크 분류

| 태스크 | 목적 | 포함 범위 | 실행 방법 |
|--------|------|-----------|----------|
| test | CI 실행 테스트 | 전체 테스트 (develop, restdocs 제외) | `./gradlew test` |
| unitTest | 단위 테스트 | 의존성 없는 빠른 테스트 | `./gradlew unitTest` |
| contextTest | 통합 테스트 | Spring Context 로딩 테스트 | `./gradlew contextTest` |
| restDocsTest | 문서화 테스트 | API 문서 생성용 테스트 | `./gradlew restDocsTest` |
| developTest | 개발 테스트 | CI에서 실행하지 않는 테스트 | `./gradlew developTest` |

### 5.3 Gradle 태스크 설정

**빌드 관련**
- `bootJar.enabled = false` (서브모듈)
- `jar.enabled = true` (서브모듈)
- `bootJar.enabled = true` (core-api만)

**테스트 태그 관리**
- `excludeTags('develop', 'restdocs')` - 기본 test 태스크
- `includeTags('context')` - contextTest 태스크
- `includeTags('restdocs')` - restDocsTest 태스크

## 6. 빌드 및 배포 가이드

### 6.1 Gradle 설정 구조

**루트 빌드 파일**: `build.gradle`
- 멀티 모듈 프로젝트 설정
- 공통 의존성 관리 (dependencyManagement)
- 플러그인 적용 (Spring Boot, Java Format, Spotless)
- 서브프로젝트 공통 설정

**의존성 관리**: `gradle.properties`
- 모든 의존성 버전 중앙 관리
- 프로젝트 메타데이터 정의
- 플러그인 버전 관리

**모듈 설정**: `settings.gradle`
- 포함할 서브모듈 정의
- 플러그인 관리 설정

### 6.2 실행 명령어 모음

**빌드 관련**
```bash
# 전체 프로젝트 빌드
./gradlew build

# 특정 모듈 빌드
./gradlew :core:core-api:build

# 전체 프로젝트 클린 빌드
./gradlew clean build
```

**테스트 관련**
```bash
# 단위 테스트만 실행
./gradlew unitTest

# 통합 테스트 실행
./gradlew contextTest

# API 문서 생성
./gradlew restDocsTest asciidoctor

# 개발용 테스트 실행
./gradlew developTest
```

**실행 관련**
```bash
# 애플리케이션 실행
./gradlew :core:core-api:bootRun

# 특정 프로파일로 실행
./gradlew :core:core-api:bootRun --args='--spring.profiles.active=local'

# JAR 파일 생성 후 실행
./gradlew :core:core-api:bootJar
java -jar core/core-api/build/libs/core-api-0.0.1-SNAPSHOT.jar
```

## 7. 개발 가이드라인
- 코드 스타일 규칙 (Spring Java Format)
- 테스트 전략 및 방법론
- 모듈 설계 원칙

## 8. 용어 사전
- 아키텍처 관련 용어
- 기술 스택 용어
- 테스트 관련 용어
- 운영 환경 용어

## 9. 참고 자료
- 외부 문서 링크
- 프로젝트 관련 파일 목록
- 개발 도구 설정 가이드

## 10. 메타데이터
- 문서 버전 히스토리
- 연락처 및 지원 정보
- 라이선스 정보