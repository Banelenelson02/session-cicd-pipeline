# Step 6 — Artifacts: Keeping What the Build Made

**Goal:** the `build-jar` job makes a jar, but it vanishes when the job ends. Artifacts let
you **save and download** it. This is how a pipeline produces something you can actually use.

---

## 6.1 — The problem

Each job runs in a fresh, throwaway container. When `build-jar` finishes, its container is
destroyed — and your shiny `target/*.jar` goes with it. If you want to keep the jar (to
download it, or hand it to the next stage), you must declare it an **artifact**.

---

## 6.2 — Save the jar

Update the `build-jar` job:

```yaml
build-jar:
  stage: build
  image: maven:3.9-eclipse-temurin-21
  script:
    - mvn package -DskipTests
  artifacts:
    paths:
      - target/*-jar-with-dependencies.jar
    expire_in: 1 week
```

- `artifacts.paths` — files to keep after the job ends
- `target/*-jar-with-dependencies.jar` — our runnable fat jar
- `expire_in` — GitLab auto-deletes it after a week so storage doesn't fill up

---

## 6.3 — Push and download it

```bash
git add .gitlab-ci.yml
git commit -m "save the jar as an artifact"
git push
```

After the pipeline runs: open the `build-jar` job. On the right you'll see a **Download**
button (job artifacts). Download it, and you have the exact jar the pipeline built — you
could run it with `java -jar`.

> **Why this matters:** a pipeline isn't just a pass/fail checker — it *produces build
> outputs*. Artifacts are how those outputs escape the throwaway container.

---

## 6.4 — Artifacts also pass between stages

Bonus: if a later stage needs the jar (e.g. to put it in a Docker image), it's automatically
available because it's an artifact. A `package` stage could pick up
`target/*-jar-with-dependencies.jar` without rebuilding it. We use this idea in the next step.

---

✅ **Done when:** you can download the built jar from the pipeline. Next:
**[Step 7 — Docker](07-docker.md)** — the last piece, building a container image in the pipeline.
