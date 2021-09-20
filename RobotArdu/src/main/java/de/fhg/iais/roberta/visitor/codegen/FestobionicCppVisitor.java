package de.fhg.iais.roberta.visitor.codegen;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.StepMotorAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IFestobionicVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for Arduino.</b> <br>
 */
public final class FestobionicCppVisitor extends AbstractCommonArduinoCppVisitor implements IFestobionicVisitor<Void> {

    // mapping of the 4 "ports" of the board to the actual pin numbers
    private static final Map<String, String> PIN_MAPPING = new HashMap<>();
    static {
        PIN_MAPPING.put("1", "25");
        PIN_MAPPING.put("2", "26");
        PIN_MAPPING.put("3", "17");
        PIN_MAPPING.put("4", "16");
    }

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     */
    public FestobionicCppVisitor(List<List<Phrase<Void>>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("digitalWrite(_led_").append(lightAction.getPort()).append(", ").append(lightAction.getMode().getValues()[0]).append(");");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.sb.append("_servo_").append(motorOnAction.getUserDefinedPort()).append(".write(");
        motorOnAction.getParam().getSpeed().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().accept(this);
        nlIndent();
        generateConfigurationVariables();
        generateTimerVariables();
        long numberConf =
            this.programPhrases
                .stream()
                .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
                .count();
        if ( (this.configuration.getConfigurationComponents().isEmpty() || this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER)) && numberConf == 0 ) {
            nlIndent();
        }
        generateUserDefinedMethods();
        if ( numberConf != 0 ) {
            nlIndent();
        }
        this.sb.append("void setup()");
        nlIndent();
        this.sb.append("{");

        incrIndentation();

        nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERIAL) ) {
            this.sb.append("Serial.begin(9600); ");
            nlIndent();
        }
        generateConfigurationSetup();

        generateUsedVars();
        this.sb.delete(this.sb.lastIndexOf("\n"), this.sb.length());

        decrIndentation();

        nlIndent();
        this.sb.append("}");

        nlIndent();
        return null;
    }
    
    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
