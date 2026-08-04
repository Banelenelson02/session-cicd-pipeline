# Step 5 — Stages: Chaining Jobs in Order

**Goal:** add a second job (build the jar) that only runs **after** tests pass. This
introduces **stages** — the way you order work on the conveyor belt.

---

## 5.1 — The idea

Right now you have one job. Real pipelines have several, in order:

```
[ test ] ──▶ [ build ] ──▶ [ package ]
```

You don't want to build a jar if the tests failed — that's wasted work. **Stages** enforce
this order: all `test` jobs run first; only if they all pass do `build` jobs start.

---

## 5.2 — Declare stages and assign jobs

Update `.gitlab-ci.yml`:

```yaml
# Declare the order of stages up front.
stages:
  - test
  - build

run-tests:
  stage: test
  image: maven:3.9-eclipse-temurin-21
  script:
    - mvn test

build-jar:
  stage: build
  image: maven:3.9-eclipse-temurin-21
  script:
    - mvn package -DskipTests    # skip tests here; the test stage already ran them
```

What changed:
- `stages:` at the top lists the order — `test`, then `build`
- each job now has a `stage:` line saying which stage it belongs to
- `build-jar` is in the `build` stage, so it runs **only after** `run-tests` passes

---

## 5.3 — Push and watch the belt

```bash
git add .gitlab-ci.yml
git commit -m "add build stage after test"
git push
```

Open the pipeline. You'll now see **two stages** drawn as columns: `test` then `build`. The
`build-jar` job won't start until `run-tests` is green. If you broke a test (Step 4), the
build stage would never even run — the belt stops at test.

---

## 5.4 — Prove the ordering (optional experiment)

Break a test again and push. Watch: `run-tests` goes red, and `build-jar` shows as
**skipped** — it never ran, because the stage before it failed. Fix the test, push, and both
go green in order. That's stages doing their job: no wasted build on broken code.

---

## 5.5 — Why `-DskipTests` in the build job?

The `test` stage already ran the tests. Running them again in `build` wastes time. So
`build-jar` just packages: `mvn package -DskipTests`. Tests live in the test stage; building
lives in the build stage. One responsibility each.

---

✅ **Done when:** your pipeline shows two ordered stages, and build only runs after test
passes. Next: **[Step 6 — Artifacts](06-artifacts.md)** — keeping the jar the build made.
