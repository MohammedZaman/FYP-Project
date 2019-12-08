package com.example.mohammedzaman.FYP;

/**
 * This activity displays the user's location on the screen,
 *  the activity uses Navgine SDK to achieve obtain and output the
 *  location.
 *
 */

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mohammedzaman.FYP.Localization.UserLocation;
import com.navigine.naviginesdk.DeviceInfo;
import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.LocationPoint;
import com.navigine.naviginesdk.LocationView;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.RoutePath;
import com.navigine.naviginesdk.SubLocation;

import java.util.Locale;

public class LocationActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback  {

    private static final String   TAG                     = "navigine API";
    private static final boolean  ORIENTATION_ENABLED     = true; // Show device orientation?

    // NavigationThread instance
    private NavigationThread mNavigation            = null;

    // UI
    private LocationView mLocationView             = null;

    // Location
    private Location      mLocation                 = null;
    private int           mCurrentSubLocationIndex  = -1;

    // Device
    private DeviceInfo    mDeviceInfo               = null; // Current device


    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // Initializing location view
        mLocationView = (LocationView)findViewById(R.id.navigation__location_view);
        mLocationView.setBackgroundColor(Color.WHITE);
        mLocationView.setListener
                (
                        new LocationView.Listener()
                        {


                            @Override public void onDraw(Canvas canvas)
                            {
                                drawDevice(canvas);
                            }
                        }
                );

        // Loading map only when location view size is known
        mLocationView.addOnLayoutChangeListener
                (
                        new View.OnLayoutChangeListener()
                        {
                            @Override public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
                            {
                                int width  = right  - left;
                                int height = bottom - top;
                                if (width == 0 || height == 0)
                                    return;

                                Log.d(TAG, "Layout chaged: " + width + "x" + height);

                                int oldWidth  = oldRight  - oldLeft;
                                int oldHeight = oldBottom - oldTop;
                                if (oldWidth != width || oldHeight != height)
                                    loadMap();
                            }
                        }
                );

        mNavigation = NavigineSDK.getNavigation();

