# Pedro tuning notes

Do these **in order**. Later steps assume the earlier ones are correct, so
skipping ahead wastes the whole meeting. Budget one uninterrupted meeting.

Open **Panels** in a browser on the hub's Wi-Fi before you start. If you can't
see the robot's drawn position, you're tuning blind.

---

## Before you start

- [ ] Pedro and Panels sync without errors
- [ ] Motor names and directions set (`PedroConstants.java`)
- [ ] Localizer chosen: drive encoders / dead wheels / Pinpoint / OTOS
- [ ] Robot weighed, mass entered
- [ ] A flat measured space, ideally a real field tile surface

---

## The order

| # | Step | You're done when | Write it here |
|---|---|---|---|
| 1 | Localizer test | Push the robot 4 ft by hand; Panels shows 4 ft | |
| 2 | Forward multiplier | Commanded distance matches measured distance | `FORWARD =` |
| 3 | Lateral multiplier | Same, strafing sideways | `LATERAL =` |
| 4 | Turn multiplier | 90° command produces a 90° turn | `TURN =` |
| 5 | Translational PID | Robot holds position when pushed | `P= I= D=` |
| 6 | Heading PID | Robot holds heading when twisted | `P= I= D=` |
| 7 | Drive PID | Straight line runs are smooth, no oscillation | `P= I= D=` |

---

## What good looks like

- **Overshoots and comes back** → P too high, or D too low
- **Stops short every time** → P too low
- **Oscillates side to side** → D too low, or you skipped step 2
- **Drawn pose drifts from the real robot** → stop tuning, fix localization first

---

## After tuning

Copy every number into `PedroConstants.java` and **commit it**. Then re-run
step 1 with a low battery. If the numbers still hold, you're done.
