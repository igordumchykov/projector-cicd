# projector-cicd

CI/CD Project Setup for Simplified version of [Soul Menu Management Back-End Server] (https://github.com/igordumchykov/soul-menu) 
for managing restaurant menu for drinks and food
For more details please refer [initial repository README file] (https://github.com/igordumchykov/soul-menu/blob/master/README.md)

# CI/CD Structure

[main file](.github/workflows/main.yml)

## Build

```yaml
  build:

    name: Build Java Artifact
    runs-on: ubuntu-latest
    steps:

    - uses: actions/checkout@v3
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Setup Gradle
      uses: gradle/gradle-build-action@v2

    - name: Run Unit Tests
      run: |
        chmod +x ./gradlew
        ./gradlew test

    - name: Execute Gradle build
      run: |
        chmod +x ./gradlew
        ./gradlew bootJar

    - name: Copy Jar file
      run: mv build/libs/$(ls build/libs) app.jar

    - uses: actions/upload-artifact@master
      with:
        name: jar-file
        path: app.jar

  push_to_registry:
    needs: build
    name: Push Docker image to Docker Hub
    runs-on: ubuntu-latest
    steps:
      - name: Check out the repo
        uses: actions/checkout@v4

      - uses: actions/download-artifact@master
        with:
          name: jar-file
          path: app.jar

      - name: Log in to Docker Hub
        uses: docker/login-action@f4ef78c080cd8ba55a85445d5b36e214a81df20a
        with:
          username: ${{ secrets.DOCKER_USER }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Extract metadata (tags, labels) for Docker
        id: meta
        uses: docker/metadata-action@9ec57ed1fcdbf14dcef7dfbe97b2010124a938b7
        with:
          images: ${{ env.DOCKERHUB_NAMESPACE }}/${{ env.DOCKERHUB_REPOSITORY }}

      - name: Build and push Docker image
        uses: docker/build-push-action@3b5e8027fcad23fda98b2e3ac259d8d67585f671
        with:
          context: .
          file: ./Dockerfile
          push: true
          tags: ${{ steps.meta.outputs.tags }}
          labels: ${{ steps.meta.outputs.labels }}
```

## Deploy

```yaml
  deploy:
    needs: push_to_registry
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v2
      - name: Login to Docker Hub
        uses: docker/login-action@v1
        with:
          username: ${{ secrets.DOCKER_USER }}
          password: ${{ secrets.DOCKER_PASSWORD }}
      - name: Set permissions for private key
        run: |
          echo "${{ env.AWS_PRIVATE_KEY }}" > key.pem
          chmod 600 key.pem
      - name: Pull Docker image
        run: |
          ssh -o StrictHostKeyChecking=no -i key.pem ubuntu@your-ec2-instance-ip 'sudo docker pull ${{ DOCKERHUB_NAMESPACE }}/${{ DOCKERHUB_REPOSITORY }}:main'
      - name: Stop running container
        run: |
          ssh -o StrictHostKeyChecking=no -i key.pem ubuntu@your-ec2-instance-ip 'sudo docker stop app || true'
          ssh -o StrictHostKeyChecking=no -i key.pem ubuntu@your-ec2-instance-ip 'sudo docker rm app || true'
      - name: Run new container
        run: |
          ssh
            -o StrictHostKeyChecking=no
            -i key.pem ubuntu@your-ec2-instance-ip
            'sudo docker run
              --env SPRING_PROFILES_ACTIVE=${{ secrets.SPRING_PROFILES_ACTIVE }} \
              --env APPLICATION_SECURITY_AUTHENTICATION_SECRET=${{ secrets.APPLICATION_SECURITY_AUTHENTICATION_SECRET }} \
              --env INIT_USER_EMAIL_LIST=${{ secrets.INIT_USER_EMAIL_LIST }} \
              --env INIT_USER_LOGIN_LIST=${{ secrets.INIT_USER_LOGIN_LIST }} \
              --env INIT_USER_ROLE_LIST=${{ secrets.INIT_USER_ROLE_LIST }} \
              --env INIT_USER_PASSWORD_LIST=${{ secrets.INIT_USER_PASSWORD_LIST }} \
              --env DB_NAME=${{ secrets.DB_NAME }} \
              --env DB_URI=${{ secrets.DB_URI }} \
              -d --name app -p 8080:8080 ${{ DOCKERHUB_NAMESPACE }}/${{ DOCKERHUB_REPOSITORY }}:main'
```

# Results for the build

## Build Pipeline
![](docs/pipeline.png)

## Image in Dockerhub
![](docs/image.png)