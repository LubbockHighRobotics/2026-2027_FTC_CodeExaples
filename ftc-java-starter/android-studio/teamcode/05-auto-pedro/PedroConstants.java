/*
 * PedroConstants.java
 * ---------------------------------------------------------------
 * WHAT   Every number that is specific to YOUR robot, in one place.
 * WHERE  Packet page 17
 * NEEDS  Nothing - this is just constants.
 * EDIT   All of it. That is the point of the file.
 * ---------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

/**
 * Pedro builds its Follower from a Constants object whose exact API changes
 * between versions. Rather than guess at that API, keep YOUR measured numbers
 * here and feed them into whatever Constants file ships with the Pedro
 * quickstart you installed.
 *
 * Fill this in during tuning and COMMIT IT. These numbers are the most
 * expensive thing your team will produce all season and they live in one file.
 */
public class PedroConstants {

    // ---- hardware names (must match the configuration file) ----
    public static final String FRONT_LEFT  = "frontLeft";
    public static final String BACK_LEFT   = "backLeft";
    public static final String FRONT_RIGHT = "frontRight";
    public static final String BACK_RIGHT  = "backRight";
    public static final String IMU_NAME    = "imu";

    // ---- physical ----
    public static final double ROBOT_MASS_KG = 12.0;   // weigh it, don't guess

    // ---- measured during tuning (Pedro's tuning OpModes print these) ----
    public static final double FORWARD_MULTIPLIER = 1.0;
    public static final double LATERAL_MULTIPLIER = 1.0;
    public static final double TURN_MULTIPLIER    = 1.0;

    // ---- follower PIDs (from the tuning steps, in the documented order) ----
    public static final double TRANSLATIONAL_P = 0.1, TRANSLATIONAL_I = 0.0, TRANSLATIONAL_D = 0.0;
    public static final double HEADING_P       = 1.0, HEADING_I       = 0.0, HEADING_D       = 0.0;
    public static final double DRIVE_P         = 0.02, DRIVE_I        = 0.0, DRIVE_D        = 0.0;

    // ---- localizer ----
    // One of: DRIVE_ENCODERS, DEAD_WHEELS, PINPOINT, OTOS
    public static final String LOCALIZER = "DRIVE_ENCODERS";

    // ---- field poses, in inches. Fill these in after the game reveal. ----
    // A pose is (x, y, heading). Keep them here so paths read cleanly.
    public static final double START_X = 9,  START_Y = 60, START_H_DEG = 0;
    public static final double SCORE_X = 37, SCORE_Y = 50, SCORE_H_DEG = 180;
    public static final double PARK_X  = 60, PARK_Y  = 15, PARK_H_DEG  = 90;

    private PedroConstants() { }   // never instantiated
}
