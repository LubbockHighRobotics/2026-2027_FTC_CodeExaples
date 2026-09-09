/*
 * DriveSubsystem.java
 * ---------------------------------------------------------------
 * WHAT   Mecanum drivetrain as a subsystem, robot- and field-centric.
 * WHERE  07-command-based/README.md
 * NEEDS  4 drive motors and the IMU.
 * EDIT   Config names and hub orientation.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RevHubOrientationOnRobot;

public class DriveSubsystem {

    private final DcMotor fl, bl, fr, br;
    private final IMU imu;

    private boolean fieldCentric = false;
    private double  drive, strafe, turn, speed = 1.0;

    public DriveSubsystem(HardwareMap hw) {
        fl  = hw.get(DcMotor.class, "frontLeft");
        bl  = hw.get(DcMotor.class, "backLeft");
        fr  = hw.get(DcMotor.class, "frontRight");
        br  = hw.get(DcMotor.class, "backRight");
        imu = hw.get(IMU.class, "imu");
    }

    public void init() {
        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);
        for (DcMotor m : new DcMotor[]{fl, bl, fr, br})
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
    }

    // ---------------- requests ----------------

    public void setInput(double drive, double strafe, double turn) {
        this.drive  = drive;
        this.strafe = strafe;
        this.turn   = turn;
    }

    public void setSpeed(double speed)        { this.speed = speed; }
    public void setFieldCentric(boolean on)   { this.fieldCentric = on; }
    public void toggleFieldCentric()          { fieldCentric = !fieldCentric; }
    public void resetHeading()                { imu.resetYaw(); }
    public boolean isFieldCentric()           { return fieldCentric; }

    public double headingDeg() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public void stop() { setInput(0, 0, 0); update(); }

    // ---------------- the loop ----------------

    public void update() {

        double d = drive, s = strafe;

        if (fieldCentric) {
            double h = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double rs = s * Math.cos(-h) - d * Math.sin(-h);
            double rd = s * Math.sin(-h) + d * Math.cos(-h);
            s = rs; d = rd;
        }

        double denom = Math.max(Math.abs(d) + Math.abs(s) + Math.abs(turn), 1.0);

        fl.setPower(speed * (d + s + turn) / denom);
        bl.setPower(speed * (d - s + turn) / denom);
        fr.setPower(speed * (d - s - turn) / denom);
        br.setPower(speed * (d + s - turn) / denom);
    }
}
