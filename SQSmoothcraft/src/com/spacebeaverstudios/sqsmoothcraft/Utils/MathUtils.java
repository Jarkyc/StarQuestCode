package com.spacebeaverstudios.sqsmoothcraft.Utils;

public class MathUtils {

    public static Integer getQuadrant(double angle){

        double sin = Math.sin(angle * (Math.PI / 180));
        double cos = Math.cos(angle * (Math.PI / 180));

        if(sin > 0 && cos > 0){
            return 1;
        } else if(sin > 0 && cos < 0){
            return 2;
        } else if(sin < 0 && cos < 0){
            return 3;
        } else if (sin < 0 &&  cos > 0 ){
            return 4;
        }

        return 0;

    }

    public static Integer getClosestCardinal(double angle){

        Integer quad = getQuadrant(angle);

        if(quad == 1){
            // It is closer to 0 degrees
            if(90 - angle > 45){
                return 0;

            // It is more than halfway to 90 degrees
            } else {
                return 90;
            }
        } else if(quad == 2) {
            // It is closer to 90 degrees
            if (180 - angle > 45) {
                return 90;

                // It is closer to 180
            } else {
                return 180;
            }
        } else if(quad == 3){
            // It is closer to 180 degrees
            if(270 - angle > 45){
                return 180;

            // It is closer to 270
            } else {
                return 270;
            }
        } else if(quad == 4){
            //It is closer to 270 degrees
            if(360 - angle > 45){
                return 270;

            // It is closer to 360 (0) degrees
            } else {
                return 0;
            }
        }
        return 0;

    }

}
