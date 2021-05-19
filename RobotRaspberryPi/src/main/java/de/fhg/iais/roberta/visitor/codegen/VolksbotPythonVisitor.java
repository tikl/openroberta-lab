package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;
import java.util.Locale;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.MotorRaspOnAction;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.ServoRaspOnAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.raspberrypi.MainTaskSimple;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class VolksbotPythonVisitor extends RaspberryPiPythonVisitor {

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     */
    public VolksbotPythonVisitor(List<List<Phrase<Void>>> programPhrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, brickConfiguration, beans);
    }

    @Override
    public Void visitMotorRaspOnAction(MotorRaspOnAction<Void> motorRaspOnAction) {
        this.sb.append("Drive(keyhandle, 400000, 400000, 1000, 1000)");
        nlIndent();
        this.sb.append("Drive(keyhandle, -400000, -400000, 1000, 1000)");
        nlIndent();
        this.sb.append("Drive(keyhandle, 400000, 400000, 2000, 2000)");
        nlIndent();
        this.sb.append("Drive(keyhandle, 160000, -160000, 1000, 1000)");
        nlIndent();
        this.sb.append("Drive(keyhandle, 500000, 500000, 2000, 2000)");
        nlIndent();
        this.sb.append("Drive(keyhandle, 160000, -160000, 1000, 1000)");
        return null;
    }

    @Override
    public Void visitServoRaspOnAction(ServoRaspOnAction<Void> servoRaspOnAction) {
        if ( servoRaspOnAction.getProperty().getBlockType().equals("robActions_servo_motor_on_rasp") ) {
            this.sb.append("motor_servo_");
        } else {
            this.sb.append("motor_servo_angular_");
        }
        this.sb.append(servoRaspOnAction.getUserDefinedPort()).append(".");
        this.sb.append(servoRaspOnAction.getMode().toString().toLowerCase(Locale.ROOT) + "()");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.usedGlobalVarInFunctions.clear();
        this.sb.append("#!/usr/bin/python");
        nlIndent();
        nlIndent();
        this.sb.append("from __future__ import absolute_import");
        nlIndent();
        this.sb.append("import sys");
        nlIndent();
        this.sb.append("sys.path.append('/home/pi/epos4.py')");
        nlIndent();
        this.sb.append("from epos4 import *");
        nlIndent();
        this.sb.append("import time");
        nlIndent();
        this.sb.append("import gpiozero as gpz");
        nlIndent();
        this.sb.append("import colorzero as cz");
        nlIndent();
        this.sb.append("import math");
        nlIndent();
        nlIndent();
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                this
                    .getBean(CodeGeneratorSetupBean.class)
                    .getHelperMethodGenerator()
                    .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.sb.append(helperMethodImpls);
            nlIndent();
            nlIndent();
        }
        this.sb.append("class BreakOutOfALoop(Exception): pass");
        nlIndent();
        this.sb.append("class ContinueLoop(Exception): pass");
        nlIndent();
    }

    @Override
    public Void visitMainTaskSimple(MainTaskSimple<Void> mainTask) {
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("def run(keyhandle, pErrorCode):");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.sb.append("def main():");
        incrIndentation();
        nlIndent();
        this.sb.append("try:");
        incrIndentation();
        nlIndent();
        this.sb.append("pErrorCode = c_uint()");
        nlIndent();
        this.sb.append("keyhandle = 0");
        nlIndent();
        this.sb.append("keyhandle = openDevice()");
        nlIndent();
        this.sb.append("activatePPM(keyhandle, NodeID_1, pErrorCode)");
        nlIndent();
        this.sb.append("activatePPM(keyhandle, NodeID_2, pErrorCode)");
        nlIndent();
        this.sb.append("setState(keyhandle, 'enable')");
        nlIndent();
        this.sb.append("run(keyhandle, pErrorCode)");
        decrIndentation();
        nlIndent();
        this.sb.append("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.sb.append("print('Fehler im RaspberryPi')");
        nlIndent();
        this.sb.append("print(e.__class__.__name__)");
        nlIndent();
        // FIXME: we can only print about 30 chars
        this.sb.append("print(e)");
        decrIndentation();
        nlIndent();
        this.sb.append("finally:");
        incrIndentation();
        nlIndent();
        this.sb.append("if keyhandle != 0:");
        incrIndentation();
        nlIndent();
        this.sb.append("setState(keyhandle, 'disable')");
        nlIndent();
        this.sb.append("closeDevice(keyhandle, pErrorCode)");
        decrIndentation();
        decrIndentation();
        nlIndent();
        decrIndentation();
        decrIndentation();
        nlIndent();
        nlIndent();
        this.sb.append("if __name__ == \"__main__\":");
        incrIndentation();
        nlIndent();
        this.sb.append("main()");

    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.sb.append("motor_servo_angular_").append(motorOnAction.getUserDefinedPort()).append(".angle = ");
        motorOnAction.getParam().getSpeed().accept(this);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        this.sb.append("motor_servo_").append(motorSetPowerAction.getUserDefinedPort()).append(".value = ");
        motorSetPowerAction.getPower().accept(this);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String prefix = "motor_";
        if ( motorStopAction.getProperty().getBlockType().equals("robActions_robot_stop_rasp") ) {
            prefix = "robot_";
        }
        this.sb.append(prefix).append(motorStopAction.getUserDefinedPort()).append(".stop()");
        return null;
    }
}
