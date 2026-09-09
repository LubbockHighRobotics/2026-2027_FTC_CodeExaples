/*
 * HelloMotor.java
 * ---------------------------------------------------------------
 * WHAT   One motor, one joystick. Run this before anything else.
 * WHERE  Packet page 7 (Control Hub setup)
 * NEEDS  One motor in the config. No IMU, no libraries.
 * EDIT   The config name on the line marked EDIT.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "01 Hello Motor", group = "Examples")
public class HelloMotor extends LinearOpMode {

    @Override
    public void runOpMode() {

        // EDIT: must match a motor name in your configuration file, exactly.
        DcMotor motor = hardwareMap.get(DcMotor.class, "frontLeft");

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Ready. Press PLAY, then push the left stick up and down.");
        telemetry.addLine("If this crashes on INIT, the name above does not match");
        telemetry.addLine("your config file. Check spelling AND capitalization.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double power = -gamepad1.left_stick_y;   // stick up reads negative
            motor.setPower(power);

            telemetry.addData("power", "%.2f", power);
            telemetry.addData("encoder", motor.getCurrentPosition());
            telemetry.update();
        }
    }
}
