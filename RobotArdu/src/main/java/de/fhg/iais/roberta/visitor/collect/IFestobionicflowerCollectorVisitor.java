package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicflowerVisitor;

/**
 * Collector for the MBot. Adds the blocks missing from the defaults of {@link ICollectorVisitor}. Defines the specific parent implementation to use (the one of
 * the collector) due to unrelated defaults.
 */
public interface IFestobionicflowerCollectorVisitor extends ICollectorVisitor, IFestobionicflowerVisitor<Void> {

    @Override
    default Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        return null;
    }

    @Override
    default Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        ledOnAction.getLedColor().accept(this);
        return null;
    }
}
