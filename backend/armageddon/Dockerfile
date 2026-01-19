# 1단계: 빌드 (요리사)
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Gradle 빌드에 필요한 파일들만 먼저 복사 (캐싱 효율을 위해)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 소스 코드 복사
COPY src src

# 빌드 실행 (테스트는 보통 건너뜁니다)
RUN ./gradlew clean build -x test

# 2단계: 실행
FROM eclipse-temurin:17-jre
WORKDIR /app

# 1단계에서 만든 JAR 파일 가져오기
COPY --from=builder /app/build/libs/*.jar app.jar

# 실행
ENTRYPOINT ["java", "-jar", "app.jar"]