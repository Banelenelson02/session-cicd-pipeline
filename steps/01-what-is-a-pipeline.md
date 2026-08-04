# Step 1 — What is a Pipeline? (the only theory, kept short)

We promised learning by doing, so this is the *only* concept page. Five minutes, then we build.

---

## The one-sentence version

> A **pipeline** (GitHub calls it a **workflow**) is a robot that runs your build commands
> automatically, on a server, every time you push code.

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

## Five words you need (GitHub terms)

| Word | Plain meaning |
|------|---------------|
| **Workflow** | The whole conveyor belt — one `.yml` file describing everything |
| **Job** | One station — a unit of work that runs on a runner (e.g. "run the tests") |
| **Step** | A single action inside a job (one command, or one reusable action) |
| **Runner** | The machine that does the work. GitHub gives you free hosted ones. |
| **Action** | A reusable, pre-made step you pull in with `uses:` (e.g. "checkout the code") |

---

## How GitHub knows what to do

You put a workflow file in this special folder:

```
.github/workflows/ci.yml
```

GitHub watches that folder. When you push, it reads any workflow files there and runs the
jobs you defined. No file = no pipeline. That's the entire trigger mechanism.

> The filename (`ci.yml`) can be anything ending in `.yml` — it just has to live in
> `.github/workflows/`.

---

## Two GitHub ideas that GitLab doesn't have

1. **`uses:` actions.** GitHub has a marketplace of reusable steps. Instead of writing
   scripts to check out code or install Java, you "use" a ready-made action:
   `uses: actions/checkout@v4`. Think of them as importing a helper.
2. **Jobs run in parallel by default.** Unlike GitLab's ordered "stages," GitHub jobs all
   start at once — *unless* you say one `needs:` another. You'll use `needs` in Step 5 to
   force test → build order.

---

## If you've seen GitLab tutorials

Same concepts, different names:

| Concept | GitHub (us) | GitLab |
|---------|-------------|--------|
| The file | `.github/workflows/*.yml` | `.gitlab-ci.yml` (repo root) |
| A unit of work | job → steps | job → script |
| Ordering | `needs:` | `stages:` |
| Reusable step | `uses: some/action` | (write the shell command) |
| Worker | runner (GitHub-hosted) | runner |

---

✅ **Done when:** you can explain "workflow", "job", "step", "runner", and "action" to each
other in one sentence each. Next: **[Step 2 — Your first workflow](02-first-workflow.md)** —
we write actual YAML now.
