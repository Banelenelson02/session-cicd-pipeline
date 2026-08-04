# 🚀 CI/CD Pipeline Tutorial — Learn by Building

A hands-on tutorial for our team to learn GitLab CI/CD **by doing**, not by reading theory.
By the end, you will have built a real pipeline that automatically tests, packages, and
containerizes an app every time you push — the exact skill Iteration 2 needs.

> **Why a separate practice repo?** So nobody breaks the real Robot World repo while
> learning. This uses a tiny calculator app. Once you understand the pipeline here,
> applying it to Robot World is easy.

> **⚠️ GitLab, not GitHub.** You may have seen "GitHub Actions" tutorials online. We are
> on **GitLab** (`gitlab.wethinkco.de`). Same idea, different file. GitLab uses one file
> called `.gitlab-ci.yml`. This tutorial teaches GitLab.

---

## What you'll understand at the end

- What a pipeline actually **is** (and what a "runner" is)
- How to make tests run **automatically** on every push
- How **stages** and **jobs** work, and how to chain them
- How to **package** a jar in the pipeline
- How to build a **Docker image** in the pipeline
- How to read a **failing** pipeline and fix it

---

## How to use this tutorial

Work through the steps **in order**. Each one is a small, working change you can see run.
Do them together as a team — one person drives, everyone watches the pipeline go green.

| Step | File | What you'll do |
|------|------|----------------|
| 0 | [steps/00-setup.md](steps/00-setup.md) | Get the repo onto GitLab, confirm it builds locally |
| 1 | [steps/01-what-is-a-pipeline.md](steps/01-what-is-a-pipeline.md) | Understand pipelines & runners (the only "theory", kept short) |
| 2 | [steps/02-first-pipeline.md](steps/02-first-pipeline.md) | Write your first `.gitlab-ci.yml` — one job that says hello |
| 3 | [steps/03-run-the-tests.md](steps/03-run-the-tests.md) | Make the pipeline compile & run the tests automatically |
| 4 | [steps/04-break-it-on-purpose.md](steps/04-break-it-on-purpose.md) | Break a test, watch it go RED, learn to read the failure |
| 5 | [steps/05-stages.md](steps/05-stages.md) | Add stages: test → build, chained in order |
| 6 | [steps/06-artifacts.md](steps/06-artifacts.md) | Save the built jar as a downloadable artifact |
| 7 | [steps/07-docker.md](steps/07-docker.md) | Build a Docker image in the pipeline |
| 8 | [steps/08-apply-to-robot-world.md](steps/08-apply-to-robot-world.md) | Map everything back to the real Robot World project |

---

## The app we're building around

A tiny calculator (`src/main/java/com/tutorial/`). Four methods, five tests. That's it —
the code is deliberately boring so all your attention goes on the **pipeline**.

Run it locally to prove it works before we automate anything:

```bash
mvn test        # runs the 5 tests
mvn package     # builds target/cicd-tutorial-1.0.0-jar-with-dependencies.jar
java -jar target/cicd-tutorial-1.0.0-jar-with-dependencies.jar
```

---

## The finished pipeline (where we're heading)

By Step 7 your `.gitlab-ci.yml` will look like this — don't paste it yet, we build it up
piece by piece so you understand every line:

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
    - mvn package
  artifacts:
    paths:
      - target/*-jar-with-dependencies.jar

build-docker-image:
  stage: package
  image: docker:latest
  services:
    - docker:dind
  script:
    - docker build -t calculator-app .
```

Start with **[Step 0](steps/00-setup.md)**. 🎯
