package de.fhg.iais.roberta.syntax.codegen.raspberrypi;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class PythonVisitorTest extends RaspberryPiAstTest {

    private static final String IMPORTS =
        "" //
            + "#!/usr/bin/python\n\n"
            + "from __future__ import absolute_import\n"
            + "import time\n"
            + "import gpiozero as gpz\n"
            + "import colorzero as cz\n"
            + "import math\n\n"
            + "class BreakOutOfALoop(Exception): pass\n"
            + "class ContinueLoop(Exception): pass\n\n";

    private static final String GLOBALS = "";

    private static final String MAIN_METHOD =
        "" //
            + "def main():\n"
            + "    try:\n"
            + "        run()\n"
            + "    except Exception as e:\n"
            + "        print('Fehler im RaspberryPi')\n"
            + "        print(e.__class__.__name__)\n"
            + "        print(e)\n"
            + "if __name__ == \"__main__\":\n"
            + "    main()";

    private static ConfigurationAst brickConfiguration;

    @Test
    public void all_sensors() throws Exception {
        Map<String, String> digitalP = Util.createMap("OUTPUT", "1");
        Map<String, String> lightP = Util.createMap("OUTPUT", "18", "THRESHOLD", "0.5");
        Map<String, String> smoothedP = Util.createMap("OUTPUT", "1", "THRESHOLD", "0.5");
        Map<String, String> keyP = Util.createMap("PIN1", "4");
        Map<String, String> ultrasonicP = Util.createMap("MAX_DISTANCE", "1", "THRESHOLD", "0.5", "TRIG", "17", "ECHO", "18");
        Map<String, String> motionP = Util.createMap("OUTPUT", "4", "THRESHOLD", "0.5");

        ConfigurationComponent digital = new ConfigurationComponent("DIGITAL_OUTPUT", false, "S", "S", digitalP);
        ConfigurationComponent light = new ConfigurationComponent("LIGHT", false, "L", "L", lightP);
        ConfigurationComponent smoothed = new ConfigurationComponent("SMOOTHED_OUTPUT", false, "S2", "S2", smoothedP);
        ConfigurationComponent key = new ConfigurationComponent("KEY", false, "B", "B", keyP);
        ConfigurationComponent ultrasonic = new ConfigurationComponent("ULTRASONIC", false, "U", "U", ultrasonicP);
        ConfigurationComponent motion = new ConfigurationComponent("MOTION", false, "M", "M", motionP);

        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.addComponents(Arrays.asList(digital, light, smoothed, key, ultrasonic, motion));
        ConfigurationAst configurationAst = builder.build();
        String configuration =
            "button_B = gpz.Button(4)\n"
                + "digital_input_S = DigitalInputDevice(1)\n"
                + "distance_sensor_U = gpz.DistanceSensor(17, 18, max_distance=1, threshold=0.5);\n"
                + "light_sensor_L = gpz.LightSensor(18, threshold=0.5);\n"
                + "motion_sensor_M = gpz.MotionSensor(4, threshold=0.5);\n"
                + "smoothed_input_S2 = gpz.SmoothedInputDevice(1, threshold=0.5);\n";
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + configuration
                + "\n___item = 0\n"
                + "def run():\n"
                + "    global ___item\n"
                + "    if button_B.is_pressed:\n"
                + "        ___item = distance_sensor_U.distance\n"
                + "    elif motion_sensor_M.motion_detected:\n"
                + "        ___item = light_sensor_L.value\n"
                + "    elif digital_input_S.value:\n"
                + "        ___item = smoothed_input_S2.value\n"
                + "    elif light_sensor_L.light_detected:\n"
                + "        pass\n"
                + "    elif smoothed_input_S2.is_active:\n"
                + "        pass\n"
                + MAIN_METHOD;

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/sensors/all_sensors.xml", configurationAst, true);

    }

    @Test
    public void light_actions() throws Exception {
        Map<String, String> ledP = Util.createMap("INPUT", "17");
        Map<String, String> rgbP = Util.createMap("RED", "9", "GREEN", "10", "BLUE", "11");

        ConfigurationComponent led = new ConfigurationComponent("LED", false, "L", "L", ledP);
        ConfigurationComponent rgb = new ConfigurationComponent("RGBLED", false, "R", "R", rgbP);

        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.addComponents(Arrays.asList(led, rgb));
        ConfigurationAst configurationAst = builder.build();
        String configuration = "pwm_led_L = gpz.PWMLED(17)\n" + "rgb_led_R = gpz.RGBLED(9, 10, 11)\n";
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + configuration

                + "def run():\n"
                + "    pwm_led_L.on()\n"
                + "    pwm_led_L.toggle()\n"
                + "    pwm_led_L.off()\n"
                + "    rgb_led_R.color = cz.Color('#cc0000')\n"
                + "    rgb_led_R.on()\n"
                + "    rgb_led_R.off()\n"
                + "    pwm_led_L.blink(1, 2, n=3, background=False)\n"
                + "    blink(pwm_led_L, 10, 4)\n"
                + "    pwm_led_L.pulse(1, 2, n=3, background=False)\n"
                + "    rgb_led_R.blink(1, 2, on_color=cz.Color('#cc0000'), off_color=cz.Color('#ffcc33'), n=3, background=False)\n"
                + "    blink_rgb(rgb_led_R, 10, cz.Color('#cc0000'), cz.Color('#ffcc33'), 10)\n"
                + "    rgb_led_R.pulse(1, 2, on_color=cz.Color('#cc0000'), off_color=cz.Color('#ffcc33'), n=3, background=False)\n"

                + MAIN_METHOD;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/led.xml", configurationAst, true);

    }

    @Test
    public void buzzer_actions() throws Exception {
        Map<String, String> buzzerP = Util.createMap("INPUT", "3");
        Map<String, String> tonalBuzzerP = Util.createMap("INPUT", "4");

        ConfigurationComponent buzzer = new ConfigurationComponent("BUZZER", false, "B", "B", buzzerP);
        ConfigurationComponent tonalBuzzer = new ConfigurationComponent("TONALBUZZER", false, "T", "T", tonalBuzzerP);

        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.addComponents(Arrays.asList(tonalBuzzer, buzzer));
        ConfigurationAst configurationAst = builder.build();
        String configuration = "tonal_buzzer_T = gpz.TonalBuzzer(4)\n" + "buzzer_B = gpz.Buzzer(3)\n";
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + configuration

                + "def run():\n"
                + "    buzzer_B.beep(1, 1, 5, background=False)"
                + "    buzzer_B.on()"
                + "    buzzer_B.toggle()"
                + "    buzzer_B.off()"
                + "    tonal_buzzer_T.play(gpz.tones.Tone(300))\n"
                + "    time.sleep(100 / 1000.)\n"
                + "    tonal_buzzer_T.stop()\n"
                + MAIN_METHOD;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/buzzer.xml", configurationAst, true);

    }

    @Test
    public void motors_actions() throws Exception {
        Map<String, String> inputP = Util.createMap("INPUT", "1");
        Map<String, String> motorP = Util.createMap("PIN_FORWARD", "17", "PIN_BACKWARD", "18");
        Map<String, String> servoP = Util.createMap("MIN_PULSE_WIDTH", "0.001", "MAX_PULSE_WIDTH", "0.002", "FRAME_WIDTH", "0.02", "PIN1", "17");
        Map<String, String> angularServoP =
            Util.createMap("MIN_ANGLE", "-90", "MAX_ANGLE", "90", "MIN_PULSE_WIDTH", "0.001", "MAX_PULSE_WIDTH", "0.002", "FRAME_WIDTH", "0.02", "PIN1", "17");

        ConfigurationComponent input = new ConfigurationComponent("DIGITAL_INPUT", false, "A", "A", inputP);
        ConfigurationComponent tonalBuzzer = new ConfigurationComponent("MOTOR", false, "M", "M", motorP);
        ConfigurationComponent servo = new ConfigurationComponent("SERVOMOTOR", false, "S", "S", servoP);
        ConfigurationComponent angularservo = new ConfigurationComponent("ANGULARSERVOMOTOR", false, "A2", "A2", angularServoP);

        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.addComponents(Arrays.asList(input, tonalBuzzer, servo, angularservo));
        ConfigurationAst configurationAst = builder.build();
        String configuration =
            "digital_output_A = gpz.DigitalOutputDevice(1)\n"
                + "motor_servo_angular_A2 = gpz.AngularServo(17, min_angle=-90, max_angle=90, min_pulse_width=0.001, max_pulse_width=0.002, frame_width=0.02)\n"
                + "motor_servo_S = gpz.Servo(17, min_pulse_width=0.001, max_pulse_width=0.002, frame_width=0.02)\n"
                + "motor_M = gpz.Motor(17, 18)\n"
                + "";
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + configuration

                + "def run():\n"
                + "   motor_M.forward(90 / 100.0)\n"
                + "   motor_M.backward(90 / 100.0)\n"
                + "   motor_M.forward(90 / 100.0)\n"
                + "   time.sleep(1)\n"
                + "   motor_M.stop()\n"
                + "   motor_M.backward(90 / 100.0)\n"
                + "   time.sleep(1)\n"
                + "   motor_M.stop()\n"
                + "   motor_M.stop()\n"
                + "   motor_servo_S.min()\n"
                + "   motor_servo_S.mid()\n"
                + "   motor_servo_S.max()\n"
                + "   motor_servo_S.value = 0\n"
                + "   motor_servo_angular_A2.min()\n"
                + "   motor_servo_angular_A2.mid()\n"
                + "   motor_servo_angular_A2.max()\n"
                + "   motor_servo_angular_A2.angle = 90\n"

                + MAIN_METHOD;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/motors.xml", configurationAst, true);

    }

    @Test
    public void robot_actions() throws Exception {

        Map<String, String> robotP = Util.createMap("LEFT-FORWARD", "4", "LEFT-BACKWARD", "14", "RIGHT-FORWARD", "17", "RIGHT-BACKWARD", "18");

        ConfigurationComponent robot = new ConfigurationComponent("ROBOT", false, "R", "R", robotP);

        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.addComponents(Arrays.asList(robot));
        ConfigurationAst configurationAst = builder.build();
        String configuration = "robot_R = gpz.Robot(left=(4, 14), right=(17, 18))\n" + "";
        String expectedResult =
            "" //
                + IMPORTS
                + GLOBALS
                + configuration

                + "def run():\n"
                + "   robot_R.forward(90 / 100.0)\n"
                + "   robot_R.backward(90 / 100.0)\n"
                + "   robot_R.left(90 / 100.0)\n"
                + "   robot_R.right(90 / 100.0)\n"
                + "   robot_R.forward(90 / 100.0)\n"
                + "   time.sleep(1)\n"
                + "   robot_R.stop()\n"
                + "   robot_R.backward(90 / 100.0)\n"
                + "   time.sleep(1)\n"
                + "   robot_R.stop()\n"
                + "   robot_R.left(90 / 100.0)\n"
                + "   time.sleep(1)\n"
                + "   robot_R.stop()\n"
                + "   robot_R.right(90 / 100.0)\n"
                + "   time.sleep(1)\n"
                + "   robot_R.stop()\n"
                + "   robot_R.stop()\n"

                + MAIN_METHOD;

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, expectedResult, "/actors/robot.xml", configurationAst, true);

    }

}
