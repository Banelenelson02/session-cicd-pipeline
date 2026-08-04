# Step 3 — Make the Workflow Run Your Tests

**Goal:** replace the "hello" job with one that actually checks out the code, installs Java,
and runs the JUnit tests — automatically, on every push. This is the core of CI.

---

## 3.1 — The problem: the runner is empty

Your `echo` worked because every runner can echo. But to run `mvn test`, the runner needs:
1. **your code** on it (the runner starts empty!)
2. **Java** installed

On GitHub you solve both with pre-made **actions** — reusable steps you pull in with `uses:`.

```yaml
    steps:
      - uses: actions/checkout@v4        # step 1: put my code on the runner
      - uses: actions/setup-java@v4      # step 2: install Java
        with:
          distribution: temurin
          java-version: '21'
      - run: mvn test                    # step 3: now run the tests
```

- `actions/checkout@v4` — the official action that clones your repo onto the runner. **Almost
  every workflow starts with this.** Without it, the runner has no code.
- `actions/setup-java@v4` — installs the Java version you ask for under `with:`
- `mvn test` — now that code + Java are present, this works

> **Why actions?** On GitLab you'd pick a Maven Docker image that already has Java. On GitHub
> you start from a bare Ubuntu runner and *add* what you need with actions. Different route,
> same destination.

---

## 3.2 — Replace your workflow

Change `.github/workflows/ci.yml` to:

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
```

Note the improved `on:` block — it now runs on pushes to `main` **and** on pull requests
targeting `main`. That's the standard setup.

Push it:
```bash
git add .github/workflows/ci.yml
git commit -m "run tests in the pipeline"
git push
```

Open the **Actions** tab, click into `run-tests`, and watch the Maven output — the same
`Tests run: 5, Failures: 0` you saw locally, now on the runner. ✅

---

## 3.3 — Speed tip: cache Maven downloads (optional)

`setup-java` can cache Maven dependencies for you — just add `cache: maven`:

```yaml
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
          cache: maven
```

Nice-to-have. Understanding `checkout`, `setup-java`, and `run` is what matters.

---

✅ **Done when:** pushing any change automatically runs your 5 tests on the runner and goes
green. **You now have Continuous Integration.** Next: **[Step 4 — Break it on purpose](04-break-it-on-purpose.md)**.
