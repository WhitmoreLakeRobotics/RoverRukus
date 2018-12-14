package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;


@Autonomous(name = "Auton_Gold_Sample", group = "Auton")
// @Autonomous(...) is the other common choice

public class Auton_Gold_Sample extends OpMode {


    //declare and initialize stages
    private static final int stage0_preStart = 0;
    private static final int stage10_extened = 10;
    private static final int stage12_drive = 12;
    private static final int stage13_storeHanger = 13;
    private static final int stage14_prepPhotos = 14;
    private static final int stage15_doVision = 15;
    private static final int stage17_doSamplePosition = 17;
    private static final int stage18_intakeArmStart = 18;
    private static final int stage20_liftIntakeAarm = 20;
    private static final int stage30_drive = 30;
    private static final int stage35_empty = 35;
    private static final int stage40_empty = 40;
    private static final int stage50_backup = 50;
    private static final int stage60_turn90 = 60;
    private static final int stage70_drive2Side = 70;
    private static final int stage80_turn2Crater = 80;
    private static final int stage85_drive2Crater = 85;
    private static final int stage90_turnFinal = 90;
    private static final int stage99_stop = 99;
    private static final String TAGAuton_SDCS = "8492-AutonGoldrSample";
    // create instance of Chassis
    Chassis RBTChassis = new Chassis();
    private int currentStage = stage0_preStart;

    // declare auton power variables
    private double AUTO_DRIVEPower = .7;
    private double AUTO_TURNPower = .4;
    private double AUTO_DRIVEPower_HI = .85;

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        RBTChassis.setParentMode(Chassis.PARENTMODE.PARENT_MODE_AUTO);
        RBTChassis.hardwareMap = hardwareMap;
        RBTChassis.telemetry = telemetry;
        RBTChassis.init();
        msStuckDetectStart = 8000;
        telemetry.addData("Auton_Gold_Sample", "Initialized");
        // initialize chassis with hardware map
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        // initialize chassis
        RBTChassis.init_loop();
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        // initialize chassis
        Runtime.getRuntime();
        RBTChassis.start();
        RBTChassis.setMotorMode_RUN_WITHOUT_ENCODER();

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        telemetry.addData("Auton_Gold_Sample", currentStage);
        RobotLog.aa(TAGAuton_SDCS, "Runtime: " + runtime.seconds() + "Auton_Gold_Sample Stage" + currentStage);
        RBTChassis.loop();

        // check stage and do what's appropriate
        if (currentStage == stage0_preStart) {
            currentStage = stage10_extened;
        }


        if (currentStage == stage10_extened) {
            if (RBTChassis.scannerArms.getIsUp()) {
                RBTChassis.hanger.cmd_MoveToTarget(Hanger.HANGERPOS_EXNTENDED);
                currentStage = stage12_drive;
            }
        }


        if (currentStage == stage12_drive) {
            if (RBTChassis.hanger.isExtended()) {
                RBTChassis.cmdDrive(AUTO_DRIVEPower, 0, 4);
                currentStage = stage13_storeHanger;
            }
        }

        if (currentStage == stage13_storeHanger) {
            if (RBTChassis.getcmdComplete()) {
                RBTChassis.hanger.cmd_MoveToTarget(Hanger.HANGERPOS_RETRACTED);
                RBTChassis.intakeArm.cmd_movePivotToCarryPos();
                currentStage = stage14_prepPhotos;
            }
        }

        if (currentStage == stage14_prepPhotos) {
            if (RBTChassis.hanger.isRetracted()) {
                RBTChassis.intakeArm.cmd_movePivotToDumpPos();
                RBTChassis.scannerArms.cmdMoveAllDown();
                currentStage = stage15_doVision;
            }
        }

        if (currentStage == stage15_doVision) {
            if (RBTChassis.intakeArm.atPivotDestination(IntakeArmStates.IntakePivotDestinations.Dump)) {
                RBTChassis.mineralVision.startVision();
                currentStage = stage17_doSamplePosition;
            }
        }


