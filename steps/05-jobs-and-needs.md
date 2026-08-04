# Step 5 — Jobs and `needs`: Chaining in Order

**Goal:** add a second job (build the jar) that only runs **after** tests pass. On GitHub,
jobs run in **parallel** by default — you use `needs:` to force an order.

---

## 5.1 — The idea

Right now you have one job. Real pipelines have several:

```
[ run-tests ] ──▶ [ build-jar ]
```

You don't want to build a jar if the tests failed — that's wasted work. But here's the
GitHub catch: **if you just add a second job, both run at the same time.** To make
`build-jar` wait for `run-tests`, you add one line: `needs: run-tests`.

> This is the biggest difference from GitLab. GitLab uses ordered `stages`; GitHub runs jobs
> in parallel and you wire up order with `needs`.

---

## 5.2 — Add the second job

Update `.github/workflows/ci.yml`:

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
    needs: run-tests             # ⬅ wait for run-tests to pass first
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
      - run: mvn package -DskipTests    # skip tests here; run-tests already did them
```

The key line is `needs: run-tests` on the second job. That single line means: "don't start
`build-jar` until `run-tests` succeeds."

---

## 5.3 — Push and watch the graph

```bash
git add .github/workflows/ci.yml
git commit -m "add build-jar job that needs run-tests"
git push
```

Open the run in the **Actions** tab. GitHub draws a little **graph**: `run-tests` → `build-jar`.
`build-jar` won't start until `run-tests` is green.

---

## 5.4 — Prove the ordering (optional experiment)

Break a test again and push. Watch: `run-tests` goes red, and `build-jar` shows as
**skipped** — it never ran, because its `needs` dependency failed. Fix the test, push, and
both run in order. That's `needs` doing its job: no wasted build on broken code.

---

## 5.5 — Why each job repeats checkout + setup-java

Notice `build-jar` also has `checkout` and `setup-java`. That's because **each job runs on a
fresh, separate runner** — `build-jar` doesn't inherit anything from `run-tests`. Every job
sets up its own environment from scratch. (If you need to pass files between jobs, you use
artifacts — that's the next step.)

---

✅ **Done when:** your workflow shows two jobs in a graph, and build only runs after test
passes. Next: **[Step 6 — Artifacts](06-artifacts.md)** — keeping the jar the build made.
