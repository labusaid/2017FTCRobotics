package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Lathe on 12/27/2017.
 */

@Autonomous(name = "orangeAutoTemplate", group = "Autonomous")
@Disabled
public class orangeAutoTemplate extends LinearOpMode {
    //<editor-fold desc="Variable Definitions">
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
    private double FlipperPosition;
    private double TrayPosition;
    private double JewelPosition;
    private double StickPosition;
    //</editor-fold>

    //Constructor
    public orangeAutoTemplate() {
    }

    @Override
    public void runOpMode() throws InterruptedException {

        /** NORMALLY INIT GOES HERE */
        //<editor-fold desc="Setup Stuff">
        /** Hardware Mapping */
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
        TrayLeft = hardwareMap.servo.get("rl");
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
        LiftLeft.setDirection(DcMotor.Direction.FORWARD);
        LiftRight.setDirection(DcMotorSimple.Direction.REVERSE);
        IntakeLeft.setDirection(DcMotor.Direction.FORWARD);
        IntakeRight.setDirection(DcMotor.Direction.REVERSE);

        /* ---Setting Starting Values For Variables--- */
        //Servos
        FlipperPosition = 1;
        TrayPosition = .5;
        StickPosition = .5;

        /* ---Setting servo start positions--- */
        Flipper.setPosition(FlipperPosition);
        TrayLeft.setPosition(TrayPosition);
        TrayRight.setPosition(1-TrayPosition);
        Stick.setPosition(StickPosition);
        //</editor-fold>

        //<editor-fold desc="Vuforia">
        //Vuforia Setup
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //Constructor
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        //License Key
        params.vuforiaLicenseKey = "AROo2jD/////AAAAGTCXjsveTUZjixVfEqJ5sYmM4947vccKgcrdAmg6HE7RNuoDbXh9fjQuVzkws2UZ8yQeVie8VcmrttkfWX9AMm/cPffe1yUBzvE4sVbm3A+qFNcHA+GJ8TVwUGUC+n8ggyG8O5tTlkpHLqNAELsj6cfyybCcSf5o2riydxoQ3RMzwf/MWvgElphFDFnW3BHZsmSiJjTyIgbxzoCmzPscvBVWx10G8pm+06qnv2NN6u47MzU3bSEoFFJ6du5vZBPAulQQJK1CU1KEDAuiOg3vM9V5h91jSuvKYKDEMyzyPzxd2duEEGcSHVuS2kTmxv3uaQR/77Fqys67lgY/O/F8nrVKIxECXUSy3X3FRNWRIGgA";
        //Set Camera
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        //Define Object
        VuforiaLocalizer magic = ClassFactory.createVuforiaLocalizer(params);
        //Max Targets
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 1);
        //Trackables
        VuforiaTrackables paper = magic.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = paper.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        //</editor-fold>

        //Starting Positions
        Stick.setPosition(StickPosition);

        /** Main Loop */
        waitForStart();

        paper.activate();

        //Delay For Start
        sleep(800);


    }
    /** Functions */
    //Used for tank drive movement
    private void Move(double Left, double Right, int milis) {
        LeftFront.setPower(Left);
        LeftBack.setPower(Left);
        RightFront.setPower(Right);
        RightBack.setPower(Right);
        sleep(milis);
        StopAll();
    }

    //Stops all motors
    private void StopAll(){
        LeftFront.setPower(0);
        LeftBack.setPower(0);
        RightFront.setPower(0);
        RightBack.setPower(0);
        LiftLeft.setPower(0);
        LiftRight.setPower(0);
        IntakeLeft.setPower(0);
        IntakeRight.setPower(0);
    }

    //Used for strafing
    private void Strafe(double FrontPower, double BackPower, int milis) {
        LeftFront.setPower(-FrontPower);
        LeftBack.setPower(BackPower);
        RightFront.setPower(FrontPower);
        RightBack.setPower(-BackPower);
        sleep(milis);
        StopAll();
    }

    //End Of Program
}