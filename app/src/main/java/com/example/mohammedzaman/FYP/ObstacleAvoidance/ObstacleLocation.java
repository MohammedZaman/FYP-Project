package com.example.mohammedzaman.FYP.ObstacleAvoidance;

import android.content.Context;

import com.example.mohammedzaman.FYP.DistanceEstimation.DistanceEstimation;
import com.example.mohammedzaman.FYP.GeneralClasses.Point;
import com.example.mohammedzaman.FYP.Localization.UserLocation;

public class ObstacleLocation {
    private UserLocation userLocation;
    private Context mContext;
    private DistanceEstimation mDis;

    public ObstacleLocation(Context context){
       mContext = context;
       userLocation = UserLocation.getInstance();
       mDis = new DistanceEstimation();
    }



    /**
     * This method will derive the obstacle based on the distance obtained from the distance estimation
     * @param GraphSize GraphSize The graph size ensures the coordinate is not out of bounds
     * @param facePx distance the distance is obtained from the distance estimation
     * @returnthe estimated position of the obstacle is returned to be
     * plotted on the graph of the navigational algorithm.
     */
        public Point getObstacleCoordinateTest (int GraphSize, float facePx){
            String distanceStr = mDis.calculateDistanceLogRegresCM(facePx);
                double distanceX = Double.parseDouble(distanceStr);
                int distance = (int)Math.round(distanceX / 100);
                Point currentLocation = userLocation.getCurrentXY();
                double directionRaw = userLocation.getCurrentDirection();
                double direction;
                if (directionRaw < 0) {
                    direction = 3.6 - directionRaw;
                } else {
                    direction = directionRaw;
                }


                if (currentLocation != null) {
                    if (currentLocation.getIndex() > 0 && currentLocation.getIndex() <= GraphSize) {
                        if (currentLocation.getY() < 10) {
                            if (direction >= 0.45 && direction <= 1.35) {
                                // the obstacle is right of the device
                                int y = currentLocation.getY();
                                int x = currentLocation.getX();
                                Point obsPoint = new Point();
                                obsPoint.setX(x - (int) Math.round(distance));
                                obsPoint.setY(y);
                                return obsPoint;
                            } else if (direction >= 2.25 && direction <= 3.15) {
                                // the obstacle is left of the device
                                int y = currentLocation.getY();
                                int x = currentLocation.getX();
                                Point obsPoint = new Point();
                                obsPoint.setX(x + (int) Math.round(distance));
                                obsPoint.setY(y);
                                return obsPoint;
                            } else if (direction >= 1.35 && direction <= 2.25) {
                                // the obstacle is in behind of the device
                                int y = currentLocation.getY();
                                int x = currentLocation.getX();
                                Point obsPoint = new Point();
                                obsPoint.setX(x);
                                obsPoint.setY(y - (int) Math.round(distance));
                                return obsPoint;
                            } else if (direction <= 3.15 && direction >= 3.6) {
                                // the obstacle is in front of the device
                                int y = currentLocation.getY();
                                int x = currentLocation.getX();
                                Point obsPoint = new Point();
                                obsPoint.setX(x);
                                obsPoint.setY(y + (int) Math.round(distance));
                                return obsPoint;
                            } else if (direction <= 0.45 && direction >= 0) {
                                // the obstacle is in front of the device
                                int y = currentLocation.getY();
                                int x = currentLocation.getX();
                                Point obsPoint = new Point();
                                obsPoint.setX(x);
                                obsPoint.setY(y + (int) Math.round(distance));
                                return obsPoint;
                            }
                        }
                    }
                } else {
                    return null;
                }

                return null;




        }
    }

