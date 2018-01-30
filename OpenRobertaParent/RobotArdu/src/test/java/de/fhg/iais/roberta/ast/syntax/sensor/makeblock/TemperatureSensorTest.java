package de.fhg.iais.roberta.ast.syntax.sensor.makeblock;

import org.junit.Assert;
import org.junit.Test;

import de.fag.iais.roberta.mode.sensor.arduino.mbot.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ardu.HelperMakeBlockForTest;

public class TemperatureSensorTest {
    HelperMakeBlockForTest h = new HelperMakeBlockForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void jaxbToAst_byDefault_temperatureSensorOnPort4() throws Exception {
        String a = "BlockAST [project=[[Location [x=113, y=88], TemperatureSensor [PORT_4, DEFAULT, EMPTY_SLOT]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_temperature.xml"));
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_temperature.xml");

        TemperatureSensor<Void> cs = (TemperatureSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(SensorPort.PORT_4, cs.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_temperature.xml");
    }
}