        if (currentStage == stage17_doSamplePosition) {
            if (RBTChassis.mineralVision.getVisionComplete() == true) {
                if (RBTChassis.mineralVision.isGoldLeft()) {
                    RBTChassis.scannerArms.cmdMoveUpRight();

                } else {
                    RBTChassis.scannerArms.cmdMoveUpLeft();
                }


                if (RBTChassis.mineralVision.isGoldCenter()) {
                    currentStage = stage18_intakeArmStart;
                    RBTChassis.intakeArm.cmd_movePivotToStartPos();
                    RBTChassis.scannerArms.cmdMoveAllUp();
                } else {
                    currentStage = stage30_drive;
                }

                if (RBTChassis.mineralVision.isGoldRight()) {
                    RBTChassis.scannerArms.cmdMoveUpLeft();
                } else {
                    RBTChassis.scannerArms.cmdMoveUpRight();
                }

            }
        }

        if (currentStage == stage18_intakeArmStart) {
            if (RBTChassis.intakeArm.atPivotDestination(IntakeArmStates.IntakePivotDestinations.Start)) {
                currentStage = stage30_drive;
            }
        }


        if (currentStage == stage30_drive) {
            if (RBTChassis.scannerArms.getIsUpLeft() ||
                    RBTChassis.scannerArms.getIsUpRight()) {
                RBTChassis.cmdDrive(AUTO_DRIVEPower, 0, 48);
                currentStage = stage35_empty;
            }
        }

        if (currentStage == stage35_empty) {
            if (RBTChassis.getcmdComplete()) {
                //RBTChassis.hanger.cmd_MoveToTarget(Hanger.HANGERPOS_RETRACTED);
                RBTChassis.intakeArm.cmd_movePivotToStartPos();
                currentStage = stage40_empty;
            }
        }

        if (currentStage == stage40_empty) {
            if (RBTChassis.intakeArm.atPivotDestination(IntakeArmStates.IntakePivotDestinations.Start)) {
                RBTChassis.scannerArms.cmdMoveAllUp();
                RBTChassis.dumpBox.cmd_ServoAutoOut(1250);
                currentStage = stage50_backup;
            }
        }

        if (currentStage == stage50_backup) {
            if (RBTChassis.dumpBox.getServoMode() == DumpBox.BoxModes.BoxModes_Stop) {
                RBTChassis.intakeArm.cmd_movePivotToDumpPos();
                // driving backwards
                RBTChassis.cmdDrive(-AUTO_DRIVEPower, 0, 34.5);
                currentStage = stage60_turn90;
            }
        }

        if (currentStage == stage60_turn90) {
            if (RBTChassis.getcmdComplete()) {
                RBTChassis.cmdTurn(AUTO_TURNPower, -AUTO_TURNPower, 95);
                currentStage = stage70_drive2Side;
            }
        }

        if (currentStage == stage70_drive2Side) {
            if (RBTChassis.getcmdComplete()) {
                RBTChassis.cmdDrive(-AUTO_DRIVEPower, 95, -50);
                RBTChassis.scannerArms.cmdMoveStartLeft();
                currentStage = stage80_turn2Crater;
            }
        }

        if (currentStage == stage80_turn2Crater) {
            if (RBTChassis.getcmdComplete()) {
                RBTChassis.cmdTurn(-AUTO_TURNPower, AUTO_TURNPower, 45);
                currentStage = stage85_drive2Crater;
            }
        }


        if (currentStage == stage85_drive2Crater) {
            if (RBTChassis.getcmdComplete()) {
                // driving backwards
                RBTChassis.cmdDrive(-AUTO_DRIVEPower_HI, 45, -50);
                currentStage = stage99_stop;
            }
        }

        if (currentStage == stage90_turnFinal) {
            if (RBTChassis.getcmdComplete()) {
                RBTChassis.dumpBox.cmd_ServosIn();
                RBTChassis.cmdTurn(AUTO_TURNPower, -AUTO_TURNPower, 180);
                currentStage = stage99_stop;
            }
        }

        if (currentStage == stage99_stop) {
            if (runtime.seconds() > 25) {
                RBTChassis.dumpBox.cmd_ServosOff();
            }
        }


        //if (runtime.seconds() > 30) {
        //    stop();
        //}

    }  //  loop

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        RBTChassis.stop();
    }

}
