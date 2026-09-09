/*
 * BlankTeleOp.java  -- rename this file and the class together.
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Blank TeleOp", group = "Templates")
public class BlankTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {

        // ---- hardware ----
        // DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");

        // ---- anything that should happen before PLAY ----
        telemetry.addLine("Initialized.");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {

            // ---- 1. read inputs ----

            // ---- 2. decide ----

            // ---- 3. set outputs ----

            // ---- 4. report ----
            telemetry.update();
        }
    }
}
