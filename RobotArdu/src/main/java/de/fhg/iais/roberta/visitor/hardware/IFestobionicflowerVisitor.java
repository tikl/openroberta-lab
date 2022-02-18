package de.fhg.iais.roberta.visitor.hardware;


import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;


public interface IFestobionicflowerVisitor<V> extends ISensorVisitor<V> {

    //    @Override
    //    default V visitKeysSensor(KeysSensor<V> keysSensor) {
    //        throw new DbcException("Not supported!");
    //    }

    //    @Override
    //    default V visitColorSensor(ColorSensor<V> colorSensor) {
    //        throw new DbcException("Not supported!");
    //    }

    //    @Override
    //    default V visitLightSensor(LightSensor<V> lightSensor) {
    //        throw new DbcException("Not supported!");
    //    }

    //    @Override
    //    default V visitSoundSensor(SoundSensor<V> soundSensor) {
    //        throw new DbcException("Not supported!");
    //    }

    //    @Override
    //    default V visitEncoderSensor(EncoderSensor<V> encoderSensor) {
    //        throw new DbcException("Not supported!");
    //    }
    //
    //    @Override
    //    default V visitGyroSensor(GyroSensor<V> gyroSensor) {
    //        throw new DbcException("Not supported!");
    //    }
    //
    //    @Override
    //    default V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
    //        throw new DbcException("Not supported!");
    //    }

    //    @Override
    //    default V visitTouchSensor(TouchSensor<V> touchSensor) {
    //        throw new DbcException("Not supported!");
    //    }

    //    @Override
    //    default V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor) {
    //        throw new DbcException("Not supported!");
    //    }
    //
    //    @Override
    //    default V visitCompassSensor(CompassSensor<V> compassSensor) {
    //        throw new DbcException("Not supported!");
    //    }

    //    @Override
    //    default V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor) {
    //        throw new DbcException("Not supported!");
    //    }

    //    @Override
    //    default V visitVoltageSensor(VoltageSensor<V> voltageSensor) {
    //        throw new DbcException("Not supported!");
    //    }

    V visitLedOffAction(LedOffAction<Void> ledOffAction);

    V visitLedOnAction(LedOnAction<Void> ledOnAction);

    V visitStepMotorAction(StepMotorAction<Void> stepMotorAction);

    //    V visitKeysSensor(KeysSensor<V> keysSensor);
    //
    //    V visitLightSensor(LightSensor<V> lightSensor);

}
