// MyRobot.java
package myfirst;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;


public class MyRobot extends Agent {
    RangeSensorBelt sonars;
    private LightSensor leftLightSensor;
    private LightSensor rightLightSensor;
    private LightSensor centerLightSensor;
    private double maxEncounteredLight;
    private double[] lightValues;
    private int lightValuesIndex;
    private boolean circumNavigating = false;
    private boolean stop = false;
    private int lastObjectCounter;
    static double K1 = 5;
    static double K2 = 0.6;
    static double K3 = 1;
    static double MT =2;
    static double SAFETY =0.8;



    boolean collided;
    public MyRobot (Vector3d position, String name) {
        super(position,name);
        sonars = RobotFactory.addSonarBeltSensor(this, 8);
        leftLightSensor = new LightSensor();
        rightLightSensor = new LightSensor();
        centerLightSensor = new LightSensor();
        circumNavigating = false;
        lightValues = new double[10];
        lightValuesIndex = 0;
        maxEncounteredLight = 0;
        leftLightSensor = RobotFactory.addLightSensorLeft(this);
        rightLightSensor = RobotFactory.addLightSensorRight(this);
        centerLightSensor = RobotFactory.addLightSensor(this);
    }
    public void initBehavior() {
        setTranslationalVelocity(0);
        setRotationalVelocity(0);

    }
    public void performBehavior() {
        int min = 0;
        if (stop)
            return;

        for (int i = 1; i < 2; i++) {
            if (sonars.getMeasurement(i) < sonars.getMeasurement(min))
                min = i;
        }
        for (int i = 7; i < 8; i++) {
            if (sonars.getMeasurement(i) < sonars.getMeasurement(min))
                min = i;
        }
        lightValues[lightValuesIndex%10] = centerLightSensor.getLux();
        lightValuesIndex++;


        if (sonars.getMeasurement(min) < SAFETY) {
            // μόλις πέσει το centerLightSensor κάτω από το maxEncounteredLight έχω τοπικό μέγιστο οπότε αρχίζει πορεία προς στόχο
            if (descendingLightOrder() && circumNavigating) {
                System.out.println("Βρήκα τοπικό μέγιστο");
                this.moveToLight();
                circumNavigating = false;
            } else {
                System.out.println("Προσπάθεια αποφυγής εμποδίου");
                this.circumNavigate(min, true);
                circumNavigating = true;
            }
        } else {
            System.out.println("Κίνηση προς το φως");
            this.moveToLight();
        }


    }

    private boolean descendingLightOrder() {
        for (int i = 0; i < 10; i++) {
            if (lightValues[lightValuesIndex%10] > lightValues[(lightValuesIndex+1)%10] ) {
                return false;
            }
        }
        return true;
    }

    public void moveToLight(){
        double leftLight = leftLightSensor.getLux();
        double rightLight = rightLightSensor.getLux();
        double centerLight = centerLightSensor.getLux();

        if (stop) {
            return;
        }

        double max_light = 0.055; // min light when the robot is at most 0.6 m away from the light
        if (centerLight > max_light) {
            this.stop();
            stop = true;
        }
        else if (rightLight == leftLight) {
            if (centerLight > rightLight) {
                this.setRotationalVelocity(-1); //
                this.setTranslationalVelocity(0);
            }
            else {
                this.setRotationalVelocity(0);
                this.setTranslationalVelocity(1);
            }
        } else {
            if (rightLight > leftLight) {
                this.setRotationalVelocity(-1);
                this.setTranslationalVelocity(0.3);
            } else {
                this.setRotationalVelocity(1);
                this.setTranslationalVelocity(0.3);
            }
        }
    }
    public void circumNavigate(int min1, boolean CLOCKWISE){
        int min = min1;

        Point3d p = getSensedPoint(this, sonars,min);
        double d = p.distance(new Point3d(0,0,0));
        Vector3d v;
        v = CLOCKWISE? new Vector3d(-p.z,0,p.x): new Vector3d(p.z,0,-p.x);
        double phLin = Math.atan2(v.z,v.x);
        double phRot =Math.atan(K3*(d-SAFETY));
        if (CLOCKWISE)
            phRot=-phRot;
        double phRef = wrapToPi(phLin+phRot);

        setRotationalVelocity(K1*phRef);
        setTranslationalVelocity(K2*Math.cos(phRef));
    }

    public static double wrapToPi(double a){
        if (a>Math.PI)
            return a-Math.PI*2;
        if (a<=-Math.PI)
            return a+Math.PI*2;
        return a;
    }

    public static Point3d getSensedPoint(Agent rob,RangeSensorBelt sonars,int sonar){
        double v;
        if (sonars.hasHit(sonar))
            v=rob.getRadius()+sonars.getMeasurement(sonar);
        else
            v=rob.getRadius()+sonars.getMaxRange();
        double x = v*Math.cos(sonars.getSensorAngle(sonar));
        double z = v*Math.sin(sonars.getSensorAngle(sonar));
        return new Point3d(x,0,z);
    }


    private void stop() {
        this.setRotationalVelocity(0);
        this.setTranslationalVelocity(0);
    }
}


