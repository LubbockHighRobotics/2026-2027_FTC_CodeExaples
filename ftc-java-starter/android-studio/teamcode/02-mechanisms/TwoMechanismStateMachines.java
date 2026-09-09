/*
 * TwoMechanismStateMachines.java
 * ---------------------------------------------------------------
 * WHAT   Two independent state machines running in one loop, plus driving.
 * WHERE  Packet page 10 - this is the file that shows why frameworks exist
 * NEEDS  lift + claw + intake motor + drivetrain.
 * EDIT   The constants at the top.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "02 Two mechanisms at once", group = "Examples")
public class TwoMechanismStateMachines extends LinearOpMode {

    enum LiftState   { IDLE, RAISING, RELEASING, RETRACTING }
    enum IntakeState { OFF, INTAKING, EJECTING }

    static final int    HIGH = 1800, TOLERANCE = 20;
    static final double OPEN = 0.6,  CLOSED = 0.2;

    @Override
    public void runOpMode() {

        DcMotor lift   = hardwareMap.get(DcMotor.class, "lift");
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
        Servo   claw   = hardwareMap.get(Servo.class,   "claw");

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        LiftState   liftState   = LiftState.IDLE;
        IntakeState intakeState = IntakeState.OFF;

        ElapsedTime liftTimer   = new ElapsedTime();
        ElapsedTime intakeTimer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {

            // ---------------- lift ----------------
            switch (liftState) {
                case IDLE:
                    if (gamepad1.a) {
                        lift.setTargetPosition(HIGH);
                        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        lift.setPower(0.8);
                        liftState = LiftState.RAISING;
                    }
                    break;
                case RAISING:
                    if (Math.abs(lift.getCurrentPosition() - HIGH) < TOLERANCE) {
                        claw.setPosition(OPEN);
                        liftTimer.reset();
                        liftState = LiftState.RELEASING;
                    }
                    break;
                case RELEASING:
                    if (liftTimer.seconds() > 0.3) {
                        claw.setPosition(CLOSED);
                        lift.setTargetPosition(0);
                        liftState = LiftState.RETRACTING;
                    }
                    break;
                case RETRACTING:
                    if (Math.abs(lift.getCurrentPosition()) < TOLERANCE) {
                        lift.setPower(0);
                        liftState = LiftState.IDLE;
                    }
                    break;
            }

            // ---------------- intake ----------------
            // Completely independent. Neither machine knows the other exists,
            // and both make progress on every single pass of the loop.
            switch (intakeState) {
                case OFF:
                    intake.setPower(0);
                    if (gamepad1.right_bumper) intakeState = IntakeState.INTAKING;
                    if (gamepad1.left_bumper) {
                        intakeTimer.reset();
                        intakeState = IntakeState.EJECTING;
                    }
                    break;
                case INTAKING:
                    intake.setPower(1.0);
                    if (!gamepad1.right_bumper) intakeState = IntakeState.OFF;
                    break;
                case EJECTING:
                    intake.setPower(-1.0);
                    if (intakeTimer.seconds() > 0.75) intakeState = IntakeState.OFF;
                    break;
            }

            telemetry.addData("lift",   liftState);
            telemetry.addData("intake", intakeState);
            telemetry.addLine();
            telemetry.addLine("Two mechanisms, one loop, nothing blocked.");
            telemetry.addLine("Add a third and this file starts to hurt --");
            telemetry.addLine("that is when 07-command-based becomes worth it.");
            telemetry.update();
        }
    }
}
