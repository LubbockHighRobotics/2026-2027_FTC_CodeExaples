/*
 * TurnToHeading.java
 * ---------------------------------------------------------------
 * WHAT   Turns to an absolute field heading with a tolerance and a timeout.
 * WHERE  Packet page 14 - the other half of an encoder auto
 * NEEDS  4 drive motors and the IMU. Encoders not required.
 * EDIT   KP_TURN and TOLERANCE while you tune.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RevHubOrientationOnRobot;

@Autonomous(name = "04 Tune - turn to heading", group = "Tuning")
public class TurnToHeading extends LinearOpMode {

    static final double KP_TURN   = 0.015;   // EDIT
    static final double MIN_POWER = 0.12;    // below this the robot won't move
    static final double TOLERANCE = 2.0;     // degrees
    static final double SETTLE_S  = 0.25;    // must stay in tolerance this long

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

        for (DcMotor m : new DcMotor[]{fl, bl, fr, br})
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));

        telemetry.addLine("Turns 90, 180, 270, then back to 0.");
        telemetry.update();

        waitForStart();
        imu.resetYaw();

        turnTo(90);
        turnTo(180);
        turnTo(-90);
        turnTo(0);
    }

    /** Turns until the robot is within TOLERANCE of an absolute heading. */
    void turnTo(double targetDeg) {

        long   deadline  = System.currentTimeMillis() + 4000;
        double inRangeAt = -1;

        while (opModeIsActive() && System.currentTimeMillis() < deadline) {

            double error = angleWrap(targetDeg - yaw());

            if (Math.abs(error) < TOLERANCE) {
                // Settle: it has to STAY there, not just pass through.
                if (inRangeAt < 0) inRangeAt = System.currentTimeMillis();
                if ((System.currentTimeMillis() - inRangeAt) / 1000.0 > SETTLE_S) break;
            } else {
                inRangeAt = -1;
            }

            double p = error * KP_TURN;
            if (Math.abs(p) < MIN_POWER) p = Math.signum(p) * MIN_POWER;
            p = Math.max(-0.6, Math.min(0.6, p));

            fl.setPower(-p); bl.setPower(-p);
            fr.setPower( p); br.setPower( p);

            telemetry.addData("target", targetDeg);
            telemetry.addData("heading", "%.1f", yaw());
            telemetry.addData("error", "%.1f", error);
            telemetry.update();
        }

        fl.setPower(0); bl.setPower(0); fr.setPower(0); br.setPower(0);
        sleep(200);
    }

    double yaw() { return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES); }

    double angleWrap(double deg) {
        while (deg >  180) deg -= 360;
        while (deg < -180) deg += 360;
        return deg;
    }
}
