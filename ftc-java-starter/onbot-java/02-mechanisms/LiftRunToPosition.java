/*
 * LiftRunToPosition.java
 * ---------------------------------------------------------------
 * WHAT   THE WRONG WAY, on purpose. Uses sleep() and freezes the whole robot.
 * WHERE  Packet page 10 - read that, then open LiftStateMachine.java
 * NEEDS  One motor named lift, one servo named claw.
 * EDIT   Nothing. Do not use this in a match. It is here to be compared.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "02 Lift - blocking (BAD)", group = "Examples")
public class LiftRunToPosition extends LinearOpMode {

    static final int HIGH = 1800;

    @Override
    public void runOpMode() {

        DcMotor lift = hardwareMap.get(DcMotor.class, "lift");
        Servo   claw = hardwareMap.get(Servo.class,   "claw");

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                lift.setTargetPosition(HIGH);
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lift.setPower(0.8);

                sleep(1500);          // <-- the robot is now frozen
                claw.setPosition(0.6);
                sleep(300);           // <-- still frozen
                claw.setPosition(0.2);
                lift.setTargetPosition(0);
                sleep(1500);          // <-- still frozen
            }

            // Drive code down here CANNOT RUN during those three sleeps.
            // That is roughly 3.3 seconds of a 2 minute 30 second match where
            // your driver has no robot, every single time they press A.

            telemetry.addLine("This OpMode is the bad example. See LiftStateMachine.");
            telemetry.update();
        }
    }
}
