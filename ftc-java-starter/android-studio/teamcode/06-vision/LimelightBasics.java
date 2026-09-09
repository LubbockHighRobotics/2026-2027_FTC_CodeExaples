/*
 * LimelightBasics.java
 * ---------------------------------------------------------------
 * WHAT   Reads whatever the Limelight's current pipeline sees.
 * WHERE  Packet page 21
 * NEEDS  A Limelight 3A named limelight, on an Ethernet-over-USB port.
 * EDIT   The pipeline number.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "06 Limelight - basics", group = "Vision")
public class LimelightBasics extends LinearOpMode {

    @Override
    public void runOpMode() {

        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

        // Telemetry refresh has to keep up with the camera or the numbers lag.
        telemetry.setMsTransmissionInterval(11);

        limelight.pipelineSwitch(0);   // EDIT: the pipeline you built in the browser
        limelight.start();

        telemetry.addLine("Press A / B / X to switch pipelines 0 / 1 / 2.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) limelight.pipelineSwitch(0);
            if (gamepad1.b) limelight.pipelineSwitch(1);
            if (gamepad1.x) limelight.pipelineSwitch(2);

            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                telemetry.addData("tx", "%.2f", result.getTx());   // left/right offset
                telemetry.addData("ty", "%.2f", result.getTy());   // up/down offset
                telemetry.addData("ta", "%.2f", result.getTa());   // target area
                telemetry.addData("botpose", result.getBotpose().toString());
                telemetry.addData("staleness ms", result.getStaleness());
            } else {
                telemetry.addLine("no valid target");
            }

            telemetry.update();
        }

        limelight.stop();
    }
}
