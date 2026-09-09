# Ticks per inch, per cm, per degree

## The only method that actually works

Don't trust a spec sheet you found on a forum. Measure it:

1. Run `02-mechanisms/EncoderReader.java`.
2. Press A to zero the encoders.
3. **Push the robot by hand** in a straight line exactly 100 cm (or 40 in).
4. Read the `frontLeft` number.
5. `TICKS_PER_CM = ticks / 100`

Do it three times and average. If the three runs disagree by more than a
few percent, your wheels are slipping and no auto above rung one will work
until you fix that.

## Or calculate it

```
TICKS_PER_CM = TICKS_PER_REV / (WHEEL_DIAMETER_CM * pi)
```

Calculated values are a starting point. Measured values are the truth —
gear lash, carpet compression and tyre squish all live in the difference.

## Common motor encoder counts (at the output shaft)

| Motor | Ticks per revolution |
|---|---|
| goBILDA 5202/5203, 1150 RPM | 145.1 |
| goBILDA 5202/5203, 435 RPM | 384.5 |
| goBILDA 5202/5203, 312 RPM | 537.7 |
| goBILDA 5202/5203, 223 RPM | 751.8 |
| goBILDA 5202/5203, 117 RPM | 1425.1 |
| REV HD Hex 20:1 | 537.6 |
| REV HD Hex 40:1 | 1075.2 |
| REV Core Hex | 288 |
| AndyMark NeveRest 20 | 537.6 |

Check your own motor's label. These change with gear ratio, and a team that
swapped a gearbox and forgot to update this number loses a Saturday to it.

## Common wheels

| Wheel | Diameter |
|---|---|
| goBILDA 96 mm mecanum | 9.6 cm / 3.78 in |
| goBILDA 104 mm mecanum | 10.4 cm / 4.09 in |
| REV 90 mm mecanum | 9.0 cm / 3.54 in |

## Ticks per degree of turn

```
TICKS_PER_DEGREE = (TRACK_WIDTH_CM * pi / 360) * TICKS_PER_CM
```

Where `TRACK_WIDTH_CM` is the distance between the left and right wheel
centres. This is a rough starting number only — mecanum wheels scrub badly
when turning, so use the IMU (`04-auto-encoder/TurnToHeading.java`) instead
of encoder ticks for any turn you care about.
