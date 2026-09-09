// FRAGMENT - not an OpMode. Paste this into your own file.

// Limits how fast a power value is allowed to change.
//
// Stops the drivetrain jerking when a driver snaps the stick, which is
// easier on the gearboxes and much easier to drive precisely. Also useful
// on an arm that slams into its hard stop.
//
// MAX_CHANGE is per loop. At ~50 Hz, 0.08 means roughly 0 to full in
// about a quarter second. Raise it if the robot feels sluggish.

static final double MAX_CHANGE = 0.08;

double lastDrive = 0;

double slew(double target, double current) {
    double delta = target - current;
    if (delta >  MAX_CHANGE) delta =  MAX_CHANGE;
    if (delta < -MAX_CHANGE) delta = -MAX_CHANGE;
    return current + delta;
}

// in the loop:
double wanted = -gamepad1.left_stick_y;
lastDrive = slew(wanted, lastDrive);
// then feed lastDrive into the mecanum mix instead of the raw stick
