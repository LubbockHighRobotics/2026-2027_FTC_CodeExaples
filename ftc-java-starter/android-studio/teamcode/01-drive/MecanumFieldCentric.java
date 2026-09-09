/*
 * MecanumFieldCentric.java
 * ---------------------------------------------------------------
 * WHAT   Mecanum drive where forward is always away from the driver.
 * WHERE  Packet page 8
 * NEEDS  4 drive motors + the Control Hub IMU.
 * EDIT   Config names, motor directions, and the hub orientation below.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RevHubOrientationOnRobot;

@TeleOp(name = "01 Mecanum - field centric", group = "Examples")
public class MecanumFieldCentric extends LinearOpMode {

    @Override
    public void runOpMode() {

        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft .setDirection(DcMotor.Direction.REVERSE);

        for (DcMotor m : new DcMotor[]{frontLeft, backLeft, frontRight, backRight})
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // EDIT: describe how your Control Hub is mounted. Get this wrong and
        // field centric will be rotated by 90 degrees and feel broken.
        IMU imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));

        telemetry.addLine("Point the robot AWAY from the driver, then press PLAY.");
        telemetry.addLine("Press BACK at any time to re-zero the heading.");
        telemetry.update();

        waitForStart();
        imu.resetYaw();

        while (opModeIsActive()) {

            if (gamepad1.back) imu.resetYaw();

            double drive  = -gamepad1.left_stick_y;
            double strafe =  gamepad1.left_stick_x;
            double turn   =  gamepad1.right_stick_x;

            // Rotate the joystick vector by the robot's heading.
            double h = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double rotStrafe = strafe * Math.cos(-h) - drive * Math.sin(-h);
            double rotDrive  = strafe * Math.sin(-h) + drive * Math.cos(-h);

            double denom = Math.max(Math.abs(rotDrive) + Math.abs(rotStrafe)
                                  + Math.abs(turn), 1.0);

            frontLeft .setPower((rotDrive + rotStrafe + turn) / denom);
            backLeft  .setPower((rotDrive - rotStrafe + turn) / denom);
            frontRight.setPower((rotDrive - rotStrafe - turn) / denom);
            backRight .setPower((rotDrive + rotStrafe - turn) / denom);

            telemetry.addData("heading",
                    "%.1f deg", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.update();
        }
    }
}
