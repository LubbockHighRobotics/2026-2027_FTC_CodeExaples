/*
 * AutoParkEncoder.java
 * ---------------------------------------------------------------
 * WHAT   Rung two. Measured distances plus IMU heading hold. Survives a low battery.
 * WHERE  Packet page 14
 * NEEDS  4 drive motors with encoders plugged in, and the IMU.
 * EDIT   TICKS_PER_REV and WHEEL_DIAM_CM for your robot.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RevHubOrientationOnRobot;

@Autonomous(name = "04 Auto - park (encoders)", group = "Auto")
public class AutoParkEncoder extends LinearOpMode {

    // EDIT: measure these. goBILDA 312 RPM = 537.7 ticks/rev, 96 mm wheel.
    static final double TICKS_PER_REV = 537.7;
    static final double WHEEL_DIAM_CM = 9.6;
    static final double TICKS_PER_CM  = TICKS_PER_REV / (WHEEL_DIAM_CM * Math.PI);

    static final double KP_HEADING = 0.02;   // raise if it wanders, lower if it wobbles

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

        waitForStart();
        if (isStopRequested()) return;

        imu.resetYaw();

        driveStraight(90, 0.5);    // 90 cm forward
        driveStraight(-30, 0.4);   // 30 cm back
    }

    /** Drives a distance in cm while holding the heading it started at. */
    void driveStraight(double cm, double power) {

        int    target   = (int)(cm * TICKS_PER_CM);
        int    start    = fl.getCurrentPosition();
        double heading0 = yaw();
        int    sign     = cm > 0 ? 1 : -1;

        long deadline = System.currentTimeMillis() + 6000;   // never hang forever

        while (opModeIsActive()
               && System.currentTimeMillis() < deadline
               && Math.abs(fl.getCurrentPosition() - start) < Math.abs(target)) {

            double error      = angleWrap(heading0 - yaw());
            double correction = error * KP_HEADING;

            // Ease off near the target so it doesn't slam to a stop.
            double remaining = Math.abs(target)
                             - Math.abs(fl.getCurrentPosition() - start);
            double scale = Math.min(1.0, Math.max(0.25,
                    remaining / (15 * TICKS_PER_CM)));
            double p = power * scale * sign;

            // IF IT CORRECTS THE WRONG WAY, swap these two signs. Which way
            // is right depends on your motor directions and IMU mounting.
            fl.setPower(p - correction);  bl.setPower(p - correction);
            fr.setPower(p + correction);  br.setPower(p + correction);

            telemetry.addData("remaining cm", "%.1f", remaining / TICKS_PER_CM);
            telemetry.addData("heading error", "%.1f", error);
            telemetry.update();
        }

        fl.setPower(0); bl.setPower(0); fr.setPower(0); br.setPower(0);
    }

    double yaw() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    /** Keeps an angle between -180 and 180, so 179 to -179 is 2 degrees, not 358. */
    double angleWrap(double deg) {
        while (deg >  180) deg -= 360;
        while (deg < -180) deg += 360;
        return deg;
    }
}
