package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateLeft;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateRight;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepBackward;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepForward;
import de.fhg.iais.roberta.syntax.lang.blocksequence.raspberrypi.MainTaskSimple;
import de.fhg.iais.roberta.visitor.hardware.IVolksbotVisitor;

public class VolksbotUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IRaspberryPiCollectorVisitor, IVolksbotVisitor<Void> {
    public VolksbotUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }

    @Override
    public Void visitStepForward(StepForward<Void> stepForward) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitStepBackward(StepBackward<Void> stepBackward) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitRotateRight(RotateRight<Void> rotateRight) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitRotateLeft(RotateLeft<Void> rotateLeft) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitMainTaskSimple(MainTaskSimple<Void> mainTaskSimple) {
        // TODO Auto-generated method stub
        return null;
    }

}
