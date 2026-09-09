/*
 * LoopTimeCheck.java
 * ---------------------------------------------------------------
 * WHAT   Measures how long one pass of your loop takes, with and without bulk caching.
 * WHERE  Packet page 22 (habits)
 * NEEDS  4 drive motors.
 * EDIT   Nothing. Run it, read the numbers.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;

@TeleOp(name = "02 Loop time check", group = "Examples")
public class LoopTimeCheck extends LinearOpMode {

    @Override
    public void runOpMode() {

        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "backRight");

        List<LynxModule> hubs = hardwareMap.getAll(LynxModule.class);

        boolean caching = true;
        boolean lastX   = false;

        ElapsedTime loop = new ElapsedTime();
        double worst = 0;

        telemetry.addLine("Press X to toggle bulk caching on and off.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.x && !lastX) { caching = !caching; worst = 0; }
            lastX = gamepad1.x;

            for (LynxModule hub : hubs) {
                hub.setBulkCachingMode(caching
                        ? LynxModule.BulkCachingMode.MANUAL
                        : LynxModule.BulkCachingMode.OFF);
                if (caching) hub.clearBulkCache();
            }

            // Read every encoder, the way a real OpMode would.
            int a = frontLeft.getCurrentPosition();
            int b = backLeft.getCurrentPosition();
            int c = frontRight.getCurrentPosition();
            int d = backRight.getCurrentPosition();

            double ms = loop.milliseconds();
            loop.reset();
            if (ms > worst) worst = ms;

            telemetry.addData("bulk caching", caching ? "MANUAL (good)" : "OFF (slow)");
            telemetry.addData("loop ms", "%.1f", ms);
            telemetry.addData("worst ms", "%.1f", worst);
            telemetry.addData("hz", "%.0f", 1000.0 / Math.max(ms, 0.001));
            telemetry.addLine();
            telemetry.addLine("Under 20 ms is healthy. Over 30 ms feels mushy.");
            telemetry.addData("encoders", "%d %d %d %d", a, b, c, d);
            telemetry.update();
        }
    }
}
