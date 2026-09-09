/*
 * BlankAuto.java  -- rename this file and the class together.
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Blank Auto", group = "Templates")
public class BlankAuto extends LinearOpMode {

    @Override
    public void runOpMode() {

        // ---- hardware ----

        telemetry.addLine("Place the robot against the alignment reference.");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;   // never leave this out

        ElapsedTime matchTimer = new ElapsedTime();

        // ---- the routine ----

        // Safety net: whatever else happens, don't run past the auto period.
        while (opModeIsActive() && matchTimer.seconds() < 29) {
            telemetry.addData("elapsed", "%.1f", matchTimer.seconds());
            telemetry.update();
            break;   // delete this once you have real steps
        }
    }
}
