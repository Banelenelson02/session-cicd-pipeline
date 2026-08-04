# Step 0 — Setup

**Goal:** get this repo onto GitHub and confirm it builds on your machine, so every later
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

## 0.2 — Put this repo on GitHub

1. On GitHub, create a **new empty repository** (call it `cicd-tutorial`). Don't add a
   README — this repo already has one.
2. In this folder, point git at it and push:

```bash
git init
git add .
git commit -m "initial tutorial project"
git remote add origin https://github.com/YOUR-USERNAME/cicd-tutorial.git
git branch -M main
git push -u origin main
```

Replace `YOUR-USERNAME` with your actual GitHub username.

---

## 0.3 — Runners: the good news on GitHub

A pipeline needs a **runner** — a machine that executes it. On GitHub, this is easy:
**GitHub gives you free hosted runners automatically** (Ubuntu, Windows, Mac). You don't set
anything up. When you push a workflow, GitHub finds a runner for you.

> On GitLab you'd have to check that a runner is enabled. On GitHub public repos, it's
> handled for you. One less thing to worry about while learning.

To confirm Actions is enabled: **your repo → Settings → Actions → General** → make sure
"Allow all actions" is selected (it usually is by default).

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

✅ **Done when:** the app builds locally, the repo is on GitHub, and Actions is enabled.
Next: **[Step 1 — What is a pipeline?](01-what-is-a-pipeline.md)**
