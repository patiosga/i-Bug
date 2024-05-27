// Env.java
package myfirst;
import javax.media.j3d.PointLight;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import simbad.sim.*;

public class Env extends EnvironmentDescription {
    Env() {
//        this.setAmbientColor(0.8f, 0.8f, 0.8f);
//        PointLight light = new PointLight();
//        light.setPosition(0, 2, 0); // Position the light above the ground
//        add(light);
//
        light1IsOn = true;
        light1SetPosition(0, 2, 5);
        light2IsOn = false;
        light2SetPosition(100, 100, 100);

        add(new MyRobot(new Vector3d(0, 0, 0), "robot 1"));
        ambientLightColor = black;
        backgroundColor = ligthgray; floorColor = white;
        archColor = red; boxColor = darkgray; wallColor = blue;

    }
}
