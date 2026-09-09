// FRAGMENT - not an OpMode. Paste this into your own file.

// Keeps an angle between -180 and +180.
//
// Without this, a robot at 179 degrees trying to reach -179 thinks it
// needs to turn 358 degrees the long way round. Everyone writes a turn
// function without this exactly once.

double angleWrap(double degrees) {
    while (degrees >  180) degrees -= 360;
    while (degrees < -180) degrees += 360;
    return degrees;
}

// Use it for every heading comparison, never plain subtraction:
double error = angleWrap(targetHeading - currentHeading);
