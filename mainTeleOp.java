package org.firstinspires.ftc.teamcode;

import android.text.InputFilter;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Lathe on 12/27/2017.
 */

//Ctrl+Alt+T

@TeleOp(name = "mainTeleOp", group = "TeleOp")

public class mainTeleOp extends OpMode{
    //<editor-fold desc="Defining Variables">
    /* ---Hardware--- */
    //Motors
    private DcMotor LeftFront, LeftBack, RightFront, RightBack;
    private DcMotor LiftLeft, LiftRight;
    private DcMotor IntakeLeft, IntakeRight;
    //Servos
    private Servo Flipper;
    private Servo TrayLeft, TrayRight;
    private Servo Stick;
    //Sensors
    private ColorSensor Color;
    //private OpticalDistanceSensor LineSense;

    /* ---Variables--- */
    //Outputs
    private double LeftFrontPower;
    private double LeftBackPower;
    private double RightFrontPower;
    private double RightBackPower;
    private double LiftPower;
    private double IntakePower;
    private double FlipperPosition;
    private double TrayPosition;
    private double StickPosition;

    /* ---Servo Configuration--- */
    //Servo Positions

    //Servo Limits

    //Logic
    private int RightStickDirection;
    private int LeftStickDirection;
    //</editor-fold>

    //Constructor
    public mainTeleOp() {
    }

    @Override
    public void init() {
        /* ---Hardware Mapping--- */
        //Motors
        LeftFront = hardwareMap.dcMotor.get("LF");
        LeftBack = hardwareMap.dcMotor.get("LB");
        RightFront = hardwareMap.dcMotor.get("RF");
        RightBack = hardwareMap.dcMotor.get("RB");
        LiftLeft = hardwareMap.dcMotor.get("LL");
        LiftRight = hardwareMap.dcMotor.get("LR");
        IntakeLeft = hardwareMap.dcMotor.get("IL");
        IntakeRight = hardwareMap.dcMotor.get("IR");

        //Servos
        Flipper = hardwareMap.servo.get("f");
        TrayLeft = hardwareMap.servo.get("tl");
        TrayRight = hardwareMap.servo.get("tr");
        Stick = hardwareMap.servo.get("s");

        //Sensors
        Color = hardwareMap.colorSensor.get("Color");
        //LineSense = hardwareMap.opticalDistanceSensor.get("LineSense");

        /* ---Setting Motor Directions--- */
        LeftFront.setDirection(DcMotor.Direction.FORWARD);
        LeftBack.setDirection(DcMotor.Direction.FORWARD);
        RightFront.setDirection(DcMotor.Direction.REVERSE);
        RightBack.setDirection(DcMotor.Direction.REVERSE);
        LiftLeft.setDirection(DcMotor.Direction.REVERSE);
        LiftRight.setDirection(DcMotorSimple.Direction.FORWARD);
        IntakeLeft.setDirection(DcMotor.Direction.FORWARD);
        IntakeRight.setDirection(DcMotor.Direction.REVERSE);

        /* ---Setting Starting Values For Variables--- */
        //Motor Power
        LeftFrontPower = 0;
        LeftBackPower = 0;
        RightFrontPower = 0;
        RightBackPower = 0;
        LiftPower = 0;
        IntakePower = 0;

        //Servos
        FlipperPosition = 1;
        TrayPosition = .5;
        StickPosition = .3;

        //Logic
        //note this whole drive system is disgusting legacy code that I don't know how to fix
        RightStickDirection = -1;
        LeftStickDirection = -1;

        /* ---Setting servo start positions--- */
        Flipper.setPosition(FlipperPosition);
        TrayLeft.setPosition(TrayPosition);
        TrayRight.setPosition(1-TrayPosition);
        Stick.setPosition(StickPosition);
    }

