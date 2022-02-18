package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;

public class FestobionicflowerUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IFestobionicflowerCollectorVisitor {
    public FestobionicflowerUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }

    @Override
    public Void visitStepMotorAction(StepMotorAction<Void> stepMotorAction) {
        stepMotorAction.getStepMotorPos().accept(this);
        return null;
    }
}