        // Setting up device listener
        if (mNavigation != null)
        {
            mNavigation.setDeviceListener
                    (
                            new DeviceInfo.Listener()
                            {
                                @Override public void onUpdate(DeviceInfo info) { handleDeviceUpdate(info); }
                            }
                    );
        }

    }



    @Override public void onDestroy()
    {
        if (mNavigation != null)
        {
            NavigineSDK.finish();
            mNavigation = null;
        }

        super.onDestroy();
    }


    /**
     * This updates the location view with the new coordinates of the user
     * @param deviceInfo
     */
    private void handleDeviceUpdate(DeviceInfo deviceInfo)
    {
        mDeviceInfo = deviceInfo;
        mLocationView.redraw();

    }


    /**
     * This loads the maps after it has gone through validation
     * @return
     */
    private boolean loadMap()
    {
        if (mNavigation == null)
        {
            Log.e(TAG, "Can't load map! Navigine SDK is not available!");
            return false;
        }

        mLocation = mNavigation.getLocation();
        mCurrentSubLocationIndex = -1;

        if (mLocation == null)
        {
            Log.e(TAG, "Loading map failed: no location");
            return false;
        }

        if (mLocation.subLocations.size() == 0)
        {
            Log.e(TAG, "Loading map failed: no sublocations");
            mLocation = null;
            return false;
        }

        if (!loadSubLocation(0))
        {
            Log.e(TAG, "Loading map failed: unable to load default sublocation");
            mLocation = null;
            return false;
        }


        mNavigation.setMode(NavigationThread.MODE_NORMAL);
        mLocationView.redraw();
        return true;
    }

    /**
     * This methods checks to see if the sub location exists
     * @param index the index of the sub location
     * @return returns true if the sub location exists
     */
    private boolean loadSubLocation(int index)
    {
        if (mNavigation == null)
            return false;

        if (mLocation == null || index < 0 || index >= mLocation.subLocations.size())
            return false;

        SubLocation subLoc = mLocation.subLocations.get(index);
        Log.d(TAG, String.format(Locale.ENGLISH, "Loading sublocation %s (%.2f x %.2f)", subLoc.name, subLoc.width, subLoc.height));

        if (subLoc.width < 1.0f || subLoc.height < 1.0f)
        {
            Log.e(TAG, String.format(Locale.ENGLISH, "Loading sublocation failed: invalid size: %.2f x %.2f", subLoc.width, subLoc.height));
            return false;
        }

        if (!mLocationView.loadSubLocation(subLoc))
        {
            Log.e(TAG, "Loading sublocation failed: invalid image");
            return false;
        }

        float viewWidth  = mLocationView.getWidth();
        float viewHeight = mLocationView.getHeight();
        float minZoomFactor = Math.min(viewWidth / subLoc.width, viewHeight / subLoc.height);
        float maxZoomFactor = LocationView.ZOOM_FACTOR_MAX;
        mLocationView.setZoomRange(minZoomFactor, maxZoomFactor);
        mLocationView.setZoomFactor(minZoomFactor);


        mCurrentSubLocationIndex = index;
        mLocationView.redraw();
        return true;
    }


    /**
     * this method draws the location on to the location view
     * @param canvas
     */
    private void drawDevice(Canvas canvas)
    {
        // Check if location is loaded
        if (mLocation == null || mCurrentSubLocationIndex < 0)
            return;

        // Check if navigation is available
        if (mDeviceInfo == null || !mDeviceInfo.isValid())
            return;

        // Get current sublocation displayed
        SubLocation subLoc = mLocation.subLocations.get(mCurrentSubLocationIndex);

        if (subLoc == null)
            return;

        final int solidColor  = Color.argb(255, 64,  163, 205); // Light-blue color
        final int circleColor = Color.argb(127, 64,  163, 205); // Semi-transparent light-blue color
        final int arrowColor  = Color.argb(255, 255, 255, 255); // White color


        // Preparing paints
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        /// Drawing device path (if it exists)
        if (mDeviceInfo.paths != null && mDeviceInfo.paths.size() > 0)
        {
            RoutePath path = mDeviceInfo.paths.get(0);
            if (path.points.size() >= 2)
            {
                paint.setColor(solidColor);

                for(int j = 1; j < path.points.size(); ++j)
                {
                    LocationPoint P = path.points.get(j-1);
                    LocationPoint Q = path.points.get(j);
                    if (P.subLocation == subLoc.id && Q.subLocation == subLoc.id)
                    {
                        paint.setStrokeWidth(5);
                        PointF P1 = mLocationView.getScreenCoordinates(P);
                        PointF Q1 = mLocationView.getScreenCoordinates(Q);
                        canvas.drawLine(P1.x, P1.y, Q1.x, Q1.y, paint);
                    }
                }
            }
        }

        paint.setStrokeCap(Paint.Cap.BUTT);

        // Check if device belongs to the current sublocation
        if (mDeviceInfo.subLocation != subLoc.id)
            return;

        final float x  = mDeviceInfo.x;
        final float y  = mDeviceInfo.y;
        final float r  = mDeviceInfo.r;
        final float angle = mDeviceInfo.azimuth;
        final float sinA = (float)Math.sin(angle);
        final float cosA = (float)Math.cos(angle);
        final float radius  = mLocationView.getScreenLengthX(r);  // External radius: navigation-determined, transparent
        final float radius1 = 25 * 2; // Internal radius: fixed, solid


        Log.d("XY" , " X:" +Float.toString(x) +" Y:"+ Float.toString(y) );

        PointF O = mLocationView.getScreenCoordinates(x, y);
        PointF P = new PointF(O.x - radius1 * sinA * 0.22f, O.y + radius1 * cosA * 0.22f);
        PointF Q = new PointF(O.x + radius1 * sinA * 0.55f, O.y - radius1 * cosA * 0.55f);
        PointF R = new PointF(O.x + radius1 * cosA * 0.44f - radius1 * sinA * 0.55f, O.y + radius1 * sinA * 0.44f + radius1 * cosA * 0.55f);
        PointF S = new PointF(O.x - radius1 * cosA * 0.44f - radius1 * sinA * 0.55f, O.y - radius1 * sinA * 0.44f + radius1 * cosA * 0.55f);

        // Drawing transparent circle
        paint.setStrokeWidth(0);
        paint.setColor(circleColor);
        canvas.drawCircle(O.x, O.y, radius, paint);

        // Drawing solid circle
        paint.setColor(solidColor);
        canvas.drawCircle(O.x, O.y, radius1, paint);

        if (ORIENTATION_ENABLED)
        {
            // Drawing arrow
            paint.setColor(arrowColor);
            Path path = new Path();
            path.moveTo(Q.x, Q.y);
            path.lineTo(R.x, R.y);
            path.lineTo(P.x, P.y);
            path.lineTo(S.x, S.y);
            path.lineTo(Q.x, Q.y);
            canvas.drawPath(path, paint);
        }
    }



    }