    @Override
    public void loop() {
        /* ---Resets--- */
        //Servos

        //Reset Variables
        LeftFrontPower = 0;
        LeftBackPower = 0;
        RightFrontPower = 0;
        RightBackPower = 0;
        LiftPower = 0;
        IntakePower = 0;
        RightStickDirection = -1;
        LeftStickDirection = -1;

        /* ---Robot Functions and Inputs--- */
        //<editor-fold desc="Stick">
        if (gamepad1.dpad_up) {
            StickPosition = .1;
        }
        else if (gamepad1.dpad_left || gamepad1.dpad_right){
            StickPosition = .3;
        }
        else if (gamepad1.dpad_down) {
            StickPosition = 1;
        }
        //</editor-fold>

        //<editor-fold desc="Tray">
        if (gamepad2.a) {
            TrayPosition = 1;
        }
        else if (gamepad2.x || gamepad1.b){
            TrayPosition = .5;
        }
        else if (gamepad2.y) {
            TrayPosition = 0;
        }
        //</editor-fold>

        //<editor-fold desc="Intake">
        if(gamepad1.right_trigger > .2) {
            IntakePower = gamepad1.right_trigger;
        }
        else if (gamepad1.left_trigger > .2) {
            IntakePower = -gamepad1.left_trigger;
        }
        else IntakePower = 0;
        //</editor-fold>

        //<editor-fold desc="Lift">
        if(gamepad2.right_trigger > .2) {
            LiftPower = gamepad2.right_trigger;
        }
        else if (gamepad2.left_trigger > .2) {
            LiftPower = -gamepad2.left_trigger;
        }
        else LiftPower = 0;
        //</editor-fold>

        /* ---Drive--- */
        //<editor-fold desc="Drive Stuff">
        //Determining Left Stick Direction
        if (gamepad1.left_stick_y > .2){
            if (gamepad1.left_stick_x < .4 && gamepad1.left_stick_x > -.4){
                LeftStickDirection = 0;
            }
            else if (gamepad1.left_stick_x > .4){
                LeftStickDirection = 45;
            }
            else if (gamepad1.left_stick_x < -.4){
                LeftStickDirection = 315;
            }
        }
        else if (gamepad1.left_stick_y < -.2){
            if (gamepad1.left_stick_x < .4 && gamepad1.left_stick_x > -.4){
                LeftStickDirection = 180;
            }
            else if (gamepad1.left_stick_x > .4){
                LeftStickDirection = 135;
            }
            else if (gamepad1.left_stick_x < -.4){
                LeftStickDirection = 225;
            }
        }
        else if (gamepad1.left_stick_y < .2 && gamepad1.left_stick_y > -.2){
            if (gamepad1.left_stick_x < .2 && gamepad1.left_stick_x > -.2){
                LeftStickDirection = -1;
            }
            else if (gamepad1.left_stick_x > .4){
                LeftStickDirection = 90;
            }
            else if (gamepad1.left_stick_x < -.4){
                LeftStickDirection = 270;
            }
        }
        //Determining Right Stick Direction
        if (gamepad1.right_stick_y > .2){
            if (gamepad1.right_stick_x < .4 && gamepad1.right_stick_x > -.4){
                RightStickDirection = 0;
            }
            else if (gamepad1.right_stick_x > .4){
                RightStickDirection = 45;
            }
            else if (gamepad1.right_stick_x < -.4){
                RightStickDirection = 315;
            }
        }
        else if (gamepad1.right_stick_y < -.2){
            if (gamepad1.right_stick_x < .4 && gamepad1.right_stick_x > -.4){
                RightStickDirection = 180;
            }
            else if (gamepad1.right_stick_x > .4){
                RightStickDirection = 135;
            }
            else if (gamepad1.right_stick_x < -.4){
                RightStickDirection = 225;
            }
        }
        else if (gamepad1.right_stick_y < .2 && gamepad1.right_stick_y > -.2){
            if (gamepad1.right_stick_x < .4 && gamepad1.right_stick_x > -.4){
                RightStickDirection = -1;
            }
            else if (gamepad1.right_stick_x > .4){
                RightStickDirection = 90;
            }
            else if (gamepad1.right_stick_x < -.4){
                RightStickDirection = 270;
            }
        }
        //Translational Movement With Right Stick
        //Tank Drive
        if(LeftStickDirection == 0 || LeftStickDirection == 180 || RightStickDirection == 0 || RightStickDirection == 180) {
            //Left Side
            if (LeftStickDirection == 0 || LeftStickDirection == 180) {
                LeftFrontPower = gamepad1.left_stick_y;
                LeftBackPower = gamepad1.left_stick_y;
            }
            //Right Side
            if (RightStickDirection == 0 || RightStickDirection == 180){
                RightFrontPower = gamepad1.right_stick_y;
                RightBackPower = gamepad1.right_stick_y;
            }
        }
        //Strafing
        else if ((LeftStickDirection == 270 || LeftStickDirection == 90) && (RightStickDirection == 270 || RightStickDirection == 90)){
            LeftFrontPower = gamepad1.left_stick_x;
            LeftBackPower = -gamepad1.left_stick_x;
            RightFrontPower = -gamepad1.left_stick_x;
            RightBackPower = gamepad1.left_stick_x;
        }
        else {
            LeftFrontPower = 0;
            LeftBackPower = 0;
            RightFrontPower = 0;
            RightBackPower = 0;
        }
        //</editor-fold>

        /* ---Final Outputs--- */
        //Motors
        LeftFront.setPower(LeftFrontPower);
        LeftBack.setPower(LeftBackPower);
        RightFront.setPower(RightFrontPower);
        RightBack.setPower(RightBackPower);
        LiftLeft.setPower(LiftPower);
        LiftRight.setPower(LiftPower);
        IntakeLeft.setPower(IntakePower);
        IntakeRight.setPower(IntakePower);

        //Servos
        Flipper.setPosition(FlipperPosition);
        TrayLeft.setPosition(TrayPosition);
        TrayRight.setPosition(1-TrayPosition);
        Stick.setPosition(StickPosition);

        //<editor-fold desc="Telemetry">
        //Remember that there is a byte limit on how much telemetry can be sent.
        //It might be worth trying to alternate every cycle and see if that overrides the telemetry

        telemetry.addData("Red: ",Color.red());
        telemetry.addData("Blue: ", Color.blue());
        /*
        telemetry.addData("LineSense", LineSense.getRawLightDetected());

        //telemetry.addData("Telemetry", "------------Telemetry------------");
        telemetry.addData("LeftStickDirection", LeftStickDirection);
        telemetry.addData("RightStickDirection", RightStickDirection);

        //telemetry.addData("Motors", "------------Motors------------");
        telemetry.addData("LeftFrontPower", LeftFrontPower);
        telemetry.addData("LeftBackPower", LeftBackPower);
        telemetry.addData("RightFrontPower", RightFrontPower);
        telemetry.addData("RightBackPower", RightBackPower);

        //telemetry.addData("Servos", "------------Servos------------");
        telemetry.addData("StickPosition", StickPosition);
        */
        //</editor-fold>
    }

    @Override
    public void stop() {
        //Emergency Stop
        LeftFront.setPower(0);
        LeftBack.setPower(0);
        RightFront.setPower(0);
        RightBack.setPower(0);
        LiftLeft.setPower(0);
        LiftRight.setPower(0);
        IntakeLeft.setPower(0);
        IntakeRight.setPower(0);
    }
}