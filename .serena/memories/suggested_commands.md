# 개발 명령어 모음

## 빌드 명령어

```bash
# 전체 프로젝트 빌드
./gradlew build

# 전체 프로젝트 클린 빌드
./gradlew clean build

# 특정 모듈 빌드
./gradlew :core:core-api:build
```

## 테스트 명령어

```bash
# 기본 테스트 (CI용, develop/restdocs 태그 제외)
./gradlew test

# 단위 테스트만 실행 (빠른 테스트)
./gradlew unitTest

# 통합 테스트 (Spring Context 로딩)
./gradlew contextTest

# API 문서 생성용 테스트
./gradlew restDocsTest

# API 문서 생성
./gradlew restDocsTest asciidoctor

# 개발용 테스트 (CI에서 제외되는 테스트)
./gradlew developTest
```

## 실행 명령어

```bash
# 애플리케이션 실행
./gradlew :core:core-api:bootRun

# 특정 프로파일로 실행
./gradlew :core:core-api:bootRun --args='--spring.profiles.active=local'
./gradlew :core:core-api:bootRun --args='--spring.profiles.active=local-dev'

# JAR 파일 생성
./gradlew :core:core-api:bootJar

# JAR 파일 실행
java -jar core/core-api/build/libs/core-api-0.0.1-SNAPSHOT.jar
java -jar core/core-api/build/libs/core-api-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

## 코드 품질 명령어

```bash
# 코드 포맷 검사 (Spring Java Format)
./gradlew checkFormat

# 코드 포맷 적용
./gradlew format

# Spotless 포맷 적용 (비활성화 상태)
# ./gradlew spotlessApply
```

## Git Hook 설정

```bash
# 커밋 전 자동 lint 검사 활성화
git config core.hookspath .githooks
```

## 시스템 유틸 명령어 (macOS/Darwin)

```bash
# 파일 검색
find . -name "*.java"

# 내용 검색
grep -r "pattern" --include="*.java" .

# 디렉토리 목록
ls -la

# Git 상태
git status
git log --oneline -10
```

## 테스트 태그 설명

| 태그 | 용도 | 태스크 |
|------|------|--------|
| (없음) | 일반 단위 테스트 | unitTest |
| @Tag("context") | Spring Context 통합 테스트 | contextTest |
| @Tag("restdocs") | API 문서 생성용 테스트 | restDocsTest |
| @Tag("develop") | 개발 중 임시 테스트 (CI 제외) | developTest |
