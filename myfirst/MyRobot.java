// MyRobot.java
package myfirst;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;


public class MyRobot extends Agent {
    RangeSensorBelt sonars;
    private LightSensor leftLightSensor;
    private LightSensor rightLightSensor;
    private LightSensor centerLightSensor;
    private double maxLightIntensity;
    static double K1 = 5;
    static double K2 = 0.8;
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
        maxLightIntensity = 0.067;

        leftLightSensor = RobotFactory.addLightSensorLeft(this);
        rightLightSensor = RobotFactory.addLightSensorRight(this);
        centerLightSensor = RobotFactory.addLightSensor(this);

    }
    public void initBehavior() {
        setTranslationalVelocity(0);
        setRotationalVelocity(0);

    }
    public void performBehavior()
    {
//        int min = 0;
//        for (int i=1;i<3;i++)
//            if (sonars.getMeasurement(i)<sonars.getMeasurement(min))
//                min=i;
//        for (int i=6;i<8;i++)
//            if (sonars.getMeasurement(i)<sonars.getMeasurement(min))
//                min=i;
//        if (sonars.getMeasurement(min)<0.5)
//            circumNavigate(min,true);
//        else
        this.moveToLight();
    }




    public void moveToLight(){
        double leftLight = leftLightSensor.getLux();
        double rightLight = rightLightSensor.getLux();
        double centerLight = centerLightSensor.getLux();

        if (this.maxLightIntensity / centerLight < 1)
            this.stop();
        else if (Math.abs(leftLight - rightLight) < 0.000001) {
            if (centerLight > rightLight)
                this.setRotationalVelocity(1);
            else
                this.setRotationalVelocity(0);
            this.setTranslationalVelocity(2.3);
        } else {
            if rightLight > light
            this.setRotationalVelocity((leftLight - rightLight) * 1000);
            this.setTranslationalVelocity(0);
        }
    }
    public void circumNavigate(int min_sonar_index, boolean CLOCKWISE){
        int min;
        min=0;
        for (int i=1;i<sonars.getNumSensors();i++)
            if (sonars.getMeasurement(i)<sonars.getMeasurement(min))
                min=i;
        Point3d p = getSensedPoint(this,sonars,min);
        double d = p.distance(new Point3d(0,0,0));
        Vector3d v;
        v = CLOCKWISE? new Vector3d(-p.z,0,p.x): new Vector3d(p.z,0,-p.x);
        double phLin = Math.atan2(v.z,v.x);
        double phRot =Math.atan(K3*(d-SAFETY));
        if (CLOCKWISE)
            phRot=-phRot;
        double phRef = wrapToPi(phLin+phRot);

        this.setRotationalVelocity(K1*phRef);
        this.setTranslationalVelocity(K2*Math.cos(phRef));
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


