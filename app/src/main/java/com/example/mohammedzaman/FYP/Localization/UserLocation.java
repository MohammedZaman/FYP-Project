package com.example.mohammedzaman.FYP.Localization;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mohammedzaman.FYP.GeneralClasses.Point;
import com.example.mohammedzaman.FYP.ObjectDetection.Network;
import com.navigine.naviginesdk.DeviceInfo;
import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.LocationView;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.SubLocation;

public class UserLocation {


    private Context mContext;
    private NavigationThread mNavigation;
    // Location parameters
    private Location mLocation  = null;
    private int mCurrentSubLocationIndex = -1;
    private String TAG = "testXY";
    private LocationView x;
    private static Point previous = null;
    private static double direction = 0.0;

    private static UserLocation userLocation = null;


    public double getCurrentDirection(){
        return  direction;
    }
    public Point getCurrentXY() {
        return previous;
    }

    public static UserLocation getInstance()
    {
        if (userLocation == null)
            userLocation = new UserLocation();

        return userLocation;
    }






    public void setup(Context context){
        this.mContext = context;
        setup();
        if (mNavigation != null)
        {
            mNavigation.setDeviceListener
                    (
                            new DeviceInfo.Listener()
                            {
                                @Override public void onUpdate(DeviceInfo info) { update(info); }
                            }
                    );
        }

    }

    /**
     * This method uses the Navgine SDK to retrieve the location of the user
     * @return The (x,y) coordinates of the mobile phone is returned
     */


    private void update(DeviceInfo info){
        if(info.x != 0 && info.y != 0 && info.azimuth != 0) {
            previous = new Point();
            previous.setX(Math.round(info.x));
            previous.setY(Math.round(info.y));
            direction = info.azimuth;
        }

    }

    public Point getUserXY(){
        NavigationThread navigation = NavigineSDK.getNavigation();
        if (navigation == null)  // Check if navigation is available
            return null;
        // Error checking
        DeviceInfo info = navigation.getDeviceInfo();
        // the device info includes location name , device coordinates and error codes
        if (!info.isValid())
        {
            switch (info.errorCode)
            {
                case 4:
                    Log.d(TAG, "You are out of navigation zone! " +
                            "Check that your bluetooth is enabled!");
                    break;
                case 7:
                    Log.d(TAG, "Not enough reference points on the location!");
                    break;
                case 8:
                case 30:
                    Log.d(TAG, "Not enough beacons on the location!");
                    break;
                default:
                    Log.d(TAG, "Something is wrong with the location! " +
                            "Error code " + info.errorCode);
                    break;
            }
        }
        else {
            Point userPos = new Point();
            userPos.setX(Math.round(info.x));
            userPos.setY(Math.round(info.y));
            return userPos;
        }
        return null;
    }


    public double getDirection(){
        NavigationThread navigation = NavigineSDK.getNavigation();
        if (navigation == null)  // Check if navigation is available
            return 0;
        // Error checking
        DeviceInfo info = navigation.getDeviceInfo();
        // the device info includes location name , device coordinates and error codes
        if (!info.isValid())
        {
            switch (info.errorCode)
            {
                case 4:
                    Log.d(TAG, "You are out of navigation zone! " +
                            "Check that your bluetooth is enabled!");
                    break;
                case 7:
                    Log.d(TAG, "Not enough reference points on the location!");
                    break;
                case 8:
                case 30:
                    Log.d(TAG, "Not enough beacons on the location!");
                    break;
                default:
                    Log.d(TAG, "Something is wrong with the location! " +
                            "Error code " + info.errorCode);
                    break;
            }
        }
        else {
           return info.azimuth;
        }
        return 0;
    }

    /**
     * This method ensures the SDK is initialised and the location is loaded.
     * after the SDK has set the XY coordinate can be acquired
     */
    private void setup(){
        if (!NavigineSDK.initialize(mContext, Cred.USER_HASH, Cred.SERVER_URL))
            Toast.makeText(mContext, "Unable to initialize Navigine library!",
                    Toast.LENGTH_LONG).show();

        mNavigation = NavigineSDK.getNavigation();
        if (mNavigation != null)
            mNavigation.setMode(NavigationThread.MODE_NORMAL);

        loadSubLocation(0);
    }


    /**
     * This method loads and validates the sub location of the location
     * @param index this is the index of the sub location
     * @return this is used to see if a sub location exist.
     */
    private boolean loadSubLocation(int index)
    {
        if (mNavigation == null) {
            return false;
        }
        if (mLocation == null || index < 0 || index >= mLocation.subLocations.size())
        {
            return false;
        }
        SubLocation subLoc = mLocation.subLocations.get(index);
        Log.d(TAG, "sub-location" + subLoc.name +  subLoc.width + subLoc.height);
        if (subLoc.width < 1.0f || subLoc.height < 1.0f) // checking the size of the sub location
        {
            Log.e(TAG, "sub-location failed" + subLoc.width + subLoc.height);
            return false;
        }
        mCurrentSubLocationIndex = index;
        return true;
    }
}
