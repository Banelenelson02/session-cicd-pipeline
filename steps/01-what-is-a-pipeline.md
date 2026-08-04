# Step 1 — What is a Pipeline? (the only theory, kept short)

We promised learning by doing, so this is the *only* concept page. Five minutes, then we build.

---

## The one-sentence version

> A **pipeline** is a robot that runs your build commands automatically, on a server,
> every time you push code.

That's it. Everything else is detail.

---

## The analogy

Imagine a **conveyor belt** at your repo's door. Every time someone pushes code, the code
rides the belt through a series of stations:

```
   push code ──▶ [ TEST ] ──▶ [ BUILD ] ──▶ [ PACKAGE ] ──▶ ✅ done
                    │
                    └─ if tests fail, the belt STOPS here ❌ and tells the team
```

Each **station** is a job. The belt only moves to the next station if the current one
passes. That's the whole model.

---

## Four words you need (and nothing more)

| Word | Plain meaning |
|------|---------------|
| **Pipeline** | The whole conveyor belt — all the stations together |
| **Job** | One station — one unit of work (e.g. "run the tests") |
| **Stage** | A group of jobs that run at the same point on the belt (e.g. all "test" jobs) |
| **Runner** | The actual machine that does the work. GitLab sends your job to a runner. |

---

## How GitLab knows what to do

You put **one file** in your repo root:

```
.gitlab-ci.yml
```

GitLab watches for this file. When you push, it reads the file and runs whatever jobs you
defined. No file = no pipeline. That's the entire trigger mechanism.

---

## GitLab vs GitHub (so online tutorials don't confuse you)

You'll find lots of "CI/CD" tutorials online using **GitHub Actions**. Same concept,
different vendor:

| | GitHub | GitLab (us) |
|---|--------|-------------|
| File location | `.github/workflows/*.yml` | `.gitlab-ci.yml` (repo root) |
| Called | "Actions" | "CI/CD" / "pipelines" |
| Worker | "runner" | "runner" |

The *ideas* are identical. We use GitLab because that's where Robot World lives. If you see
`on: push` and `jobs:` and `runs-on` — that's GitHub syntax, mentally translate it.

---

✅ **Done when:** you can explain "pipeline", "job", "stage", and "runner" to each other in
one sentence each. Next: **[Step 2 — Your first pipeline](02-first-pipeline.md)** — we write
actual YAML now.
