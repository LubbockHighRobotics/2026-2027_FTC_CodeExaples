/*
 * BlankSubsystem.java  -- rename this file and the class together.
 *
 * Rules: nothing in here blocks. update() is called once per loop.
 * Public methods request things; they never wait for them.
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class BlankSubsystem {

    public enum State { IDLE }

    private final ElapsedTime timer = new ElapsedTime();
    private State state = State.IDLE;

    public BlankSubsystem(HardwareMap hw) {
        // motor = hw.get(DcMotor.class, "name");
    }

    /** Call once before the match. */
    public void init() {
        state = State.IDLE;
    }

    // ---- requests ----

    public boolean isIdle()   { return state == State.IDLE; }
    public State   getState() { return state; }

    /** Call once per loop, always. */
    public void update() {
        switch (state) {
            case IDLE:
                break;
        }
    }
}
