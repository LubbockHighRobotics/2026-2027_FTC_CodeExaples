/*
 * PEDRO VERSION NOTE - read this before you paste
 * ---------------------------------------------------------------
 * The imports below match Pedro 2.x, where Pose and BezierLine live in
 * com.pedropathing.geometry and PathChain lives in com.pedropathing.paths.
 * Pedro 1.x used com.pedropathing.localization and com.pedropathing.pathgen,
 * and built the follower with `new Follower(hardwareMap, FConstants.class,
 * LConstants.class)` instead of `Constants.createFollower(hardwareMap)`.
 *
 * If an import line is red in Android Studio: delete it, put the cursor
 * on the red class name, press Alt+Enter, and let Android Studio import
 * the right one. Do not fight the import lines - check pedropathing.com
 * for the version you actually installed.
 * ---------------------------------------------------------------
 */
/*
 * AutoPedroTwoPath.java
 * ---------------------------------------------------------------
 * WHAT   Rung three. Drives one curve, pauses, drives another. No blocking anywhere.
 * WHERE  Packet page 18
 * NEEDS  Android Studio, Pedro installed and TUNED, and a localizer.
 * EDIT   The three poses. Get them from PedroConstants or the visualizer.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;

@Autonomous(name = "05 Auto - Pedro two paths", group = "Auto")
public class AutoPedroTwoPath extends OpMode {

    private Follower  follower;
    private PathChain scorePath, parkPath;
    private Timer     stateTimer;
    private int       pathState;

    // Poses are (x, y, heading) in field inches. Draw these at
    // visualizer.pedropathing.com and copy the numbers out.
    private final Pose startPose = new Pose(9,  60, Math.toRadians(0));
    private final Pose scorePose = new Pose(37, 50, Math.toRadians(180));
    private final Pose parkPose  = new Pose(60, 15, Math.toRadians(90));

    public void buildPaths() {

        scorePath = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(
                        startPose.getHeading(), scorePose.getHeading())
                .build();

        parkPath = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, parkPose))
                .setLinearHeadingInterpolation(
                        scorePose.getHeading(), parkPose.getHeading())
                .build();
    }

    /** Same state machine idea as packet page 11: never wait, always check. */
    public void autonomousPathUpdate() {
        switch (pathState) {

            case 0:
                follower.followPath(scorePath);
                setPathState(1);
                break;

            case 1:
                if (!follower.isBusy()) {          // arrived
                    setPathState(2);
                }
                break;

            case 2:
                if (stateTimer.getElapsedTimeSeconds() > 0.5) {
                    follower.followPath(parkPath);
                    setPathState(3);
                }
                break;

            case 3:
                if (!follower.isBusy()) setPathState(-1);   // done
                break;
        }
    }

    public void setPathState(int state) {
        pathState = state;
        stateTimer.resetTimer();
    }

    @Override
    public void init() {
        stateTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    @Override
    public void start() {
        setPathState(0);
    }

    @Override
    public void loop() {
        follower.update();              // must be called every single loop
        autonomousPathUpdate();

        telemetry.addData("path state", pathState);
        telemetry.addData("pose", follower.getPose());
        telemetry.update();
    }
}
