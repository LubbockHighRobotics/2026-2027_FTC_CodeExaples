// FRAGMENT - not an OpMode. Paste this into your own file.

// One press = one action.
//
// Your loop runs 50+ times a second. A human holds a button for maybe
// 200 ms, so "if (gamepad1.a)" fires ten or more times per press. That
// is why your toggle flickers and your counter jumps by 12.
//
// Remember what the button was doing LAST loop and only act on the change.

boolean lastA = false;

while (opModeIsActive()) {

    if (gamepad1.a && !lastA) {
        // fires exactly once, on the press
        fieldCentric = !fieldCentric;
    }
    lastA = gamepad1.a;
}

// For several buttons, one boolean each. If you find yourself with six of
// them, that is a sign it's time for 07-command-based.
