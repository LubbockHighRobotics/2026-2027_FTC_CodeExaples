/*
 * LimelightAutoAim.java
 * ---------------------------------------------------------------
 * WHAT   Turns the robot until the target is centred, with a deadband so it settles.
 * WHERE  Packet page 21
 * NEEDS  Limelight 3A named limelight, mecanum drivetrain.
 * EDIT   KP_AIM and DEADBAND.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "06 Limelight - auto aim", group = "Vision")
public class LimelightAutoAim extends LinearOpMode {

    static final double KP_AIM    = 0.020;   // EDIT
    static final double DEADBAND  = 1.0;     // degrees - stops it hunting
    static final double MIN_POWER = 0.10;    // below this nothing moves
    static final double MAX_POWER = 0.35;

    @Override
    public void runOpMode() {

        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft .setDirection(DcMotor.Direction.REVERSE);

        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();

        telemetry.addLine("Hold RIGHT BUMPER to auto-aim. Otherwise drive normally.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            LLResult result = limelight.getLatestResult();
            boolean  locked = result != null && result.isValid();

            double drive, strafe, turn;

            if (gamepad1.right_bumper && locked) {
                double tx = result.getTx();

                if (Math.abs(tx) < DEADBAND) {
                    turn = 0;
                    telemetry.addLine("ON TARGET");
                } else {
                    turn = tx * KP_AIM;
                    if (Math.abs(turn) < MIN_POWER) turn = Math.signum(turn) * MIN_POWER;
                    turn = Math.max(-MAX_POWER, Math.min(MAX_POWER, turn));
                }

                // Driver keeps translation while the robot handles rotation.
                drive  = -gamepad1.left_stick_y * 0.5;
                strafe =  gamepad1.left_stick_x * 0.5;

                telemetry.addData("tx", "%.2f", tx);

            } else {
                drive  = -gamepad1.left_stick_y;
                strafe =  gamepad1.left_stick_x;
                turn   =  gamepad1.right_stick_x;
                telemetry.addLine(locked ? "manual (target visible)" : "manual (no target)");
            }

            double denom = Math.max(Math.abs(drive) + Math.abs(strafe)
                                  + Math.abs(turn), 1.0);
            frontLeft .setPower((drive + strafe + turn) / denom);
            backLeft  .setPower((drive - strafe + turn) / denom);
            frontRight.setPower((drive - strafe - turn) / denom);
            backRight .setPower((drive + strafe - turn) / denom);

            telemetry.update();
        }

        limelight.stop();
    }
}
