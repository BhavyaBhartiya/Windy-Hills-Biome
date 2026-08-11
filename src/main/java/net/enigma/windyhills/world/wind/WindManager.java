package net.enigma.windyhills.world.wind;

import net.minecraft.world.phys.Vec3;

public class WindManager {
    private static Vec3 wind = new Vec3(1, 0, 0);
    private static float targetAngle = 0F;
    private static float currentAngle = 0F;
    private static double strength = 0.04;
    private static double targetStrength = 0.04;

    private WindManager() {}

    public static void tick() {
        if (Math.random() < (1.0/400.0)) {
            targetAngle = (float)(Math.random() * Math.PI * 2);
            targetStrength = 0.015+Math.random()*0.01;
        }
        strength += (targetStrength-strength)*0.01;

        float diff = targetAngle - currentAngle;

        while (diff > Math.PI) {
            diff -=Math.PI*2;
        }

        while (diff < -Math.PI) {
            diff +=Math.PI*2;
        }

        currentAngle += diff*0.02F;

        wind = new Vec3(Math.cos(currentAngle), 0.0, Math.sin(currentAngle));
    }

    public static double getStrength() {
        return strength;
    }

    public static Vec3 getWindVector(){return wind;}
}
