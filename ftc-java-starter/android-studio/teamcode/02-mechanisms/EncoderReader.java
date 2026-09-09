/*
 * EncoderReader.java
 * ---------------------------------------------------------------
 * WHAT   Prints every drive encoder plus one mechanism motor.
 * WHERE  Packet page 14 (you need ticks before you can write an encoder auto)
 * NEEDS  4 drive motors. The 'lift' motor is optional.
 * EDIT   Config names. Delete the lift lines if you don't have one yet.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "02 Encoder reader", group = "Examples")
public class EncoderReader extends LinearOpMode {

    @Override
    public void runOpMode() {

        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor lift       = hardwareMap.get(DcMotor.class, "lift");   // EDIT or delete

        for (DcMotor m : new DcMotor[]{frontLeft, backLeft, frontRight, backRight, lift}) {
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        telemetry.addLine("Press PLAY, then PUSH THE ROBOT BY HAND a measured distance.");
        telemetry.addLine("Divide ticks by distance to get your ticks-per-inch.");
        telemetry.addLine("Press A to zero all encoders.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                for (DcMotor m : new DcMotor[]{frontLeft, backLeft, frontRight, backRight, lift}) {
                    m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
            }

            telemetry.addData("frontLeft ", frontLeft.getCurrentPosition());
            telemetry.addData("backLeft  ", backLeft.getCurrentPosition());
            telemetry.addData("frontRight", frontRight.getCurrentPosition());
            telemetry.addData("backRight ", backRight.getCurrentPosition());
            telemetry.addData("lift      ", lift.getCurrentPosition());
            telemetry.update();
        }
    }
}
