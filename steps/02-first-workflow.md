# Step 2 — Your First Workflow

**Goal:** write the smallest possible GitHub Actions workflow, push it, and watch it run. No
tests yet — just prove the machinery works.

---

## 2.1 — Create the file

Create a file at exactly this path (the folders matter):

```
.github/workflows/ci.yml
```

Put this inside it:

```yaml
# Our very first workflow. One job that just prints a message.
name: First Pipeline

on: push              # run this whenever anyone pushes

jobs:
  say-hello:
    runs-on: ubuntu-latest      # use a GitHub-hosted Ubuntu runner
    steps:
      - run: echo "Hello from the pipeline!"
      - run: echo "This ran on a GitHub runner, not my laptop."
```

That's a complete, valid workflow. Let's read it:

- `name:` — a label for the workflow, shown in the Actions tab
- `on: push` — the **trigger**: run every time someone pushes
- `jobs:` — the list of jobs
- `say-hello` — the **name** of our job (you choose this)
- `runs-on: ubuntu-latest` — which runner to use (a free GitHub Ubuntu machine)
- `steps:` — the list of things the job does
- each `- run:` — a shell command

---

## 2.2 — Push it and watch

```bash
git add .github/workflows/ci.yml
git commit -m "add first workflow"
git push
```

Now go to GitHub: **your repo → Actions tab**. You'll see a workflow run appear. Click into
it, click the `say-hello` job, and you'll see your echoed messages in the log.

🎉 **That's a pipeline.** A machine, somewhere, checked out your code and ran your commands
because you pushed.

---

## 2.3 — What just happened (the flow)

```
you: git push
        │
        ▼
GitHub sees .github/workflows/ci.yml
        │
        ▼
GitHub assigns the job to a hosted runner
        │
        ▼
runner runs your `steps` commands
        │
        ▼
✅ green tick if all steps succeed (exit 0)
❌ red X if any step fails (non-zero exit)
```

**The golden rule:** a job passes if every step exits with code 0. If any step errors, the
job goes red and stops.

---

## 2.4 — Try it yourself (learn by breaking)

Add a step that fails on purpose and watch it go red:

```yaml
    steps:
      - run: echo "about to fail on purpose"
      - run: exit 1               # this forces a failure
      - run: echo "you will NEVER see this line"
```

Push it. The run goes **red**, and notice the last echo never runs — the job stops at the
first failing step. Now remove the `exit 1` step and watch it go green again.

> **This is the entire feedback loop of CI/CD:** push → runner runs steps → green or red.
> Everything from here is just *better steps*.

---

✅ **Done when:** you've seen a run go green, made one go red on purpose, and understand it's
just running shell commands. Next: **[Step 3 — Run the tests](03-run-the-tests.md)**.
