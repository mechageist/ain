package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.drivebase.HDrive;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "the")
public class The extends LinearOpMode {
    private static final double LIFTY_POWER = 1.0;

    private boolean dpadUpPressed = false;
    private boolean dpadRightPressed = false;
    private boolean dpadLeftPressed = false;
    private Servo intakeElevation;
    private Motor intake;

    @Override
    public void runOpMode() {
        Gamepad gamepad1 = this.gamepad1;
        Gamepad gamepad2 = this.gamepad2;

        Motor hanger = new Motor(hardwareMap, "hanger");
        Motor claw = new Motor(hardwareMap, "claw");
        Motor lifty = new Motor(hardwareMap, "lifty");
        Motor intake = new Motor(hardwareMap, "intake");
        Servo intake_elevation = hardwareMap.get(Servo.class, "intake_elevation");
        /**Servo grab1 = hardwareMap.get(Servo.class, "grab1");
        Servo grab2 = hardwareMap.get(Servo.class, "grab2");**/
        Servo drone = hardwareMap.get(Servo.class, "drone");
        Motor front_left = new Motor(hardwareMap, "front_left");
        Motor front_right = new Motor(hardwareMap, "front_right");
        Motor back_left = new Motor(hardwareMap, "back_left");
        Motor back_right = new Motor(hardwareMap, "back_right");
        HDrive drive = new HDrive(front_left, front_right, back_left, back_right);

        waitForStart();
        intake.setPositionTolerance(1);
        intake_elevation.setPosition(0);
        drone.setPosition(0.0);

        while (opModeIsActive()) {
            initializeMotorsAndServos(hanger, claw, lifty, intake, intake_elevation
                   /** grab1, grab2**/, drone, front_left, front_right, back_left, back_right);

            hangerControl(hanger);
            liftyControl(lifty);
            drivetrainControl(drive);
            intakeElevationControl(intake_elevation);
            /**
            clawControl(grab1, grab2);
             **/
            droneControl(drone);

            double intakePower = gamepad2.right_trigger > 0.8 ? 1.0 : 0.9;
            intake.set(0);



            stopAllMotorsAndServos(hanger, lifty, claw, intake,
                    front_left, front_right, back_left, back_right);
            stopAllServos(intake_elevation /**grab1, grab2**/, drone);
        }
    }

    private void initializeMotorsAndServos(Motor hanger, Motor claw, Motor lifty, Motor intake,
                                           Servo intakeElevation/** Servo grab1, Servo grab2**/,
                                           Servo drone, Motor frontLeft, Motor frontRight,
                                           Motor backLeft, Motor backRight) {

        hanger.setRunMode(Motor.RunMode.VelocityControl);


       /** claw.setRunMode(Motor.RunMode.VelocityControl);**/


        lifty.setRunMode(Motor.RunMode.VelocityControl);


        intake.setRunMode(Motor.RunMode.RawPower);
        intake.set(0);

        frontLeft.setRunMode(Motor.RunMode.RawPower);
        frontLeft.set(0);



        intakeElevation.setPosition(0.0);
/**
        grab1.setPosition(0.0);
        grab2.setPosition(0.0);
**/
        drone.setPosition(0.0);
    }

    private void stopAllMotorsAndServos(Motor... motors) {
        for (Motor motor : motors) {
            motor.set(0);
        }
    }

    private void stopAllServos(Servo... servos) {
        for (Servo servo : servos) {
            servo.setPosition(0);
        }
    }

    private void hangerControl(Motor hanger) {
        hanger.setRunMode(Motor.RunMode.VelocityControl);


        double hangerPower = 1.0;

        if (gamepad1.dpad_up && !dpadUpPressed) {
            hanger.set(hangerPower);
            dpadUpPressed = true;
        } else if (!gamepad1.dpad_up) {
            dpadUpPressed = false;
            hanger.set(0);
        } else {
            hanger.set(hangerPower);
        }
    }
    private void liftyControl(Motor lifty) {
        lifty.setRunMode(Motor.RunMode.VelocityControl);

        double leftStickY = -gamepad2.left_stick_x;

        double targetVelocity = LIFTY_POWER * leftStickY;

        lifty.set(targetVelocity);
    }
/**
    private void drivetrainControl(HDrive drive) {
        double driveY = gamepad1.left_stick_y;
        double turnX = gamepad1.right_stick_x;


        double maxPower = 1.0;

        double forwardPower = Range.clip(driveY / maxPower, -1, 1);
        double turnPower = Range.clip(turnX / maxPower, -1, 1);


        drive.setMaxSpeed(forwardPower);

        sleep(20);
    }
**/
private void drivetrainControl(HDrive drive) {
    this.intakeElevation = intakeElevation;
    this.intake = intake;
    double elevationPower = gamepad2.right_stick_y;


    double newPosition = intakeElevation.getPosition() + elevationPower * 0.02;
    newPosition = Range.clip(newPosition, 0.0, 1.0);
    intakeElevation.setPosition(newPosition);


    double intakePower = gamepad2.right_trigger;
    intakePower = Range.clip(intakePower, 0.0, 1.0);
    intake.set(intakePower);
}



    private void intakeElevationControl(Servo intakeElevation) {
        double position = intakeElevation.getPosition();
        double newPosition = position + gamepad2.left_stick_y * 0.4;

        newPosition = Range.clip(newPosition, 0.4, 1.0);

        if (newPosition < 0.4) {
            newPosition = Range.clip(newPosition, 0.4, 0.6);
        } else if (newPosition < 0.8) {
            newPosition = Range.clip(newPosition, 0.6, 0.8);
        } else if (newPosition < 1.2) {
            newPosition = Range.clip(newPosition, 1.0, 1.2);
        } else if (newPosition < 1.6) {
            newPosition = Range.clip(newPosition, 1.4, 1.6);
        } else {
            newPosition = Range.clip(newPosition, 1.6, 1.8);
        }

        intakeElevation.setPosition(newPosition);
    }
/**
    private void clawControl(Servo grab1, Servo grab2) {
        if (gamepad2.dpad_right && !dpadRightPressed) {
            grab1.setPosition(1.0);
            dpadRightPressed = true;
        } else if (gamepad2.dpad_up && !gamepad2.dpad_right) {
            grab1.setPosition(0.0);
            dpadRightPressed = false;
        }

        if (gamepad2.dpad_left && !dpadLeftPressed) {
            grab2.setPosition(1.0);
            dpadLeftPressed = true;
        } else if (gamepad2.dpad_down && !gamepad2.dpad_left) {
            grab2.setPosition(0.0);
            dpadLeftPressed = false;
        }
    }
**/
private void intakeElevationControl(Servo intakeElevation, Motor intake) {
    double elevationPower = gamepad2.right_stick_y;
    double intakePower = gamepad2.right_trigger > 1.0 ? 1.0 : 0.9;


    double maxPower = 1.0;


    double newPosition = intakeElevation.getPosition() + elevationPower * 1.0;
    newPosition = Range.clip(newPosition, 0.4, 1.0);
    intakeElevation.setPosition(newPosition);


    intakePower = Range.clip(intakePower / maxPower, 1, 1);
    intake.set(intakePower);
}
    private void droneControl(Servo drone) {
        if (gamepad1.dpad_right && !dpadRightPressed) {
            drone.setPosition(1.0);
            dpadRightPressed = true;
        } else if (!gamepad1.dpad_right) {
            dpadRightPressed = false;
        }
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
