/*
 * AprilTagChooseRoutine.java
 * ---------------------------------------------------------------
 * WHAT   Reads a tag during INIT and picks one of three autos. Always has a fallback.
 * WHERE  Packet page 20
 * NEEDS  A webcam named webcam1, plus a drivetrain.
 * EDIT   The three tag ids, and what each routine does.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@Autonomous(name = "06 AprilTag - choose routine", group = "Vision")
public class AprilTagChooseRoutine extends LinearOpMode {

    static final int TAG_LEFT = 1, TAG_MIDDLE = 2, TAG_RIGHT = 3;   // EDIT

    DcMotor frontLeft, backLeft, frontRight, backRight;

    @Override
    public void runOpMode() {

        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft .setDirection(DcMotor.Direction.REVERSE);

        AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder().build();

        VisionPortal portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "webcam1"))
                .setCameraResolution(new Size(640, 480))
                .addProcessor(tagProcessor)
                .build();

        int seenId = -1;

        // Looking at a tag BEFORE the match starts is legal and is the most
        // common use of vision in FTC. opModeInInit() runs between INIT and PLAY.
        while (opModeInInit()) {
            for (AprilTagDetection d : tagProcessor.getDetections()) {
                if (d.metadata != null) seenId = d.id;
            }
            telemetry.addData("tag", seenId < 0 ? "none yet" : seenId);
            telemetry.addLine("Whatever is showing when you press PLAY is what runs.");
            telemetry.update();
        }

        waitForStart();
        if (isStopRequested()) return;

        // Stop streaming once we have the answer -- it frees up CPU.
        portal.close();

        if      (seenId == TAG_LEFT)   routineLeft();
        else if (seenId == TAG_MIDDLE) routineMiddle();
        else if (seenId == TAG_RIGHT)  routineRight();
        else                           routineFallback();
    }

    void routineLeft()   { drive(0.4, 0, 0, 1000); turnFor(0.4, 500); park(); }
    void routineMiddle() { drive(0.4, 0, 0, 1300); park(); }
    void routineRight()  { drive(0.4, 0, 0, 1000); turnFor(-0.4, 500); park(); }

    /** NEVER leave this out. An auto that does nothing when it can't see
     *  is worse than a time-based auto that always parks. */
    void routineFallback() {
        telemetry.addLine("No tag seen -- running the safe park.");
        telemetry.update();
        drive(0.4, 0, 0, 1200);
        park();
    }

    void park() { drive(0, 0.5, 0, 800); stopDriving(); }

    void turnFor(double power, long ms) { drive(0, 0, power, ms); }

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