//        this.sb.append("rob.setLed(");
//        if ( ledOnAction.getSide().equals("Left") ) {
//            this.sb.append("EYE_2, ");
//        } else {
//            this.sb.append("EYE_1, ");
//        }
//        ledOnAction.getLedColor().accept(this);
//        this.sb.append(");");
        
        this.sb.append("set_color(");
        ledOnAction.getLedColor().accept(this);
        this.sb.append(");");
        
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        this.sb.append("rob.setLed(");
        if ( ledOffAction.getSide().equals("Left") ) {
            this.sb.append("EYE_2, OFF);");
        } else {
            this.sb.append("EYE_1, OFF);");
        }
        return null;
    }
    
    @Override
    public Void visitStepMotorAction(StepMotorAction<Void> stepMotorAction) {
        this.sb.append("set_stepmotorpos(");
        stepMotorAction.getStepMotorPos().accept(this);
        this.sb.append(");");
        return null;
    }
    
    

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        } else {
            decrIndentation();
        }

        //*** generate definitions ***
        this.sb.append("#define _ARDUINO_STL_NOT_NEEDED"); // TODO remove negation and thus double negation in NEPODEFS.h, maybe define when necessary
        nlIndent();
        
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.getComponentType() ) {
                case SC.SERVOMOTOR:
                    break;
                case SC.LED:
                    this.sb.append("#define LED_BUILTIN 13");
			        nlIndent();
			        break;
                case SC.RGBLED:
                	this.sb.append("// *** BionicFlower LED config ***");
			        nlIndent();
			        this.sb.append("#define LED_PIN 16");
			        nlIndent();
			        this.sb.append("#define NUM_LEDS 5");
			        nlIndent();
			        nlIndent();
                    break;
                case SC.STEPMOTOR:
                	this.sb.append("// *** BionicFlower stepmotor config ***");
			        nlIndent();
			        this.sb.append("// MOTOR GPIO");
			        nlIndent();
			        this.sb.append("#define DIR 33");
			        nlIndent();
			        this.sb.append("#define STEP 25");
			        nlIndent();
			        this.sb.append("#define SLEEP 13");
			        nlIndent();
			        this.sb.append("//MOTOR CHARACTERISTIC");
			        nlIndent();
			        this.sb.append("//1.8 degree/step");
			        nlIndent();
			        this.sb.append("#define MOTOR_STEPS 200");
			        nlIndent();
			        this.sb.append("#define MICROSTEPS 1");
			        nlIndent();
			        nlIndent();
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.getComponentType());
            }
        }
        
        
      //*** generate definitions ***
      this.sb.append("#include <Arduino.h>\n");
	  nlIndent();
	  this.sb.append("#include <NEPODefs.h>");
	  nlIndent();
	            
	  Set<String> headerFiles = new LinkedHashSet<>();
	  for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
	      switch ( usedConfigurationBlock.getComponentType() ) {
	          case SC.SERVOMOTOR:
	              headerFiles.add("#include <ESP32Servo/src/ESP32Servo.h>");
	              break;
	          case SC.LED:
	              break;
	          case SC.RGBLED:
	          	headerFiles.add("// *** BionicFlower LED headerfiles ***");
	             nlIndent();
	             headerFiles.add("#include <Adafruit_NeoPixel.h>");
	             nlIndent();
	             break;
	          case SC.STEPMOTOR:
	          	headerFiles.add("// *** BionicFlower stepmotor headerfiles ***");
	            nlIndent();
	            headerFiles.add("#include <BasicStepperDriver.h>");
	            nlIndent();
	            break;
	          default:
	            throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.getComponentType());
	        }
	    }
	    for ( String header : headerFiles ) {
	        this.sb.append(header);
	        nlIndent();
	    }
	
	    super.generateProgramPrefix(withWrapping);
    }

    private void generateConfigurationSetup() {
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.getComponentType() ) {
                case SC.LED:
                    this.sb.append("pinMode(_led_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", OUTPUT);");
                    nlIndent();
                    break;
                case SC.RGBLED:
                	this.sb.append("// *** BionicFlower LED initialization ***");
                    nlIndent();
                    this.sb.append("pixels.begin();");
                    nlIndent();
                    break;
                case SC.SERVOMOTOR:
                    this.sb
                        .append("_servo_")
                        .append(usedConfigurationBlock.getUserDefinedPortName())
                        .append(".attach(")
                        .append(PIN_MAPPING.get(usedConfigurationBlock.getProperty(SC.PULSE)))
                        .append(");");
                    nlIndent();
                    break;
                case SC.STEPMOTOR:
                	this.sb.append("// *** BionicFlower stepmotor initialization ***");
                    nlIndent();
                    this.sb.append("//initialise the motor");
                    nlIndent();
                    this.sb.append("stepper.begin(RPM, MICROSTEPS);");
                    nlIndent();
                    this.sb.append("motor_calibration();");
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.getComponentType());
            }
        }
    }

    private void generateConfigurationVariables() {
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = cc.getUserDefinedPortName();
            switch ( cc.getComponentType() ) {
                case SC.LED:
                    this.sb.append("int _led_").append(blockName).append(" = ").append(cc.getProperty("INPUT")).append(";");
                    nlIndent();
                    break;
                case SC.RGBLED:
                	this.sb.append("// *** BionicFlower LED var declaration ***");
                    nlIndent();
                    this.sb.append("Adafruit_NeoPixel pixels(NUM_LEDS, LED_PIN, NEO_GRB + NEO_KHZ800);");
                    nlIndent();
                    nlIndent();
                    break;
                case SC.SERVOMOTOR:
                    this.sb.append("Servo _servo_").append(blockName).append(";");
                    nlIndent();
                    break;
                case SC.STEPMOTOR:
                	this.sb.append("// *** BionicFlower stepmotor var declaration/object creation ***");
                    nlIndent();
                    this.sb.append("BasicStepperDriver stepper(MOTOR_STEPS, DIR, STEP, SLEEP);");
                    nlIndent();
                    this.sb.append("//speed");
                    nlIndent();
                    this.sb.append("int RPM=240;");
                    nlIndent();
                    this.sb.append("//number of turns to go from state: full close to full open");
                    nlIndent();
                    this.sb.append("int FLOWER_CLOSE_TO_OPEN = 120;");
                    nlIndent();
                    this.sb.append("//position of the motor between full open and full close");
                    nlIndent();
                    this.sb.append("int current_position;");
                    nlIndent();
                    nlIndent();
                    break;
                default:
                	this.sb.append(cc.getComponentType());
                	throw new DbcException("Configuration block is not supported: " + cc.getComponentType());
            }
        }
    }
    
    @Override
    protected void generateUserDefinedMethods() {
    	for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = cc.getUserDefinedPortName();
            switch ( cc.getComponentType() ) {
                case SC.LED:
                    break;
                case SC.RGBLED:
                	this.sb.append("// *** BionicFlower LED helperfunction ***");
                    nlIndent();
                    this.sb.append("void set_color(uint32_t color)");
                    nlIndent();
                    this.sb.append("{");
                    nlIndent();
                    this.sb.append("  for (int i = 0; i < NUM_LEDS; i++)");
                    nlIndent();
                    this.sb.append("  {");
                    nlIndent();
                    this.sb.append("    pixels.setPixelColor(i,color);");
                    nlIndent();
                    this.sb.append("  }");
                    nlIndent();
                    this.sb.append("  pixels.show();");
                    nlIndent();
                    this.sb.append("}");
                    nlIndent();
                    nlIndent();
                    break;
                case SC.SERVOMOTOR:
                    break;
                case SC.STEPMOTOR:
                	this.sb.append("// *** BionicFlower stepmotor helperfunctions ***");
                    nlIndent();
                    this.sb.append("//The motor_calibration() function allows completely close the flower. It's the initial position of the flower.");
                    nlIndent();
                    this.sb.append("void motor_calibration()");
                    nlIndent();
                    this.sb.append("{");
                    nlIndent();
                    this.sb.append("  Serial.println(\"Calibatrion motor\");");
                    nlIndent();
                    this.sb.append("  for (int i =0; i<FLOWER_CLOSE_TO_OPEN;i++)");
                    nlIndent();
                    this.sb.append("  {");
                    nlIndent();
                    this.sb.append("    Serial.println(i);");
                    nlIndent();
                    this.sb.append("    stepper.rotate(-360);");
                    nlIndent();
                    this.sb.append("  }");
                    nlIndent();
                    this.sb.append("  current_position=0;");
                    nlIndent();
                    this.sb.append("  Serial.println(\"Calibatrion motor done\");");
                    nlIndent();
                    this.sb.append("}");
                    nlIndent();
                    
                    this.sb.append("//Set the position position of the flower.");
                    nlIndent();
                    this.sb.append("void set_stepmotorpos(int pos)");
                    nlIndent();
                    this.sb.append("{");
                    nlIndent();
                    this.sb.append("  if(pos>current_position)");
                    nlIndent();
                    this.sb.append("  {");
                    nlIndent();
                    this.sb.append("    //the flower is opening");
                    nlIndent();
                    this.sb.append("    Serial.println(\"Flower open\");");
                    nlIndent();
                    this.sb.append("    while(current_position < pos)");
                    nlIndent();
                    this.sb.append("    {");
                    nlIndent();
                    this.sb.append("      Serial.println(current_position);");
                    nlIndent();
                    this.sb.append("      stepper.rotate(360);");
                    nlIndent();
                    this.sb.append("      current_position = current_position +1;");
                    nlIndent();
                    this.sb.append("    }");
                    nlIndent();
                    this.sb.append("  }");
                    nlIndent();
                    this.sb.append("  else");
                    nlIndent();
                    this.sb.append("  {");
                    nlIndent();
                    this.sb.append("    //the flower is closing");
                    nlIndent();
                    this.sb.append("    Serial.println(\"Flower close\");");
                    nlIndent();
                    this.sb.append("    while (current_position > pos)");
                    nlIndent();
                    this.sb.append("    {");
                    nlIndent();
                    this.sb.append("      Serial.println(current_position);");
                    nlIndent();
                    this.sb.append("      stepper.rotate(-360);");
                    nlIndent();
                    this.sb.append("      current_position = current_position -1;");
                    nlIndent();
                    this.sb.append("    }");
                    nlIndent();
                    this.sb.append("  }");
                    nlIndent();
                    this.sb.append("}");
                    nlIndent();
                    nlIndent();
                    break;
                default:
                	throw new DbcException("Configuration block is not supported: " + cc.getComponentType());
            }
        }
    }
        
}
