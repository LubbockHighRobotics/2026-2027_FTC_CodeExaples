# Templates

Empty starting points. Every file here compiles as-is and does nothing.

**How to use one:** copy it into your project, rename the file, rename the
class to match the new filename exactly, change the `name =` in the
annotation, and start typing.

| File | Start here when |
|---|---|
| `BlankTeleOp.java` | Writing any driver-controlled OpMode |
| `BlankAuto.java` | Writing any autonomous |
| `BlankSubsystem.java` | Adding a mechanism once you're past two |
| `RobotHardware.java` | You're tired of retyping `hardwareMap.get` in every file |
| `robot-constants-worksheet.md` | Tuning day — fill it in and commit it |

`RobotHardware.java` is the one worth adopting early. One file holds every
hardware name, so a config change is a one-line fix instead of a hunt through
nine OpModes.
