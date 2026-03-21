# Spring Boot Java Template - 프로젝트 개요

## 목적
- 초기 개발 생산성을 극대화하기 위한 Spring Boot 기반 멀티모듈 템플릿
- 서비스 성장에 따라 모듈 구조가 확장 가능한 기본 아키텍처 구성
- 도메인별 서비스 분리를 통한 유지보수성 향상

## 기술 스택

| 구분 | 기술 | 버전 |
|------|------|------|
| 언어 | Java | 17 |
| 프레임워크 | Spring Boot | 3.2.0 |
| 빌드 도구 | Gradle | 8.5 |
| Cloud | Spring Cloud | 2023.0.0 |
| 데이터베이스 | MySQL, H2 (테스트) | 8.1.0, 2.2.224 |
| 메시징 | Apache Kafka | 3.6.0 |
| ORM | Spring Data JPA / Hibernate | 3.2.0 / 6.3.1 |
| HTTP Client | OpenFeign | 4.1.0 |
| 모니터링 | Micrometer, Sentry | 1.12.0, 7.0.0 |

## 모듈 구조

```
spring-boot-java-template/
├── core/                    # 핵심 도메인 모듈
│   ├── core-api/           # 실행 가능한 API 모듈 (유일한 실행 모듈)
│   └── core-enum/          # 공통 열거형 모듈
├── clients/                # 외부 시스템 통합 모듈
│   └── client-example/     # HTTP 통신 예제 (OpenFeign)
├── storage/                # 데이터 저장소 모듈
│   ├── db-core/           # 데이터베이스 연동 (JPA)
│   └── kafka-core/        # 카프카 연동
├── support/               # 지원 기능 모듈
│   ├── logging/           # 로깅 및 분산 추적 (Sentry)
│   └── monitoring/        # 모니터링 (Micrometer)
└── tests/                 # 테스트 지원 모듈
    └── api-docs/          # API 문서화 (Spring REST Docs)
```

## 레이어드 아키텍처

- **Presentation Layer**: REST Controllers, DTOs, Exception Handlers
- **Business Layer**: Service Classes, Domain Logic
- **Persistence Layer**: JPA Repositories, Entities
- **Integration Layer**: Feign Clients, Kafka Producers/Consumers

## 패키지 구조 (core-api)

```
io.dodn.springboot.core.api/
├── config/           # 설정 클래스 (AsyncConfig 등)
├── controller/       # REST 컨트롤러
│   └── v1/          # API 버전 1
│       ├── request/  # 요청 DTO
│       └── response/ # 응답 DTO
├── domain/          # 도메인 서비스 및 데이터
└── support/         # 지원 클래스
    ├── response/    # ApiResponse, ResultType
    └── error/       # ErrorCode, CoreApiException
```

## 런타임 프로파일

| 프로파일 | 목적 |
|----------|------|
| local | 네트워크 연결 없이 개발 가능한 환경 |
| local-dev | 로컬에서 개발 서버에 접속 |
| dev | 개발 환경 배포용 |
| staging | 운영 전 검증 환경 |
| live | 실제 서비스 운영 환경 |

## 프로젝트 메타정보

- **그룹**: io.dodn.springboot
- **버전**: 0.0.1-SNAPSHOT
- **라이선스**: Apache License 2.0
- **저장소**: https://github.com/team-dodn/spring-boot-java-template
