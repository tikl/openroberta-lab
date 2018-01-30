package de.fhg.iais.roberta.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class DriveActionTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void drive() throws Exception {
        String a = "\nhal.regulatedDrive(DriveDirection.FOREWARD, 50);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorDiffOn.xml");
    }

    @Test
    public void driveFor() throws Exception {
        String a = "\nhal.driveDistance(DriveDirection.FOREWARD, 50, 20);}";

        this.h.assertCodeIsOk(a, "/syntax/actions/action_MotorDiffOnFor.xml");
    }
}