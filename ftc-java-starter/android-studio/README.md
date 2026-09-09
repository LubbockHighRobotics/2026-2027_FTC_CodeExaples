# Android Studio

Full project on your own machine: Git, outside libraries, a real editor, a
debugger.

## Where these files go

Everything under `teamcode/` maps into **one** folder in your project:

```
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
```

The numbered folders here (`01-drive`, `02-mechanisms`, …) are for **browsing
this repo**. When you copy a file into your project, drop it straight into
`teamcode/`. Every file already declares:

```java
package org.firstinspires.ftc.teamcode;
```

If you'd rather keep subfolders in your own project, that's fine — just make
the `package` line match the folder path, or Android Studio will complain.

## Before anything else

1. Install Android Studio and get the FTC SDK — `docs/setup/02-android-studio.md`
2. Let the first Gradle sync finish. It's slow once and fast forever after.
3. Set up Git — `docs/setup/05-git-for-teams.md`

## The folders

| Folder | What's in it | Needs |
|---|---|---|
| `01-drive/` | Hello motor, mecanum, field-centric | 4 motors, IMU |
| `02-mechanisms/` | State machines, encoder/servo finders, loop timing | a lift + claw |
| `03-auto-time/` | Rung one — timed autos | 4 motors |
| `04-auto-encoder/` | Rung two — measured autos, turn helper | encoders + IMU |
| `05-auto-pedro/` | Rung three — path following | Pedro installed + tuned, a localizer |
| `06-vision/` | AprilTags, drive-to-tag, Limelight 3A | webcam or Limelight |
| `07-command-based/` | Subsystem pattern, no external library | — |

Work through them in order. The numbers are the order.

## Adding an outside library

Only `05-auto-pedro/` needs one. Its files have a version note at the top —
Pedro's package names have moved between releases, so if an import is red,
delete it and let Alt+Enter re-import rather than fighting it.
