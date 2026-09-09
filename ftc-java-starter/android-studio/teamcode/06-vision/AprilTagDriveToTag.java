/*
 * AprilTagDriveToTag.java
 * ---------------------------------------------------------------
 * WHAT   Closes on a tag to a set distance using range, bearing and yaw.
 * WHERE  Packet page 20 - the first genuinely useful vision behaviour
 * NEEDS  A webcam named webcam1, mecanum drivetrain.
 * EDIT   DESIRED_DISTANCE and the three gains.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp(name = "06 AprilTag - drive to tag", group = "Vision")
public class AprilTagDriveToTag extends LinearOpMode {

    static final double DESIRED_DISTANCE = 12.0;   // inches from the tag

    // Start small. If it oscillates, halve them.
    static final double SPEED_GAIN  = 0.02;
    static final double STRAFE_GAIN = 0.015;
    static final double TURN_GAIN   = 0.01;

    static final double MAX_AUTO_SPEED  = 0.5;
    static final double MAX_AUTO_STRAFE = 0.5;
    static final double MAX_AUTO_TURN   = 0.3;

    static final int TARGET_TAG = -1;   // -1 means "any tag"

    @Override
    public void runOpMode() {

        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft .setDirection(DcMotor.Direction.REVERSE);

        AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder().build();
        VisionPortal portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "webcam1"))
                .setCameraResolution(new Size(640, 480))
                .addProcessor(tagProcessor)
                .build();

        telemetry.addLine("Hold RIGHT BUMPER to drive to the tag.");
        telemetry.addLine("Release it and you get manual control back.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            AprilTagDetection target = null;
            for (AprilTagDetection d : tagProcessor.getDetections()) {
                if (d.metadata == null) continue;
                if (TARGET_TAG < 0 || d.id == TARGET_TAG) { target = d; break; }
            }

            double drive, strafe, turn;

            if (gamepad1.right_bumper && target != null) {

                double rangeError   = target.ftcPose.range - DESIRED_DISTANCE;
                double headingError = target.ftcPose.bearing;
                double yawError     = target.ftcPose.yaw;

                drive  = Range.clip(rangeError   * SPEED_GAIN,  -MAX_AUTO_SPEED,  MAX_AUTO_SPEED);
                turn   = Range.clip(headingError * TURN_GAIN,   -MAX_AUTO_TURN,   MAX_AUTO_TURN);
                strafe = Range.clip(-yawError    * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

                telemetry.addData("AUTO on tag", target.id);
                telemetry.addData("range err",   "%.1f", rangeError);

            } else {
                drive  = -gamepad1.left_stick_y  * 0.5;
                strafe =  gamepad1.left_stick_x  * 0.5;
                turn   =  gamepad1.right_stick_x * 0.3;
                telemetry.addLine(target == null ? "manual - no tag in view" : "manual");
            }

            double denom = Math.max(Math.abs(drive) + Math.abs(strafe)
                                  + Math.abs(turn), 1.0);
            frontLeft .setPower((drive + strafe + turn) / denom);
            backLeft  .setPower((drive - strafe + turn) / denom);
            frontRight.setPower((drive - strafe - turn) / denom);
            backRight .setPower((drive + strafe - turn) / denom);

            telemetry.update();
        }

        portal.close();
    }
}
