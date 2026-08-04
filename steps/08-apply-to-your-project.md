# Step 8 — Apply It to Your Real Project

**Goal:** you've built a full pipeline on the toy app. Now map every piece back to a real
Java project. The concepts are identical — only names change.

---

## 8.1 — The translation table

| Tutorial (calculator) | Your real project |
|-----------------------|-------------------|
| `mvn test` (5 tests) | `mvn test` (your real test suite) |
| `mvn package` → calculator jar | `mvn package` → your `*-jar-with-dependencies.jar` |
| main class `com.tutorial.App` | your real main class |
| calculator Dockerfile | your project's Dockerfile |
| `calculator-app` image | your project's image name |

Everything you learned transfers directly. Your real workflow is the same shape with these
names swapped.

---

## 8.2 — ⚠️ If your real project is on GitLab (like Robot World)

This tutorial taught **GitHub Actions**. If your graded project lives on **GitLab**
(`gitlab.wethinkco.de`), you'll write the same pipeline in GitLab's format. Here's the
**side-by-side translation** so nothing you learned is wasted:

| What you did on GitHub | The GitLab equivalent |
|------------------------|-----------------------|
| File: `.github/workflows/ci.yml` | File: `.gitlab-ci.yml` (repo root) |
| `on: push` / `pull_request` | `rules:` with `$CI_COMMIT_BRANCH` |
| `jobs:` → `steps:` | jobs → `script:` |
| `runs-on: ubuntu-latest` | `image: maven:3.9-eclipse-temurin-21` |
| `uses: actions/checkout` | (automatic — GitLab checks out for you) |
| `uses: actions/setup-java` | (baked into the `image:` you choose) |
| `needs: run-tests` (ordering) | `stages:` (ordered list) |
| `actions/upload-artifact` | `artifacts: paths:` |
| `docker build` (Docker preinstalled) | needs `services: [docker:dind]` |

### The same pipeline, in GitLab form:

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
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
    - if: '$CI_PIPELINE_SOURCE == "merge_request_event"'

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
    - docker build -t robot-world .
```

Notice: **exactly the same three stages**, same order, same idea. You already understand it —
you're just writing it in GitLab's dialect.

---

## 8.3 — The GitHub workflow for a real project

If your project *is* on GitHub, use the workflow from Step 7 with your names swapped:

```yaml
name: CI Pipeline
on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  run-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
      - run: mvn test

  build-jar:
    runs-on: ubuntu-latest
    needs: run-tests
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
      - run: mvn package -DskipTests
      - uses: actions/upload-artifact@v4
        with:
          name: app-jar
          path: target/*-jar-with-dependencies.jar

  build-docker-image:
    runs-on: ubuntu-latest
    needs: build-jar
    steps:
      - uses: actions/checkout@v4
      - run: docker build -t your-app .
```

---

## 8.4 — One thing that matters for tests in CI

If your real project has **tests that need a running server** (like acceptance tests that
connect over a socket), they must be **self-contained** to work in CI — because a runner has
no server running for them. The usual fix: each test starts its own server in a
`@BeforeEach` on a random port. If your tests already do that, `mvn test` works in the
pipeline with no changes. If they don't, that's the first thing to sort before CI can run them.

---

## 8.5 — Team checklist for the real thing

- [ ] Everyone completed this tutorial and understands each job
- [ ] Docker installed on everyone's machine (`docker run hello-world` works)
- [ ] Workflow/pipeline file added to the real repo, green on `main`
- [ ] Dockerfile added, image builds locally for everyone
- [ ] Each person can demo `docker build` + `docker run` from their own laptop

---

## 8.6 — What to say at the showcase

> "We didn't just copy a pipeline — we learned it on a practice repo first. Our pipeline
> runs three stages: it tests on every push to main, packages the jar, and builds a Docker
> image. Every one of us can build and run the container locally."

That demonstrates real understanding, which is what's being assessed.

---

🎉 **You're done.** You built a pipeline from nothing, understand every line, and can apply
it to any project — on GitHub or GitLab. Back to **[the README](../README.md)**.
