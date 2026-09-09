// FRAGMENT - not an OpMode. Paste this into your own file.

// Three inputs -> four wheel powers, without clipping.
//
// The denominator keeps the RATIOS between wheels correct. If you just
// clamp each wheel to 1.0 instead, hard turns while driving fast come out
// crooked, because the wheel that needed 1.4 gets the same 1.0 as the
// wheel that needed 1.1.

double drive  = -gamepad1.left_stick_y;    // stick up reads negative
double strafe =  gamepad1.left_stick_x;
double turn   =  gamepad1.right_stick_x;

double denom = Math.max(Math.abs(drive) + Math.abs(strafe)
                      + Math.abs(turn), 1.0);

frontLeft .setPower((drive + strafe + turn) / denom);
backLeft  .setPower((drive - strafe + turn) / denom);
frontRight.setPower((drive - strafe - turn) / denom);
backRight .setPower((drive + strafe - turn) / denom);
