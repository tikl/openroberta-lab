package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicflowerVisitor;

public final class FestobionicflowerValidatorVisitor extends AbstractProgramValidatorVisitor implements IFestobionicflowerVisitor<Void> {

    private boolean main = false;

    public FestobionicflowerValidatorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
	protected void checkSensorPort(ExternalSensor<Void> sensor) {
		// TODO Auto-generated method stub
		
	}
    
    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitStepMotorAction(StepMotorAction<Void> stepMotorAction) {
        // TODO Auto-generated method stub
        return null;
    }

}
