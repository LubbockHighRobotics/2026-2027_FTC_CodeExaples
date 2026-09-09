/*
 * ServoPositionFinder.java
 * ---------------------------------------------------------------
 * WHAT   Nudge a servo with the d-pad and read its position. This is how you
 * WHERE  Packet page 11 (you need these numbers before the lift example)
 * NEEDS  One servo named in the config.
 * EDIT   The servo config name.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "02 Servo position finder", group = "Examples")
public class ServoPositionFinder extends LinearOpMode {

    @Override
    public void runOpMode() {

        Servo servo = hardwareMap.get(Servo.class, "claw");   // EDIT

        double pos = 0.5;
        boolean lastUp = false, lastDown = false, lastLeft = false, lastRight = false;

        telemetry.addLine("D-pad UP/DOWN = 0.01 steps.  LEFT/RIGHT = 0.05 steps.");
        telemetry.addLine("Write the numbers down. They become your OPEN and CLOSED.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // Rising-edge checks so one press is one step, not fifty.
            if (gamepad1.dpad_up    && !lastUp)    pos += 0.01;
            if (gamepad1.dpad_down  && !lastDown)  pos -= 0.01;
            if (gamepad1.dpad_right && !lastRight) pos += 0.05;
            if (gamepad1.dpad_left  && !lastLeft)  pos -= 0.05;

            lastUp = gamepad1.dpad_up;       lastDown  = gamepad1.dpad_down;
            lastRight = gamepad1.dpad_right; lastLeft  = gamepad1.dpad_left;

            pos = Range.clip(pos, 0.0, 1.0);
            servo.setPosition(pos);

            telemetry.addData("position", "%.3f", pos);
            telemetry.addLine("Move it by hand ONLY with the robot powered off.");
            telemetry.update();
        }
    }
}
