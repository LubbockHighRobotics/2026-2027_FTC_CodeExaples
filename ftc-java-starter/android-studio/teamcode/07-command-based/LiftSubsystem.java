/*
 * LiftSubsystem.java
 * ---------------------------------------------------------------
 * WHAT   The lift as a self-contained subsystem. Same logic as 02-mechanisms.
 * WHERE  07-command-based/README.md
 * NEEDS  One motor named lift, one servo named claw.
 * EDIT   The four constants at the top.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Rules this class follows, and yours should too:
 *   - No method in here ever blocks or sleeps.
 *   - update() is called exactly once per loop, always.
 *   - Public methods REQUEST things. They never wait for them.
 */
public class LiftSubsystem {

    public enum State { IDLE, RAISING, RELEASING, RETRACTING }

    public static final int    HIGH = 1800, TOLERANCE = 20;   // EDIT
    public static final double OPEN = 0.6,  CLOSED = 0.2;     // EDIT

    private final DcMotor lift;
    private final Servo   claw;
    private final ElapsedTime timer = new ElapsedTime();

    private State state = State.IDLE;

    public LiftSubsystem(HardwareMap hw) {
        lift = hw.get(DcMotor.class, "lift");
        claw = hw.get(Servo.class,   "claw");
    }

    /** Call once, before the match starts. */
    public void init() {
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        claw.setPosition(CLOSED);
        state = State.IDLE;
    }

    // ---------------- requests ----------------

    /** Asks the lift to run its scoring sequence. Returns immediately. */
    public void scoreHigh() {
        if (state != State.IDLE) return;      // already busy, ignore
        lift.setTargetPosition(HIGH);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(0.8);
        state = State.RAISING;
    }

    /** Cancels whatever is happening and returns to the bottom. */
    public void abort() {
        claw.setPosition(CLOSED);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(0.6);
        state = State.RETRACTING;
    }

    public boolean isIdle()  { return state == State.IDLE; }
    public State   getState(){ return state; }
    public int     position(){ return lift.getCurrentPosition(); }

    // ---------------- the loop ----------------

    /** Call this once per loop pass, no matter what else is going on. */
    public void update() {
        switch (state) {

            case IDLE:
                break;

            case RAISING:
                if (Math.abs(lift.getCurrentPosition() - HIGH) < TOLERANCE) {
                    claw.setPosition(OPEN);
                    timer.reset();
                    state = State.RELEASING;
                }
                break;

            case RELEASING:
                if (timer.seconds() > 0.3) {
                    claw.setPosition(CLOSED);
                    lift.setTargetPosition(0);
                    state = State.RETRACTING;
                }
                break;

            case RETRACTING:
                if (Math.abs(lift.getCurrentPosition()) < TOLERANCE) {
                    lift.setPower(0);
                    state = State.IDLE;
                }
                break;
        }
    }
}
