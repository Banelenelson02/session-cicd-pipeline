# Step 2 — Your First Pipeline

**Goal:** write the smallest possible `.gitlab-ci.yml`, push it, and watch it run. No tests
yet — just prove the machinery works.

---

## 2.1 — Create the file

In the **root** of the repo (same folder as `pom.xml`), create a file named exactly:

```
.gitlab-ci.yml
```

Put this inside it:

```yaml
# Our very first pipeline. One job that just prints a message.
say-hello:
  script:
    - echo "Hello from the pipeline!"
    - echo "This ran on a GitLab runner, not my laptop."
```

That's a complete, valid pipeline. Let's read it:

- `say-hello` — the **name** of our job (you choose this; any name works)
- `script:` — the list of shell commands the job runs
- the two `echo` lines — just print text

---

## 2.2 — Push it and watch

```bash
git add .gitlab-ci.yml
git commit -m "add first pipeline"
git push
```

Now go to GitLab: **your project → Build → Pipelines**. You'll see a pipeline appear. Click
into it, click the `say-hello` job, and you'll see your echoed messages in the log.

🎉 **That's a pipeline.** A machine, somewhere, checked out your code and ran your commands
because you pushed.

---

## 2.3 — What just happened (the flow)

```
you: git push
        │
        ▼
GitLab sees .gitlab-ci.yml
        │
        ▼
GitLab hands the job to a runner
        │
        ▼
runner runs your `script:` commands
        │
        ▼
✅ green tick if all commands succeed (exit 0)
❌ red X if any command fails (non-zero exit)
```

**The golden rule:** a job passes if every command in `script:` exits with code 0. If any
command errors, the job goes red and stops.

---

## 2.4 — Try it yourself (learn by breaking)

Change the job to run a command that fails, and watch it go red:

```yaml
say-hello:
  script:
    - echo "about to fail on purpose"
    - exit 1          # this forces a failure
    - echo "you will NEVER see this line"
```

Push it. The pipeline goes **red**, and notice the last echo never runs — the job stops at
the first failing command. Now change `exit 1` back to a real command and watch it go green
again.

> **This is the entire feedback loop of CI/CD:** push → runner runs commands → green or red.
> Everything from here is just *better commands* in `script:`.

---

✅ **Done when:** you've seen a pipeline go green, and made one go red on purpose, and you
understand it's just running shell commands. Next: **[Step 3 — Run the tests](03-run-the-tests.md)**.
