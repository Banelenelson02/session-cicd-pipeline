# Step 4 — Break It On Purpose

**Goal:** feel *why* CI is worth it. You'll break a test, watch the pipeline catch it and go
red, then read the failure. This is the whole point of a pipeline — catching bad code before
it spreads.

---

## 4.1 — Break the code

Open `src/main/java/com/tutorial/Calculator.java` and sabotage `add`:

```java
public int add(int a, int b) {
    return a - b;   // BUG: minus instead of plus
}
```

Commit and push:
```bash
git add .
git commit -m "oops, broke add() on purpose"
git push
```

---

## 4.2 — Watch it go red

Go to **Build → Pipelines**. The pipeline runs, and this time it turns **red** ❌.

Open the `run-tests` job log and scroll to the failure. You'll see something like:

```
[ERROR] Tests run: 5, Failures: 1
[ERROR] CalculatorTest.addWorks:16 expected: <5> but was: <-1>
```

The pipeline caught your bug **automatically**, without anyone remembering to test. If a
teammate had pulled `main` right now, CI would have warned everyone it was broken.

> **This is the value in one sentence:** broken code can't quietly reach the team, because
> the pipeline goes red the moment tests fail.

---

## 4.3 — Read the failure like a pro

The important line is:
```
CalculatorTest.addWorks:16 expected: <5> but was: <-1>
```

- `CalculatorTest.addWorks` — which test failed
- `:16` — the line in the test
- `expected: <5> but was: <-1>` — it wanted 5, got -1

That tells you exactly what's wrong: `add(2,3)` returned -1 instead of 5. Straight to the bug.

---

## 4.4 — Fix it and go green again

Put `add` back:
```java
public int add(int a, int b) {
    return a + b;   // fixed
}
```

Push:
```bash
git add .
git commit -m "fix add()"
git push
```

Pipeline goes **green** ✅. That red → fix → green loop is what you'll live in during
Iteration 2.

---

## 4.5 — Team reflection (talk about this)

- What would have happened *without* the pipeline? (Someone pulls broken `main`, wastes an
  hour wondering why their build fails.)
- How is this better than "remember to run the tests"? (It's not optional — the machine
  always runs them.)
- This is why the Iteration 2 brief wants the pipeline on `main`: so `main` is always known-good.

---

✅ **Done when:** you've seen the pipeline catch a real bug and you can read a failure log.
Next: **[Step 5 — Stages](05-stages.md)** — chaining multiple jobs in order.
