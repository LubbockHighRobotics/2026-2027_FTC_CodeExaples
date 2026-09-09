# FTC Java Starter

Working example code for FTC teams, from your first spinning motor to a
path-following autonomous with vision.

**New here? → [START-HERE.md](START-HERE.md)**

---

## What this is

A library of examples you copy **out of**. Every file is self-contained: open
it, copy it, change the config names at the top, run it.

This is **not** a fork of the FTC SDK. Get the SDK from
[FIRST-Tech-Challenge/FtcRobotController](https://github.com/FIRST-Tech-Challenge/FtcRobotController)
and copy examples from here into your own `TeamCode`.

---

## Where to go

| I'm using... | Folder |
|---|---|
| A Chromebook, or a locked-down school computer | [`onbot-java/`](onbot-java/) |
| Android Studio on a computer I can install on | [`android-studio/`](android-studio/) |
| Neither yet | [`docs/handouts/`](docs/handouts/) — the page for asking your school |

| I want... | Folder |
|---|---|
| To get the robot driving | `01-drive/` |
| To run a lift without freezing the robot | `02-mechanisms/` |
| An autonomous that scores | `03-auto-time/` then `04-auto-encoder/` |
| Smooth path-following autos | `05-auto-pedro/` |
| AprilTags and Limelight | `06-vision/` |
| Small pieces to paste into my own code | [`snippets/`](snippets/) |
| A blank file to start from | [`templates/`](templates/) |

---

## Do these four things first

1. **Get one motor spinning.** `01-drive/HelloMotor.java`. Nothing else matters
   until this works.
2. **Get the robot driving.** `01-drive/MecanumBothModes.java`.
3. **Put your code in Git.** Today, not in January. See `docs/setup/05-git-for-teams.md`.
4. **Write an autonomous.** `03-auto-time/AutoParkTime.java` is fifteen lines and
   scores every match. Teams leave this until December every year and it costs
   them matches all season.

---

## Config names used everywhere in this repo

Match these and every example runs without edits:

```
frontLeft   backLeft   frontRight   backRight
imu         lift       claw         intake
webcam1     limelight
```

Capitalization matters. `frontleft` is not `frontLeft`.

---

## The three rules every file here follows

1. **Never block the loop.** No `sleep()` in TeleOp, ever. See
   `02-mechanisms/LiftRunToPosition.java` for the wrong way and
   `LiftStateMachine.java` for the right one.
2. **Always have a fallback.** A vision auto that does nothing when it can't see
   is worse than a time-based auto that always parks.
3. **Consistency beats ambition.** A 10-point auto that works 12 times out of 12
   is worth more to your alliance than a 40-point auto that works twice.

---

## Something's broken?

- `docs/setup/99-troubleshooting.md` — symptom → cause
- Open an issue: the templates ask for what people need to help you
- `docs/how-to-ask-for-help.md` — how to ask anywhere and actually get an answer

## License

MIT. Copy it, change it, ship it. No attribution needed.
