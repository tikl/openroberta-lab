package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotClearAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotPointAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.GpsSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IPinVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISerialVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISimpleSoundVisitor;

public interface IArduinoVisitor<V>
    extends IDisplayVisitor<V>, ISimpleSoundVisitor<V>, ILightVisitor<V>, ISerialVisitor<V>, IPinVisitor<V>, INeuralNetworkVisitor<V>, INano33BleSensorVisitor<V>, IHardwareVisitor<V> {

    V visitDropSensor(DropSensor<V> dropSensor);

    default V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample) {
        return sensorGetSample.getSensor().accept(this);
    }

    V visitHumiditySensor(HumiditySensor<V> humiditySensor);

    V visitMotionSensor(MotionSensor<V> motionSensor);

    V visitAccelerometerSensor(AccelerometerSensor<V> accelerometerSensor);

    V visitGyroSensor(GyroSensor<V> gyroSensor);

    V visitInfraredSensor(InfraredSensor<V> infraredSensor);

    V visitLightSensor(LightSensor<V> lightSensor);

    V visitMoistureSensor(MoistureSensor<V> moistureSensor);

    V visitMotorOnAction(MotorOnAction<V> motorOnAction);

    V visitPinGetValueSensor(PinGetValueSensor<V> pinGetValueSensor);

    V visitPulseSensor(PulseSensor<V> pulseSensor);

    V visitRelayAction(RelayAction<V> relayAction);
    V visitKeysSensor(KeysSensor<Void> button);

    V visitRfidSensor(RfidSensor<V> rfidSensor);

    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    V visitVoltageSensor(VoltageSensor<Void> potentiometer);

    V visitTimerSensor(TimerSensor<V> timerSensor);

    V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor);

    V visitVoltageSensor(VoltageSensor<V> voltageSensor);
   V visitLedOffAction(LedOffAction<V> ledOffAction);  
        throw new DbcException("Block is not implemented!");
    }
   V visitLedOnAction(LedOnAction<V> ledOnAction);
        throw new DbcException("Block is not implemented!");
    }
	V visitStepMotorAction(StepMotorAction<Void> stepMotorAction);
    	throw new DbcException("Block is not implemented!");
    }
}
