# Step 6 — Artifacts: Keeping What the Build Made

**Goal:** the `build-jar` job makes a jar, but it vanishes when the job ends. Artifacts let
you **save and download** it. This is how a pipeline produces something you can actually use.

---

## 6.1 — The problem

Each job runs on a fresh, throwaway runner. When `build-jar` finishes, its runner is wiped —
and your shiny `target/*.jar` goes with it. To keep the jar (to download it, or hand it to
another job), you upload it as an **artifact** using the `actions/upload-artifact` action.

---

## 6.2 — Save the jar

Add an upload step to the `build-jar` job:

```yaml
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
```

- `actions/upload-artifact@v4` — the official action for saving build outputs
- `name: app-jar` — what the download will be called
- `path:` — which files to keep (our runnable fat jar)

---

## 6.3 — Push and download it

```bash
git add .github/workflows/ci.yml
git commit -m "upload the jar as an artifact"
git push
```

After the run: open the workflow run in the **Actions** tab. Scroll to the bottom — there's
an **Artifacts** section with `app-jar`. Download it, unzip, and you have the exact jar the
pipeline built. You could run it with `java -jar`.

> **Why this matters:** a pipeline isn't just a pass/fail checker — it *produces build
> outputs*. Artifacts are how those outputs escape the throwaway runner.

---

## 6.4 — Passing an artifact to another job (optional)

If a later job needs the jar, it can pull it down with the matching **download** action:

```yaml
      - uses: actions/download-artifact@v4
        with:
          name: app-jar
```

That's how you'd hand the jar to a deploy job without rebuilding it. (Our Docker step in the
next lesson rebuilds inside the image instead, which is also fine — either approach works.)

---

✅ **Done when:** you can download the built jar from the Actions run. Next:
**[Step 7 — Docker](07-docker.md)** — the last piece, building a container image in the pipeline.
