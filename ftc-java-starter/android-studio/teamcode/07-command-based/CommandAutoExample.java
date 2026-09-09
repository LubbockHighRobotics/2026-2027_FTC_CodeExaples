/*
 * CommandAutoExample.java
 * ---------------------------------------------------------------
 * WHAT   A TeleOp and an Autonomous built from the two subsystems.
 * WHERE  07-command-based/README.md
 * NEEDS  Both subsystem files in the same package.
 * EDIT   Nothing, if your config matches the rest of the repo.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Two OpModes in one file so you can see how little changes between them.
 * Both do the same three things every loop:
 *     1. decide what should happen
 *     2. call update() on every subsystem
 *     3. print telemetry
 */
public class CommandAutoExample {

    // ============================================================
    @TeleOp(name = "07 Subsystems - TeleOp", group = "Command")
    public static class SubsystemTeleOp extends LinearOpMode {

        @Override
        public void runOpMode() {

            DriveSubsystem drive = new DriveSubsystem(hardwareMap);
            LiftSubsystem  lift  = new LiftSubsystem(hardwareMap);
            drive.init();
            lift.init();

            boolean lastToggle = false;

            waitForStart();

            while (opModeIsActive()) {

                // ---- 1. decide ----
                drive.setInput(-gamepad1.left_stick_y,
                                gamepad1.left_stick_x,
                                gamepad1.right_stick_x);
                drive.setSpeed(1.0 - 0.65 * gamepad1.left_trigger);

                if (gamepad1.options && !lastToggle) drive.toggleFieldCentric();
                lastToggle = gamepad1.options;
                if (gamepad1.back) drive.resetHeading();

                if (gamepad1.a) lift.scoreHigh();
                if (gamepad1.b) lift.abort();

                // ---- 2. update everything, every pass ----
                drive.update();
                lift.update();

                // ---- 3. report ----
                telemetry.addData("mode", drive.isFieldCentric() ? "FIELD" : "ROBOT");
                telemetry.addData("lift", lift.getState());
                telemetry.update();
            }
        }
    }

    // ============================================================
    @Autonomous(name = "07 Subsystems - Auto", group = "Command")
    public static class SubsystemAuto extends LinearOpMode {

        @Override
        public void runOpMode() {

            DriveSubsystem drive = new DriveSubsystem(hardwareMap);
            LiftSubsystem  lift  = new LiftSubsystem(hardwareMap);
            drive.init();
            lift.init();

            waitForStart();
            if (isStopRequested()) return;

            ElapsedTime timer = new ElapsedTime();
            int step = 0;

            // The auto is one more state machine, sitting on top of the
            // subsystems' own state machines. Nothing anywhere blocks.
            while (opModeIsActive() && step >= 0) {

                switch (step) {

                    case 0:                                  // drive forward
                        drive.setInput(0.4, 0, 0);
                        if (timer.seconds() > 1.2) { drive.setInput(0,0,0); timer.reset(); step = 1; }
                        break;

                    case 1:                                  // score
                        lift.scoreHigh();
                        step = 2;
                        break;

                    case 2:                                  // wait for the lift
                        if (lift.isIdle()) { timer.reset(); step = 3; }
                        break;

                    case 3:                                  // park
                        drive.setInput(0, 0.5, 0);
                        if (timer.seconds() > 0.8) { drive.setInput(0,0,0); step = -1; }
                        break;
                }

                drive.update();
                lift.update();

                telemetry.addData("step", step);
                telemetry.addData("lift", lift.getState());
                telemetry.update();
            }

            drive.stop();
        }
    }
}
