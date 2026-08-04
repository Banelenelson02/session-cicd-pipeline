# Step 8 — Apply It to Robot World

**Goal:** you've built a full pipeline on the toy app. Now map every piece back to the real
Robot World project. The concepts are identical — only names change.

---

## 8.1 — The translation table

| Tutorial (calculator) | Robot World (real) |
|-----------------------|--------------------|
| `mvn test` (5 tests) | `mvn test` (your 189 tests — they self-start a server, so this works in CI) |
| `mvn package` → calculator jar | `mvn package` → `robot-world-0.1.0-jar-with-dependencies.jar` |
| main class `com.tutorial.App` | main class `za.co.wethinkcode.robots.server.Server` |
| calculator Dockerfile | Robot World Dockerfile (already drafted) |
| `calculator-app` image | `robot-world` image |

Everything you learned transfers directly. Your Robot World `.gitlab-ci.yml` is the same
shape with these names swapped.

---

## 8.2 — Robot World's pipeline (what you'll commit there)

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

---

## 8.3 — Why `mvn test` works in Robot World's CI (important)

In this tutorial, the tests are simple unit tests. In Robot World, the **acceptance tests
need a server** — but your team already fixed this: each acceptance test starts its own
server in `@BeforeEach` on a random port (`--port 0`). That means `mvn test` is fully
self-contained and runs on a CI runner with no external server. **This is exactly why that
fix mattered** — without it, the pipeline could never run your acceptance tests.

---

## 8.4 — The Iteration 2 requirements, mapped

| Requirement | Where it's satisfied |
|-------------|----------------------|
| Pipeline runs on commits to `main` | the `rules:` block on `run-tests` |
| Use the build script | jobs call `mvn` (and can call your `make` targets) |
| Package the server | `build-jar` + `build-docker-image` stages |
| Docker containerisation | the `Dockerfile` + `package` stage |
| Everyone contributes to the build | split jobs/Dockerfile across pairs |
| Everyone demos Docker locally | `docker build` + `docker run` on each machine |

---

## 8.5 — Your team's checklist for the real thing

- [ ] Everyone completed this tutorial and understands each stage
- [ ] Docker installed on all four machines (`docker run hello-world` works)
- [ ] Runners confirmed active on the Robot World project
- [ ] `.gitlab-ci.yml` added to Robot World, pipeline green on `main`
- [ ] Dockerfile added, image builds locally for everyone
- [ ] Each person can demo `docker build` + `docker run` from their own laptop

---

## 8.6 — What to say at the showcase

> "We didn't just copy a pipeline — we learned it on a practice repo first. Our Robot World
> pipeline runs three stages: it tests on every push to main, packages the jar, and builds a
> Docker image. The acceptance tests run in CI because each one starts its own server, so the
> pipeline is fully self-contained. Every one of us can build and run the container locally."

That demonstrates real understanding, which is what's being assessed.

---

🎉 **You're done.** You built a pipeline from nothing, understand every line, and can apply
it to Robot World. Back to **[the README](../README.md)**.
