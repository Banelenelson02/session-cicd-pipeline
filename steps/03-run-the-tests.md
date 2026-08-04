# Step 3 — Make the Pipeline Run Your Tests

**Goal:** replace the "hello" job with one that actually compiles the code and runs the
JUnit tests — automatically, on every push. This is the core of CI.

---

## 3.1 — The problem: the runner needs Java + Maven

Your `echo` worked because every runner can echo. But to run `mvn test`, the runner needs
**Maven and Java installed**. We don't want to install them by hand — instead we tell the
job to run **inside a Docker image** that already has them.

That's what the `image:` keyword does:

```yaml
run-tests:
  image: maven:3.9-eclipse-temurin-21    # a ready-made box with Maven + Java 21
  script:
    - mvn test
```

- `image:` — the runner starts a container from this image and runs your `script` inside it
- `maven:3.9-eclipse-temurin-21` — an official image with Maven 3.9 and Java 21 preinstalled

> **Why this is great:** you don't manage Java versions on the runner. The image guarantees
> the exact tools you need, every run, identical for everyone. (This is the same idea as
> Docker in Iteration 2 — reproducible environments.)

---

## 3.2 — Replace your pipeline

Change `.gitlab-ci.yml` to:

```yaml
# Compile and test the app automatically on every push.
run-tests:
  image: maven:3.9-eclipse-temurin-21
  script:
    - mvn test
```

Push it:
```bash
git add .gitlab-ci.yml
git commit -m "run tests in the pipeline"
git push
```

Go to **Build → Pipelines**, open the `run-tests` job, and watch the Maven output scroll by
— the same `Tests run: 5, Failures: 0` you saw locally, now running on the runner. ✅

---

## 3.3 — Make the pipeline only run on the right branches (optional but good)

Right now it runs on every push to every branch. Usually you want it on `main` and on merge
requests. Add a `rules:` or `only:` clause:

```yaml
run-tests:
  image: maven:3.9-eclipse-temurin-21
  script:
    - mvn test
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
    - if: '$CI_PIPELINE_SOURCE == "merge_request_event"'
```

- first rule: run when someone pushes to `main`
- second rule: run when someone opens/updates a merge request

> The Iteration 2 brief says the pipeline must "run when new commits are made to your main
> branch" — that first rule is exactly that requirement.

---

## 3.4 — Speed tip: cache Maven downloads (optional)

Maven re-downloads dependencies every run, which is slow. Cache them:

```yaml
run-tests:
  image: maven:3.9-eclipse-temurin-21
  cache:
    key: maven-repo
    paths:
      - .m2/repository
  variables:
    MAVEN_OPTS: "-Dmaven.repo.local=.m2/repository"
  script:
    - mvn test
```

Don't stress about this one — it's a nice-to-have. Understanding `image` and `script` is
what matters.

---

✅ **Done when:** pushing any change automatically runs your 5 tests on the runner and goes
green. **You now have Continuous Integration.** Next: **[Step 4 — Break it on purpose](04-break-it-on-purpose.md)**.
