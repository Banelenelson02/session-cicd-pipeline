# Step 7 — Build a Docker Image in the Pipeline

**Goal:** the final piece. Add a stage that builds a **Docker image** of the app. This is
exactly Goal 4 of Iteration 2, practised on the tiny app first.

---

## 7.1 — First, what the Dockerfile does

There's a `Dockerfile` in this repo already. Open it. It has two stages:
1. **build** — uses a Maven image to compile the jar
2. **run** — copies just the jar onto a slim Java runtime image

Try it **locally** first (if you have Docker installed):
```bash
docker build -t calculator-app .
docker run --rm calculator-app
```
You should see the calculator demo print from inside a container. That's your app, packaged
so it runs the same anywhere. ✅

> If Docker isn't installed yet — install it now. Iteration 2 requires **everyone** to run
> Docker on their own machine. Don't leave it to the last week.

---

## 7.2 — Building Docker *inside* the pipeline (docker-in-docker)

Building an image inside a pipeline job is special: the job itself runs in a container, and
now it needs to run `docker build` — a container building a container. GitLab handles this
with a **service** called `docker:dind` ("Docker-in-Docker").

Add a `package` stage:

```yaml
stages:
  - test
  - build
  - package

# ... run-tests and build-jar jobs from before ...

build-docker-image:
  stage: package
  image: docker:latest
  services:
    - docker:dind
  script:
    - docker build -t calculator-app .
```

Reading the new job:
- `image: docker:latest` — the job runs in an image that has the `docker` command
- `services: [docker:dind]` — starts a Docker engine the job can talk to
- `script: docker build ...` — builds the image, same command as local

---

## 7.3 — The complete pipeline

Your full `.gitlab-ci.yml` now:

```yaml
stages:
  - test
  - build
  - package

run-tests:
  stage: test
  image: maven:3.9-eclipse-temurin-21
  script:
    - mvn test

build-jar:
  stage: build
  image: maven:3.9-eclipse-temurin-21
  script:
    - mvn package -DskipTests
  artifacts:
    paths:
      - target/*-jar-with-dependencies.jar
    expire_in: 1 week

build-docker-image:
  stage: package
  image: docker:latest
  services:
    - docker:dind
  script:
    - docker build -t calculator-app .
```

Push it:
```bash
git add .gitlab-ci.yml Dockerfile
git commit -m "build docker image in pipeline"
git push
```

Watch three stages run in order: **test → build → package**. The last one builds your
container image. 🎉

---

## 7.4 — If docker-in-docker doesn't work on WeThinkCode's runners

Some shared runners don't allow `docker:dind` for security reasons. If the docker job fails
with a permissions or "cannot connect to the Docker daemon" error:

- It's a **runner configuration** issue, not your YAML. Ask a coach whether dind is enabled.
- Fallback: you can still demo `docker build` **locally** on each machine (which Iteration 2
  requires anyway), and keep test + build automated in the pipeline.

Don't burn hours fighting the runner — get test+build green in CI, and demo Docker locally.

---

✅ **Done when:** your pipeline runs test → build → package, and the last stage builds a
Docker image (or you've confirmed dind isn't available and can build locally). Next:
**[Step 8 — Apply it to Robot World](08-apply-to-robot-world.md)**.
