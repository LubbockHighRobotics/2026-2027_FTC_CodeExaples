/*
 * AutoScoreThenPark.java
 * ---------------------------------------------------------------
 * WHAT   Rung one, plus one mechanism action. The realistic week-three auto.
 * WHERE  Packet page 13
 * NEEDS  4 drive motors, a lift motor and a claw servo.
 * EDIT   Times, distances, and your lift/claw constants.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "03 Auto - score then park (time)", group = "Auto")
public class AutoScoreThenPark extends LinearOpMode {

    static final int    HIGH   = 1800;   // EDIT
    static final double OPEN   = 0.6, CLOSED = 0.2;

    DcMotor frontLeft, backLeft, frontRight, backRight, lift;
    Servo   claw;

    @Override
    public void runOpMode() {

        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");
        lift       = hardwareMap.get(DcMotor.class, "lift");
        claw       = hardwareMap.get(Servo.class,   "claw");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft .setDirection(DcMotor.Direction.REVERSE);

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        claw.setPosition(CLOSED);

        waitForStart();
        if (isStopRequested()) return;

        drive(0.4, 0, 0, 1100);         // approach
        stopDriving();

        liftTo(HIGH, 0.8);              // raise
        claw.setPosition(OPEN);         // score
        sleep(400);
        claw.setPosition(CLOSED);
        liftTo(0, 0.6);                 // retract

        drive(-0.4, 0, 0, 500);         // back off
        drive(0, 0.5, 0, 900);          // park
        stopDriving();
    }

    /** Blocks until the lift reaches a target, with a timeout so a jam
     *  can never eat the whole autonomous period. */
    void liftTo(int target, double power) {
        lift.setTargetPosition(target);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(power);

        long deadline = System.currentTimeMillis() + 3000;   // 3 s timeout
        while (opModeIsActive() && lift.isBusy()
               && System.currentTimeMillis() < deadline) {
            telemetry.addData("lift", lift.getCurrentPosition());
            telemetry.update();
        }
        lift.setPower(0);
    }

    void drive(double fwd, double strafe, double turn, long ms) {
        frontLeft .setPower(fwd + strafe + turn);
        backLeft  .setPower(fwd - strafe + turn);
        frontRight.setPower(fwd - strafe - turn);
        backRight .setPower(fwd + strafe - turn);
        sleep(ms);
    }

    void stopDriving() {
        frontLeft.setPower(0);  backLeft.setPower(0);
        frontRight.setPower(0); backRight.setPower(0);
    }
}
