# 코드 스타일 및 컨벤션

## 코드 포맷팅

### Spring Java Format
- 프로젝트는 **Spring Java Format** 플러그인을 사용하여 일관된 코드 스타일 유지
- 버전: 0.0.40
- Git Hook으로 커밋 전 자동 검사 (`./gradlew checkFormat`)

### IntelliJ IDEA 설정
1. Spring Java Format 플러그인 설치
   - [Spring Java Format IntelliJ IDEA](https://github.com/spring-io/spring-javaformat#intellij-idea)
2. 테스트 실행 설정:
   - `Build, Execution, Deployment > Build Tools > Gradle > Run tests using > IntelliJ IDEA`

## 네이밍 컨벤션

### 패키지
- 루트 패키지: `io.dodn.springboot`
- 모듈별 하위 패키지: `core.api`, `storage.db`, `clients` 등

### 클래스
- **Controller**: `*Controller` (예: `ExampleController`, `HealthController`)
- **Service**: `*Service` (예: `ExampleService`)
- **DTO**: `*Dto` (예: `ExampleRequestDto`, `ExampleResponseDto`)
- **Entity**: `*Entity` (예: `ExampleEntity`)
- **Exception**: `*Exception` (예: `CoreApiException`)

### DTO 패턴
- Java Record 사용 권장
- 요청 DTO: `request` 패키지에 위치
- 응답 DTO: `response` 패키지에 위치

```java
// Request DTO
public record ExampleRequestDto(String data) {
    public ExampleData toExampleData() {
        return new ExampleData(data);
    }
}

// Response DTO
public record ExampleResponseDto(String result) {
    public static ExampleResponseDto from(ExampleResult result) {
        return new ExampleResponseDto(result.value());
    }
}
```

## API 응답 패턴

### 공통 응답 래퍼
- `ApiResponse<T>` 클래스 사용
- `ResultType`: 성공/실패 구분

```java
@GetMapping("/example")
public ApiResponse<ExampleResponseDto> getExample() {
    return ApiResponse.success(result);
}
```

## 에러 처리

### 예외 클래스
- `CoreApiException`: 비즈니스 예외
- `ErrorCode`: 에러 코드 열거형
- `ErrorType`: 에러 유형 분류
- `ApiControllerAdvice`: 전역 예외 핸들러

## API 버전 관리

- 패키지 기반 버전 관리: `controller.v1`, `controller.v2`
- URL 패턴: `/api/v1/*`

## 테스트 컨벤션

### 테스트 태그 사용
```java
@Tag("context")  // Spring Context 통합 테스트
@Tag("restdocs") // API 문서 생성용
@Tag("develop")  // 개발 중 테스트 (CI 제외)
```

### 테스트 클래스 위치
- 단위 테스트: `src/test/java`
- 통합 테스트: `@Tag("context")` 표시
- API 문서 테스트: `tests/api-docs` 모듈

## 의존성 관리

- 모든 버전은 `gradle.properties`에서 중앙 관리
- 새 의존성 추가 시 `gradle.properties`에 버전 정의 후 `build.gradle`에서 참조

```properties
# gradle.properties
newLibraryVersion=1.0.0
```

```groovy
// build.gradle
implementation "com.example:library:${newLibraryVersion}"
```
