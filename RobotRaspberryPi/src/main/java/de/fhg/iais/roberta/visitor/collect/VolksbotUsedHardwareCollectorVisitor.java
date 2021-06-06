package de.fhg.iais.roberta.visitor.collect;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean.IBuilder;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateLeft;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateRight;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepBackward;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepForward;
import de.fhg.iais.roberta.syntax.lang.blocksequence.raspberrypi.MainTaskSimple;
import de.fhg.iais.roberta.visitor.hardware.IVolksbotVisitor;

/**
 * This visitor collects information for used actors and sensors in Blockly program.
 *
 * @author kcvejoski
 * @param <V>
 */
public final class VolksbotUsedHardwareCollectorVisitor extends RaspberryPiUsedHardwareCollectorVisitor
    implements IRaspberryPiCollectorVisitor, IVolksbotVisitor<Void> {

    public VolksbotUsedHardwareCollectorVisitor(
        List<List<Phrase<Void>>> phrasesSet,
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IBuilder<?>> beanBuilders) {
        super(phrasesSet, brickConfiguration, beanBuilders);
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
