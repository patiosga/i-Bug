// Env.java
package myfirst;
import javax.media.j3d.PointLight;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import simbad.gui.TestSimbatch;
import simbad.sim.*;

public class Env extends EnvironmentDescription {
    Env() {
//        this.setAmbientColor(0.8f, 0.8f, 0.8f);
//        PointLight light = new PointLight();
//        light.setPosition(0, 2, 0); // Position the light above the ground
//        add(light);
//
        light1IsOn = true;
        light1SetPosition(-5, 2, -5);
        light2IsOn = false;
        light2SetPosition(1000, 1000, 1000);

        setWorldSize(20);

        add(new Box(new Vector3d(-2,0,2), new Vector3f(1,1,1),this));
        add(new Box(new Vector3d(1,0,5), new Vector3f(1,1,1),this));
        add(new Box(new Vector3d(6,0,5), new Vector3f(1,1,1),this));
        add(new Box(new Vector3d(1,0,0), new Vector3f(1,1,1),this));
        add(new Box(new Vector3d(3,0,3), new Vector3f(1,1,1),this));


        add(new MyRobot(new Vector3d(7, 0, 7), "Αριστοφάνης"));
        ambientLightColor = black;
        backgroundColor = ligthgray; floorColor = white;
        archColor = red; boxColor = darkgray; wallColor = blue;

    }
}
