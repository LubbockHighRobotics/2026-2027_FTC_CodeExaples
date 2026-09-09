/*
 * LiftStateMachine.java
 * ---------------------------------------------------------------
 * WHAT   The same lift routine with no blocking. Driver keeps the robot the whole time.
 * WHERE  Packet page 11
 * NEEDS  One motor named lift, one servo named claw, plus your drivetrain.
 * EDIT   HIGH, OPEN and CLOSED - get those from EncoderReader and ServoPositionFinder.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "02 Lift - state machine", group = "Examples")
public class LiftStateMachine extends LinearOpMode {

    // Every state the lift can be in. Add states, never add sleeps.
    enum LiftState { IDLE, RAISING, RELEASING, RETRACTING }

    static final int    HIGH = 1800, TOLERANCE = 20;   // EDIT: encoder ticks
    static final double OPEN = 0.6,  CLOSED = 0.2;     // EDIT: servo positions

    @Override
    public void runOpMode() {

        DcMotor lift = hardwareMap.get(DcMotor.class, "lift");
        Servo   claw = hardwareMap.get(Servo.class,   "claw");

        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft .setDirection(DcMotor.Direction.REVERSE);

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        claw.setPosition(CLOSED);

        LiftState   state = LiftState.IDLE;
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {

            switch (state) {

                case IDLE:
                    if (gamepad1.a) {
                        lift.setTargetPosition(HIGH);
                        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        lift.setPower(0.8);
                        state = LiftState.RAISING;
                    }
                    break;

                case RAISING:
                    // Don't wait for it. Ask whether it has arrived.
                    if (Math.abs(lift.getCurrentPosition() - HIGH) < TOLERANCE) {
                        claw.setPosition(OPEN);
                        timer.reset();
                        state = LiftState.RELEASING;
                    }
                    break;

                case RELEASING:
                    if (timer.seconds() > 0.3) {          // servo travel time
                        claw.setPosition(CLOSED);
                        lift.setTargetPosition(0);
                        state = LiftState.RETRACTING;
                    }
                    break;

                case RETRACTING:
                    if (Math.abs(lift.getCurrentPosition()) < TOLERANCE) {
                        lift.setPower(0);
                        state = LiftState.IDLE;
                    }
                    break;
            }

            // Escape hatch. Press B at any point to bail out of the sequence.
            if (gamepad1.b) {
                lift.setTargetPosition(0);
                state = LiftState.RETRACTING;
            }

            // ---- This runs every pass, in every state. The driver never
            // ---- loses the robot no matter what the lift is doing.
            double drive  = -gamepad1.left_stick_y;
            double strafe =  gamepad1.left_stick_x;
            double turn   =  gamepad1.right_stick_x;
            double denom  = Math.max(Math.abs(drive) + Math.abs(strafe)
                                   + Math.abs(turn), 1.0);
            frontLeft .setPower((drive + strafe + turn) / denom);
            backLeft  .setPower((drive - strafe + turn) / denom);
            frontRight.setPower((drive - strafe - turn) / denom);
            backRight .setPower((drive + strafe - turn) / denom);

            telemetry.addData("lift state", state);
            telemetry.addData("position",   lift.getCurrentPosition());
            telemetry.update();
        }
    }
}
