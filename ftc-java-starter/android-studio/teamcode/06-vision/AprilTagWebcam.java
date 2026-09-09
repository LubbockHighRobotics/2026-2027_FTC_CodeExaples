/*
 * AprilTagWebcam.java
 * ---------------------------------------------------------------
 * WHAT   Reads AprilTags with a USB webcam and prints id, range, bearing and yaw.
 * WHERE  Packet page 20
 * NEEDS  A USB webcam named webcam1 in the config. Works in OnBot Java too.
 * EDIT   The camera name, and the exposure numbers once you're on a real field.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "06 AprilTag - webcam", group = "Vision")
public class AprilTagWebcam extends LinearOpMode {

    @Override
    public void runOpMode() {

        AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .build();

        VisionPortal portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "webcam1"))   // EDIT
                .setCameraResolution(new Size(640, 480))
                .addProcessor(tagProcessor)
                .build();

        setManualExposure(portal, 6, 250);

        telemetry.addLine("Hold a printed tag in front of the camera.");
        telemetry.addLine("Watch range drop as you walk it in.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            boolean sawAny = false;

            for (AprilTagDetection d : tagProcessor.getDetections()) {
                if (d.metadata == null) continue;      // tag not in the field library
                sawAny = true;
                telemetry.addLine();
                telemetry.addData("id",      d.id);
                telemetry.addData("range",   "%.1f in",  d.ftcPose.range);
                telemetry.addData("bearing", "%.1f deg", d.ftcPose.bearing);
                telemetry.addData("yaw",     "%.1f deg", d.ftcPose.yaw);
            }

            if (!sawAny) telemetry.addLine("no tags in view");
            telemetry.update();
        }

        portal.close();
    }

    /** Fixed low exposure kills motion blur. This is the fix for most
     *  "it won't detect" problems -- far more often than the code is. */
    void setManualExposure(VisionPortal portal, int exposureMs, int gain) {
        while (!isStopRequested()
               && portal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            sleep(20);
        }
        if (isStopRequested()) return;

        ExposureControl exposure = portal.getCameraControl(ExposureControl.class);
        if (exposure.getMode() != ExposureControl.Mode.Manual) {
            exposure.setMode(ExposureControl.Mode.Manual);
            sleep(50);
        }
        exposure.setExposure((long) exposureMs, TimeUnit.MILLISECONDS);
        sleep(20);
        portal.getCameraControl(GainControl.class).setGain(gain);
        sleep(20);
    }
}
