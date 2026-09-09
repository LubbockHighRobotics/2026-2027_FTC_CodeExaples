# 07 — Command-based, without the framework

## When this is worth it

You have **three or more mechanisms** and `TwoMechanismStateMachines.java` has
started to hurt — one giant `runOpMode` with four switch blocks in it, and
every change risks breaking something unrelated.

## When it isn't

A drivetrain and one intake. Don't do this yet. A hand-written state machine is
shorter, clearer, and easier to debug.

## What's in here

These files use **no outside libraries**. That's deliberate — you can read them,
they compile in a plain SDK project, and they show you the actual pattern
instead of hiding it behind a framework:

- `LiftSubsystem.java` — the same lift from `02-mechanisms`, as a subsystem
- `DriveSubsystem.java` — mecanum, robot- and field-centric, as a subsystem
- `CommandAutoExample.java` — a TeleOp and an auto that use both

Diff `LiftSubsystem.java` against `02-mechanisms/LiftStateMachine.java`. The
logic is identical. What changed is where it lives.

## The pattern

1. Each mechanism is a class with `init()`, `update()`, and public methods that
   *request* things (`scoreHigh()`), never methods that *wait* for things.
2. `update()` is called once per loop, for every subsystem, always.
3. Your OpMode reads the gamepad and calls request methods. It never touches a
   motor directly.

## Real frameworks

Once you want a scheduler, command groups, and default commands, use a
maintained library rather than growing this yourself:

- **NextFTC** — `nextftc.dev`. Actively developed, has a Pedro Pathing integration.
- **SolversLib** — the maintained continuation of FTCLib.

**FTCLib itself is no longer actively maintained.** You'll find a lot of old
tutorials for it. Skip them — the concepts transfer, the imports don't.
