/*
 * DriveStraightHeadingHold.java
 * ---------------------------------------------------------------
 * WHAT   A runnable test for the driveStraight helper. Drives a square.
 * WHERE  Packet page 14
 * NEEDS  4 drive motors with encoders, and the IMU.
 * EDIT   The distance and KP_HEADING while you tune.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RevHubOrientationOnRobot;

@Autonomous(name = "04 Tune - drive straight", group = "Tuning")
public class DriveStraightHeadingHold extends LinearOpMode {

    static final double TICKS_PER_CM = 537.7 / (9.6 * Math.PI);   // EDIT
    static final double KP_HEADING   = 0.02;                      // EDIT while tuning

    DcMotor fl, bl, fr, br;
    IMU imu;

    @Override
    public void runOpMode() {

        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);

        for (DcMotor m : new DcMotor[]{fl, bl, fr, br}) {
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));

        telemetry.addLine("Drives 100 cm out and 100 cm back, four times.");
        telemetry.addLine("Measure where it ends up. It should return to the start.");
        telemetry.update();

        waitForStart();
        imu.resetYaw();

        for (int i = 0; i < 4 && opModeIsActive(); i++) {
            driveStraight(100, 0.5);
            sleep(400);
            driveStraight(-100, 0.5);
            sleep(400);
        }

        telemetry.addLine("Done. Measure the error and adjust TICKS_PER_CM.");
        telemetry.update();
        sleep(4000);
    }

    void driveStraight(double cm, double power) {
        int    target   = (int)(cm * TICKS_PER_CM);
        int    start    = fl.getCurrentPosition();
        double heading0 = yaw();
        int    sign     = cm > 0 ? 1 : -1;
        long   deadline = System.currentTimeMillis() + 6000;

        while (opModeIsActive()
               && System.currentTimeMillis() < deadline
               && Math.abs(fl.getCurrentPosition() - start) < Math.abs(target)) {

            double correction = angleWrap(heading0 - yaw()) * KP_HEADING;
            double remaining  = Math.abs(target)
                              - Math.abs(fl.getCurrentPosition() - start);
            double scale = Math.min(1.0, Math.max(0.25,
                    remaining / (15 * TICKS_PER_CM)));
            double p = power * scale * sign;

            // IF IT CORRECTS THE WRONG WAY, swap these two signs. Which way
            // is right depends on your motor directions and IMU mounting.
            fl.setPower(p - correction);  bl.setPower(p - correction);
            fr.setPower(p + correction);  br.setPower(p + correction);
            telemetry.addData("remaining cm", "%.1f", remaining / TICKS_PER_CM);
            telemetry.update();
        }
        fl.setPower(0); bl.setPower(0); fr.setPower(0); br.setPower(0);
    }

    double yaw() { return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES); }

    double angleWrap(double deg) {
        while (deg >  180) deg -= 360;
        while (deg < -180) deg += 360;
        return deg;
    }
}
