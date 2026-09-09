/*
 * MecanumBothModes.java
 * ---------------------------------------------------------------
 * WHAT   Mecanum drive with a robot-centric / field-centric toggle.
 * WHERE  Packet page 9 - this is the one most teams end up using
 * NEEDS  4 drive motors + IMU. No outside libraries.
 * EDIT   The four config names. Nothing else.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RevHubOrientationOnRobot;

@TeleOp(name = "01 Mecanum - both modes", group = "Examples")
public class MecanumBothModes extends LinearOpMode {

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

        IMU imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));

        boolean fieldCentric = false;
        boolean lastToggle   = false;
        double  speed        = 1.0;

        telemetry.addLine("OPTIONS toggles drive mode. BACK resets heading.");
        telemetry.addLine("Left trigger = slow mode.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // Rising edge: fires once per press, not once per loop.
            if (gamepad1.options && !lastToggle) fieldCentric = !fieldCentric;
            lastToggle = gamepad1.options;

            if (gamepad1.back) imu.resetYaw();

            // Slow mode for precise scoring. Full speed at rest.
            speed = 1.0 - (0.65 * gamepad1.left_trigger);

            double drive  = -gamepad1.left_stick_y;
            double strafe =  gamepad1.left_stick_x;
            double turn   =  gamepad1.right_stick_x;

            if (fieldCentric) {
                double h = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
                double rotStrafe = strafe * Math.cos(-h) - drive * Math.sin(-h);
                double rotDrive  = strafe * Math.sin(-h) + drive * Math.cos(-h);
                strafe = rotStrafe;
                drive  = rotDrive;
            }

            double denom = Math.max(Math.abs(drive) + Math.abs(strafe)
                                  + Math.abs(turn), 1.0);

            frontLeft .setPower(speed * (drive + strafe + turn) / denom);
            backLeft  .setPower(speed * (drive - strafe + turn) / denom);
            frontRight.setPower(speed * (drive - strafe - turn) / denom);
            backRight .setPower(speed * (drive + strafe - turn) / denom);

            telemetry.addData("mode",  fieldCentric ? "FIELD" : "ROBOT");
            telemetry.addData("speed", "%.0f%%", speed * 100);
            telemetry.addData("heading",
                    "%.1f deg", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.update();
        }
    }
}
