# projector-cicd

Simplified version of [Soul Menu Management Back-End Server] (https://github.com/igordumchykov/soul-menu) 
for managing restaurant menu for drinks and food

# Prerequisites

- Java 17.x
- Kotlin 1.7.x
- Gradle 7.5.x

# Build application

```shell
./gradlew build
```
# Start application

```shell
SPRING_PROFILES_ACTIVE=<profile dev/qa/prod> \
APPLICATION_SECURITY_AUTHENTICATION_SECRET=<provide any base 64 encoded secret value> \
INIT_USER_EMAIL_LIST=<coma separated emails to be used for users> \
INIT_USER_LOGIN_LIST=<coma separated logins to be used for users> \
INIT_USER_ROLE_LIST=<coma separated role list for users. Values: ROLE_ADMIN,ROLE_USER,ROLE_ANONYMOUS> \
INIT_USER_PASSWORD_LIST=<coma separated passwords to be used for users> \
DB_NAME=<db name> \
DB_URI='<db url>' \
./gradlew bootRun
```
# Docker

## Build Docker Image
```shell
docker build --build-arg SERVER_PORT=<port> -t <image-name> ./src/main/docker
```

## Run Docker Container
```shell
docker run -d -p 8080:8080 \
  --env SPRING_PROFILES_ACTIVE='' \
  --env APPLICATION_SECURITY_AUTHENTICATION_SECRET='' \
  --env INIT_USER_EMAIL_LIST='' \
  --env INIT_USER_LOGIN_LIST='' \
  --env INIT_USER_ROLE_LIST='' \
  --env INIT_USER_PASSWORD_LIST='' \
  --env DB_NAME='' \
  --env DB_URI='' \
  --name soul-core <image-name> --network=host
```

# API Documentation

- when application is running, go to `<app host>:<app port>/swagger-ui/index.html`
- run `./gradlew generateOpenApiDocs` -> open `./build/openapi.json` -> copy file and paste to `https://editor.swagger.io/`

# Contributors
- Igor Dumchykov [igor.dumchykov@gmail.com]