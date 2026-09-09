// FRAGMENT - not an OpMode. Paste this into your own file.

// Control Hub IMU setup.
//
// LogoFacingDirection  = which way the REV logo on the hub points
// UsbFacingDirection   = which way the USB ports point
//
// Get this wrong and field-centric drive will be rotated by 90 or 180
// degrees and feel completely broken. Look at the actual hub on the
// actual robot before you fill these in.

import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RevHubOrientationOnRobot;

IMU imu = hardwareMap.get(IMU.class, "imu");

imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
        RevHubOrientationOnRobot.LogoFacingDirection.UP,
        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));

// Zero the heading once the robot is squared up on the field:
imu.resetYaw();

// Read it:
double headingDeg = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
double headingRad = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
