/*
 * MecanumRobotCentric.java
 * ---------------------------------------------------------------
 * WHAT   Mecanum drive. Forward is wherever the robot's nose points.
 * WHERE  Packet page 8
 * NEEDS  4 drive motors. No IMU.
 * EDIT   The four config names, and the two REVERSE lines if a wheel spins wrong.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "01 Mecanum - robot centric", group = "Examples")
public class MecanumRobotCentric extends LinearOpMode {

    @Override
    public void runOpMode() {

        // EDIT: these four names must match your configuration file.
        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "backRight");

        // EDIT: if a wheel spins the wrong way, fix it HERE, never in the math.
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft .setDirection(DcMotor.Direction.REVERSE);

        for (DcMotor m : new DcMotor[]{frontLeft, backLeft, frontRight, backRight})
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {

            double drive  = -gamepad1.left_stick_y;    // forward / back
            double strafe =  gamepad1.left_stick_x;    // left / right
            double turn   =  gamepad1.right_stick_x;   // rotate

            // Keep the ratios between wheels instead of clipping them.
            double denom = Math.max(Math.abs(drive) + Math.abs(strafe)
                                  + Math.abs(turn), 1.0);

            frontLeft .setPower((drive + strafe + turn) / denom);
            backLeft  .setPower((drive - strafe + turn) / denom);
            frontRight.setPower((drive - strafe - turn) / denom);
            backRight .setPower((drive + strafe - turn) / denom);

            telemetry.addData("drive",  "%.2f", drive);
            telemetry.addData("strafe", "%.2f", strafe);
            telemetry.addData("turn",   "%.2f", turn);
            telemetry.update();
        }
    }
}
