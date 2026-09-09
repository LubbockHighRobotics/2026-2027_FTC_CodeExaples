/*
 * RobotHardware.java
 * ---------------------------------------------------------------
 * WHAT   Every hardware device and its config name, in one file.
 * WHY    A config rename becomes a one-line change instead of a hunt
 *        through nine OpModes. Half of all "the code is broken" reports
 *        are a name mismatch, and this is the fix.
 * USE    In any OpMode:
 *            RobotHardware robot = new RobotHardware(hardwareMap);
 *            robot.init();
 *            robot.frontLeft.setPower(0.5);
 * ---------------------------------------------------------------
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.RevHubOrientationOnRobot;

public class RobotHardware {

    // ---- config names. Change them HERE and nowhere else. ----
    public static final String FRONT_LEFT  = "frontLeft";
    public static final String BACK_LEFT   = "backLeft";
    public static final String FRONT_RIGHT = "frontRight";
    public static final String BACK_RIGHT  = "backRight";
    public static final String LIFT        = "lift";
    public static final String CLAW        = "claw";
    public static final String IMU_NAME    = "imu";

    // ---- devices ----
    public DcMotor frontLeft, backLeft, frontRight, backRight, lift;
    public Servo   claw;
    public IMU     imu;

    private final HardwareMap hw;

    public RobotHardware(HardwareMap hardwareMap) {
        this.hw = hardwareMap;
    }

    public void init() {

        frontLeft  = hw.get(DcMotor.class, FRONT_LEFT);
        backLeft   = hw.get(DcMotor.class, BACK_LEFT);
        frontRight = hw.get(DcMotor.class, FRONT_RIGHT);
        backRight  = hw.get(DcMotor.class, BACK_RIGHT);
        lift       = hw.get(DcMotor.class, LIFT);
        claw       = hw.get(Servo.class,   CLAW);
        imu        = hw.get(IMU.class,     IMU_NAME);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft .setDirection(DcMotor.Direction.REVERSE);

        for (DcMotor m : new DcMotor[]{frontLeft, backLeft, frontRight, backRight})
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
    }

    /** Convenience: the mecanum mix, so no OpMode has to repeat it. */
    public void drive(double forward, double strafe, double turn) {
        double denom = Math.max(Math.abs(forward) + Math.abs(strafe)
                              + Math.abs(turn), 1.0);
        frontLeft .setPower((forward + strafe + turn) / denom);
        backLeft  .setPower((forward - strafe + turn) / denom);
        frontRight.setPower((forward - strafe - turn) / denom);
        backRight .setPower((forward + strafe - turn) / denom);
    }

    public void stop() { drive(0, 0, 0); }
}
