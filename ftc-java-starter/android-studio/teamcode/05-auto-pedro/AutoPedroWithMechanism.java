/*
 * PEDRO VERSION NOTE - read this before you paste
 * ---------------------------------------------------------------
 * Pedro's import PACKAGES have moved between versions (Pose, BezierLine,
 * PathChain and Follower have all lived in more than one package).
 * The CLASS NAMES and the structure below are stable.
 *
 * If an import line is red in Android Studio: delete it, put the cursor
 * on the red class name, press Alt+Enter, and let Android Studio import
 * the right one. Do not fight the import lines - check pedropathing.com
 * for the version you actually installed.
 * ---------------------------------------------------------------
 */
/*
 * AutoPedroWithMechanism.java
 * ---------------------------------------------------------------
 * WHAT   Path, then run a mechanism, then path. The auto everyone actually wants.
 * WHERE  Packet page 18
 * NEEDS  Pedro tuned, plus a lift motor and claw servo.
 * EDIT   Poses, and your lift/claw constants.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;

@Autonomous(name = "05 Auto - Pedro + mechanism", group = "Auto")
public class AutoPedroWithMechanism extends OpMode {

    static final int    HIGH = 1800, TOLERANCE = 20;
    static final double OPEN = 0.6,  CLOSED = 0.2;

    private Follower  follower;
    private PathChain toScore, toPark;
    private Timer     stateTimer;
    private int       pathState;

    private DcMotor lift;
    private Servo   claw;

    private final Pose startPose = new Pose(9,  60, Math.toRadians(0));
    private final Pose scorePose = new Pose(37, 50, Math.toRadians(180));
    private final Pose parkPose  = new Pose(60, 15, Math.toRadians(90));

    public void buildPaths() {
        toScore = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();
        toPark = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, parkPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {

            case 0:                                  // drive and raise together
                follower.followPath(toScore);
                lift.setTargetPosition(HIGH);
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lift.setPower(0.8);
                setPathState(1);
                break;

            case 1:                                  // wait for BOTH to finish
                if (!follower.isBusy()
                        && Math.abs(lift.getCurrentPosition() - HIGH) < TOLERANCE) {
                    claw.setPosition(OPEN);
                    setPathState(2);
                }
                break;

            case 2:                                  // let the servo travel
                if (stateTimer.getElapsedTimeSeconds() > 0.35) {
                    claw.setPosition(CLOSED);
                    lift.setTargetPosition(0);
                    follower.followPath(toPark);
                    setPathState(3);
                }
                break;

            case 3:
                if (!follower.isBusy()) {
                    lift.setPower(0);
                    setPathState(-1);
                }
                break;
        }

        // Safety net: if anything jams, park anyway rather than score zero.
        if (pathState != -1 && stateTimer.getElapsedTimeSeconds() > 8) {
            follower.followPath(toPark);
            setPathState(3);
        }
    }

    public void setPathState(int state) {
        pathState = state;
        stateTimer.resetTimer();
    }

    @Override
    public void init() {
        stateTimer = new Timer();
        lift = hardwareMap.get(DcMotor.class, "lift");
        claw = hardwareMap.get(Servo.class,   "claw");
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        claw.setPosition(CLOSED);

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    @Override public void start() { setPathState(0); }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();
        telemetry.addData("path state", pathState);
        telemetry.addData("lift", lift.getCurrentPosition());
        telemetry.addData("pose", follower.getPose());
        telemetry.update();
    }
}
