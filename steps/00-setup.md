# Step 0 — Setup

**Goal:** get this repo onto GitLab and confirm it builds on your machine, so every later
step has a foundation.

---

## 0.1 — Confirm it builds locally first

Before any pipeline, make sure the app works on your own machine. From the repo folder:

```bash
mvn test
```
You should see `Tests run: 5, Failures: 0`. If that works, Maven and Java are set up right.

```bash
mvn package
```
This builds a runnable jar in `target/`. Run it:
```bash
java -jar target/cicd-tutorial-1.0.0-jar-with-dependencies.jar
```
You should see the calculator demo print. ✅

> **Why this matters:** a pipeline just runs these same commands on a server instead of
> your laptop. If they don't work locally, they won't work in the pipeline. Always get it
> green locally first.

---

## 0.2 — Put this repo on GitLab

1. On `gitlab.wethinkco.de`, create a **new blank project** (call it `cicd-tutorial`).
2. In this folder, point git at it and push:

```bash
git init
git add .
git commit -m "initial tutorial project"
git remote add origin git@gitlab.wethinkco.de:YOUR-USERNAME/cicd-tutorial.git
git branch -M main
git push -u origin main
```

Replace `YOUR-USERNAME` with your actual GitLab path.

---

## 0.3 — Check that runners exist ⚠️ (do this now, it's the #1 blocker)

A pipeline needs a **runner** — a machine that actually executes it. If your project has no
runner, your pipeline will sit "pending" forever and you'll think you did something wrong.

On GitLab: **your project → Settings → CI/CD → Runners**. You should see at least one
**active** runner (usually a shared "instance runner" provided by WeThinkCode).

- **Green/active runner listed?** → you're good, continue.
- **No runners?** → ask a coach whether shared runners are enabled for student projects.
  Sort this **before** Iteration 2, or the real pipeline won't run either.

---

## 0.4 — A `.gitignore` so you don't commit build junk

Create a file called `.gitignore` in the repo root:

```
target/
*.class
.idea/
*.iml
```

Commit it:
```bash
git add .gitignore
git commit -m "add gitignore"
git push
```

---

✅ **Done when:** the app builds locally, the repo is on GitLab, and you can see an active
runner. Next: **[Step 1 — What is a pipeline?](01-what-is-a-pipeline.md)**
