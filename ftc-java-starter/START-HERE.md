# Start here

Answer three questions and you'll know exactly which folder to open.

---

## 1. What computer can you use?

**A Chromebook, or a school laptop you can't install anything on**
→ Use **OnBot Java**. It runs in a browser, needs no install, and is a
legitimate full-season answer. Teams have won on it.
→ Go to [`docs/setup/01-onbot-java.md`](docs/setup/01-onbot-java.md), then
[`onbot-java/`](onbot-java/).
→ Also read [`docs/handouts/01-asking-your-school-for-a-computer.pdf`](docs/handouts/)
and start that conversation this week. You don't have to wait for it to finish.

**A Windows or Mac laptop you can install on**
→ Use **Android Studio**. You get Git, outside libraries, and a debugger.
→ Go to [`docs/setup/02-android-studio.md`](docs/setup/02-android-studio.md), then
[`android-studio/`](android-studio/).

**Nothing at all**
→ [`docs/handouts/01-asking-your-school-for-a-computer.pdf`](docs/handouts/)
has the script, who to ask, and the spec sheet. Meanwhile, OnBot Java only
needs a browser — even a phone or a library computer gets you started.

---

## 2. Does your robot drive yet?

**No** → `01-drive/HelloMotor.java`, then `01-drive/MecanumBothModes.java`.
Stop there. Get that solid before anything else.

**Yes** → skip to question 3.

---

## 3. Do you have an autonomous that scores?

**No** → `03-auto-time/AutoParkTime.java`. Fifteen lines. Do this before you
build another mechanism. Run it ten times in a row; if it works ten out of ten,
it's done.

**Yes, a time-based one** → `04-auto-encoder/AutoParkEncoder.java`. Measured
distances instead of guessed times, so a low battery doesn't ruin it.

**Yes, and it's consistent** → now you've earned `05-auto-pedro/` and
`06-vision/`. Not before.

---

## The order, if you just want a list

```
1. HelloMotor                     -> one motor spins
2. MecanumBothModes               -> robot drives
3. Git set up                     -> code can't be lost
4. AutoParkTime                   -> auto scores, 10 for 10
5. LiftStateMachine               -> mechanism without freezing the robot
6. AutoParkEncoder                -> auto survives a low battery
7. Driver practice                -> more points than any library
8. Pedro / vision                 -> only once everything above is boring
```

Steps 1–4 are two weeks of work for a rookie team, and they're worth more
than everything below them combined.
