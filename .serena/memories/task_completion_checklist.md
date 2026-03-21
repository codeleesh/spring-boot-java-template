# 작업 완료 체크리스트

## 코드 변경 후 필수 확인 사항

### 1. 코드 품질 검사

```bash
# 코드 포맷 검사 (필수)
./gradlew checkFormat

# 포맷 오류 시 자동 수정
./gradlew format
```

### 2. 테스트 실행

```bash
# 단위 테스트 (빠른 검증)
./gradlew unitTest

# 전체 테스트 (CI와 동일)
./gradlew test

# 통합 테스트 (Spring Context 관련 변경 시)
./gradlew contextTest
```

### 3. 빌드 확인

```bash
# 전체 빌드
./gradlew build

# 실행 가능 JAR 생성 (배포 전)
./gradlew :core:core-api:bootJar
```

### 4. API 문서 (API 변경 시)

```bash
# REST Docs 테스트 및 문서 생성
./gradlew restDocsTest asciidoctor
```

## 커밋 전 체크리스트

- [ ] `./gradlew checkFormat` 통과
- [ ] `./gradlew test` 통과
- [ ] 새로운 의존성은 `gradle.properties`에 버전 추가
- [ ] API 변경 시 REST Docs 업데이트

## Git Hook 활성화 (권장)

```bash
git config core.hookspath .githooks
```

이 설정으로 커밋 시 자동으로 `checkFormat` 실행됨

## 특정 상황별 추가 체크

### 새 모듈 추가 시
1. `settings.gradle`에 모듈 추가
2. 해당 모듈에 `build.gradle` 생성
3. 의존성 버전은 `gradle.properties`에 정의

### Entity 변경 시
1. 통합 테스트 실행: `./gradlew contextTest`
2. 데이터베이스 스키마 변경 확인

### API 엔드포인트 추가/변경 시
1. REST Docs 테스트 작성
2. 문서 생성: `./gradlew restDocsTest asciidoctor`
3. `http/` 폴더의 HTTP 테스트 파일 업데이트

## 배포 전 최종 체크

```bash
# 전체 클린 빌드
./gradlew clean build

# 실행 테스트
./gradlew :core:core-api:bootRun --args='--spring.profiles.active=local'
```
