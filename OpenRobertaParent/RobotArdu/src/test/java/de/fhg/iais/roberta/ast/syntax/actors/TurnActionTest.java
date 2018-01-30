package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForTest;

public class TurnActionTest {

    HelperBotNrollForTest h = new HelperBotNrollForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void turn() throws Exception {
        final String a = "\none.movePID(50,-50);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorDiffTurn.xml", false);
    }

    @Test
    public void turnFor() throws Exception {
        final String a = "\nbnr.moveTimePID(50,-50,20);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorDiffTurnFor.xml", false);
    }
}