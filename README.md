# 🚀 CI/CD Pipeline Tutorial — Learn by Building (GitHub Actions)

A hands-on tutorial for our team to learn CI/CD **by doing**, not by reading theory.
By the end, you'll have built a real **GitHub Actions** pipeline that automatically tests,
packages, and containerizes an app on every push.

> **Why a separate practice repo?** So nobody breaks the real project while learning. This
> uses a tiny calculator app. Once you understand the pipeline here, applying it anywhere
> is easy.

> **📝 Note on GitHub vs GitLab.** This tutorial uses **GitHub Actions**. If your graded
> project lives on **GitLab**, the *ideas* are identical — only the file and a few keywords
> differ. The final step ([Step 8](steps/08-apply-to-your-project.md)) gives you the GitLab
> translation so nothing is lost.

---

## What you'll understand at the end

- What a pipeline (a "workflow") actually **is**, and what a **runner** is
- How to make tests run **automatically** on every push
- How **jobs** and **steps** work, and how to chain jobs in order with `needs`
- How to **package** a jar in the pipeline and save it as an artifact
- How to build a **Docker image** in the pipeline
- How to read a **failing** run and fix it

---

## How to use this tutorial

Work through the steps **in order**. Each one is a small, working change you can watch run.
Do it together as a team — one person drives, everyone watches the run go green.

| Step | File | What you'll do |
|------|------|----------------|
| 0 | [steps/00-setup.md](steps/00-setup.md) | Get the repo onto GitHub, confirm it builds locally |
| 1 | [steps/01-what-is-a-pipeline.md](steps/01-what-is-a-pipeline.md) | Understand workflows, jobs, steps & runners (short) |
| 2 | [steps/02-first-workflow.md](steps/02-first-workflow.md) | Write your first workflow — one job that says hello |
| 3 | [steps/03-run-the-tests.md](steps/03-run-the-tests.md) | Make the workflow compile & run the tests automatically |
| 4 | [steps/04-break-it-on-purpose.md](steps/04-break-it-on-purpose.md) | Break a test, watch it go RED, learn to read the failure |
| 5 | [steps/05-jobs-and-needs.md](steps/05-jobs-and-needs.md) | Chain jobs in order: test → build with `needs` |
| 6 | [steps/06-artifacts.md](steps/06-artifacts.md) | Save the built jar as a downloadable artifact |
| 7 | [steps/07-docker.md](steps/07-docker.md) | Build a Docker image in the pipeline |
| 8 | [steps/08-apply-to-your-project.md](steps/08-apply-to-your-project.md) | Map it back to a real project (GitHub **and** GitLab) |

---

## The app we're building around

A tiny calculator (`src/main/java/com/tutorial/`). Four methods, five tests. The code is
deliberately boring so all your attention goes on the **pipeline**.

Run it locally to prove it works before we automate anything:

```bash
mvn test        # runs the 5 tests
mvn package     # builds target/cicd-tutorial-1.0.0-jar-with-dependencies.jar
java -jar target/cicd-tutorial-1.0.0-jar-with-dependencies.jar
```

---

## The finished workflow (where we're heading)

By Step 7 your `.github/workflows/ci.yml` will look like this — don't paste it yet, we build
it up piece by piece so you understand every line:

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
      - run: docker build -t calculator-app .
```

Start with **[Step 0](steps/00-setup.md)**. 🎯
