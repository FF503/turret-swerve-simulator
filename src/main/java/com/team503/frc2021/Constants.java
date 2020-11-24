package com.team503.frc2021;

public class Constants {
    /* Navigator Constants: all length dimensions in INCHES */
    public static final int kFieldWidth = 324;
    public static final int kFieldLength = 648;
    public static final int kRobotSize = 30;
    public static final int kWalkFrequency = 5;
    public static final int KWalkMax = 40;
    public static final int KNoOfBeacons = 6;
    public static final int ballDiameter = 9;
    public static final double ballSpeed = 1;
    public static final double robotSpeed = 5;

    /* Turret Forward Kinematics */
    public static final double kTurretLoad = 13;
    public static final double kTurretMaxAcceleration = 100000000;
    public static final double kTurretMaxVelocity = 429.6;

    /* Turret PID Constants */
    public static final double kTurretP = 2.04;
    public static final double kTurretI = 0.00000;
    public static final double kTurretD = 0.035;

    /* Translation Forward Kinematics */
    public static final double kTranslationLoad = 13;
    public static final double kTranslationMaxAcceleration = 4000;
    public static final double kTranslationMaxVelocity = 600.0;

    /* Robot Rotational Forward Kinematics */
    public static final double kRotationLoad = 13;
    public static final double kRotationMaxAcceleration = 2000;
    public static final double kRotationMaxVelocity = 300.0;
}