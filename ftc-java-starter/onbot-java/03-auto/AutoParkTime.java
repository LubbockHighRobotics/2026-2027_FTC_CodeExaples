/*
 * AutoParkTime.java
 * ---------------------------------------------------------------
 * WHAT   Rung one. Drives for a set time and parks. A complete, legal, scoring auto.
 * WHERE  Packet page 13
 * NEEDS  4 drive motors. Nothing else.
 * EDIT   The two times in runOpMode. Tune them on your field.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "03 Auto - park (time)", group = "Auto")
public class AutoParkTime extends LinearOpMode {

    DcMotor frontLeft, backLeft, frontRight, backRight;

    @Override
    public void runOpMode() {

        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft .setDirection(DcMotor.Direction.REVERSE);

        for (DcMotor m : new DcMotor[]{frontLeft, backLeft, frontRight, backRight})
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Place the robot against your alignment reference.");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        drive(0.4, 0, 0, 1200);   // EDIT: forward, 1.2 s
        drive(0, 0.4, 0,  800);   // EDIT: strafe right, 0.8 s
        stopDriving();
    }

    /** Runs the drivetrain at a power for a number of milliseconds. */
    void drive(double fwd, double strafe, double turn, long ms) {
        frontLeft .setPower(fwd + strafe + turn);
        backLeft  .setPower(fwd - strafe + turn);
        frontRight.setPower(fwd - strafe - turn);
        backRight .setPower(fwd + strafe - turn);
        sleep(ms);
        // sleep() is acceptable HERE and only here: in auto there is no driver
        // to starve. Never do this in TeleOp -- see packet page 10.
    }

    void stopDriving() {
        frontLeft.setPower(0);  backLeft.setPower(0);
        frontRight.setPower(0); backRight.setPower(0);
    }
}
