# OnBot Java

Real Java, typed in a browser, compiled on the Control Hub. **No install, no
admin rights, works on a Chromebook.** This is a legitimate full-season answer.

## The one constraint

**No outside libraries.** Nothing in this folder imports Pedro Pathing,
NextFTC, or anything that isn't in the FTC SDK. Every file here is
self-contained.

That means `05-auto-pedro/` and `07-command-based/` in the Android Studio
folder won't work here. Everything else will — including AprilTags, which
people assume needs Android Studio. It doesn't.

## How to use these files

1. Power the Control Hub and join its Wi-Fi.
2. Browse to `192.168.43.1:8080` → **OnBot Java**.
3. Click **+** to make a new OpMode. **Name it exactly the same as the class
   name** in the file you're copying — `MecanumBothModes`, not `mydrive`.
   OnBot rejects a mismatch.
4. Open the `.java` file here on GitHub, click **Copy raw file**.
5. Paste over everything in the OnBot editor.
6. Change the config names marked `EDIT`.
7. Click the wrench/build button. Watch for green.

## The folders

| Folder | Start when |
|---|---|
| `01-drive/` | Always. `HelloMotor.java` first. |
| `02-mechanisms/` | The robot drives and you've added an arm or a lift |
| `03-auto/` | The robot drives. Don't wait for anything else. |
| `04-vision/` | Your auto already works consistently without vision |

## Two things that bite everyone

- **Class name must match the OpMode name you created.** Most common paste error.
- **Config names are case-sensitive.** `frontleft` is not `frontLeft`.

## When to move to Android Studio

When you actually need something OnBot can't do — path following, a command
framework, or Git. Not before. Working code in OnBot beats a stalled Android
Studio install every time.
