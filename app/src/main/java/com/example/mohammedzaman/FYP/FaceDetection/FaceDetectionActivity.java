
package com.example.mohammedzaman.FYP.FaceDetection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammedzaman.FYP.Accessibility.OnSwipeTouchListener;
import com.example.mohammedzaman.FYP.Accessibility.WarningSystemTTS;
import com.example.mohammedzaman.FYP.FaceDetection.UI.camera.CameraSourcePreview;
import com.example.mohammedzaman.FYP.FaceDetection.UI.camera.GraphicOverlay;
import com.example.mohammedzaman.FYP.GeneralClasses.Point;
import com.example.mohammedzaman.FYP.Localization.UserLocation;
import com.example.mohammedzaman.FYP.LocationActivity;
import com.example.mohammedzaman.FYP.ObjectDetection.ObjectDetector;
import com.example.mohammedzaman.FYP.ObstacleAvoidance.ObstacleLocation;
import com.example.mohammedzaman.FYP.R;
import com.example.mohammedzaman.FYP.ShortestPathAlgorithm.AStar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;
import java.util.Hashtable;


/**
 * Face detection and object detection activity, provides
 * notifications to the user of obstacles
 */
public final class FaceDetectionActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "Face";

    private CameraSource mCameraSource = null;

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private int CurrentFaceID = -1;
    WarningSystemTTS tts;
    private LinearLayout topLayout;
    private TextView obstacleOutput;
    private TextView PathOutput;
    private static boolean obstacleAvoidance;
    private AStar aStar;





    //==============================================================================================
    // Activity Methods
    //==============================================================================================

    /**
     * Initializes classes required
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        topLayout = (LinearLayout) findViewById(R.id.topLayout);
        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
        tts  = new WarningSystemTTS(this,"Navigation Started, for help double tap on the screen");
        obstacleOutput = findViewById(R.id.ObstacleText);
        PathOutput = findViewById(R.id.path);

        aStar = new AStar();

        // obtain source and destination from navigation activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int source= extras.getInt("src");
            int destin = extras.getInt("dest");

            PathOutput.setText(aStar.performAStar(source,destin));
        }


        // gesture control

       topLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
              obstacleAvoidance = true;
                tts.speak("Obstacle Avoidance On");
            }
            public void onSwipeRight() {
                Intent intent2 = new Intent(getApplicationContext(), LocationActivity.class);
                startActivity(intent2);
                tts.pause();
            }
            public void onSwipeLeft() {
                FaceDetectionActivity.super.onBackPressed();
                tts.speak("Navigation Ended");
            }
            public void onSwipeBottom() {
                finishAffinity();

            }
            public void doubleTap(){
                tts.pause();
                tts.speak("Swipe left to end navigation, Swipe down to exit");
            }

        });
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }
    }

    /**
       camera permissions
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    /**
     * This method creates the camera for the detection to take place.
     * The face detector is masked passed into the object detector.
     * so both can be processed
     */
    private void createCameraSource() {

// Old code needs authentication for google cloud for object detection !!!
//        Context context = getApplicationContext();
//        FaceDetector faceDetector = new FaceDetector.Builder(context)
//                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
//                .build();
//
//        ObjectDetector objDetector = new ObjectDetector(faceDetector);
//       objDetector.setProcessor(
//                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
//                        .build());
//
//        if (!faceDetector.isOperational()) {
//            Log.w(TAG, "Face faceDetector not available ");
//        }
//
//        mCameraSource = new CameraSource.Builder(context, objDetector)
//                .setRequestedPreviewSize(640, 480)
//                .setFacing(CameraSource.CAMERA_FACING_BACK)
//                .setRequestedFps(30.0f)
//                .build();

                Context context = getApplicationContext();
        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();


        faceDetector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        if (!faceDetector.isOperational()) {
            Log.w(TAG, "Face faceDetector not available ");
        }

        mCameraSource = new CameraSource.Builder(context,faceDetector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();

        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }

    /**
     * Releases the resources associated with the camera source
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Face Tracker sample")
                .setMessage("This application cannot run because it does not have the camera permission.  The application will now exit")
                .setPositiveButton(android.R.string.ok, listener)
                .show();

    }

    //==============================================================================================
    // Camera Source Preview
    //==============================================================================================


    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }


    //==============================================================================================
    // Graphic Face Tracker
    //==============================================================================================

    /**
     * The multiprocessor uses this factory to create face trackers for each face .
     */
    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    /**
     * Face tracker for each detected individual
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;
        private ObstacleLocation mObs;


        private Hashtable<Integer, String> indexTable; // find node name with index
        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
            mObs = new ObstacleLocation(getApplicationContext());
            DynamicLoadTable();


        }

        private  void DynamicLoadTable(){
            indexTable = new Hashtable<>();
            int x = 0;
            char[] Letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            String Letter = "A";
            int D = 1;



            for (int i = 0; i < 260; i++) {

                indexTable.put(i, String.valueOf(Letters[x]) + D);
                D++;

                int g = i + 1;
                if (g % 10 == 0) {
                    x = x + 1;
                    D = 1;

                }
            }
        }
        private String findNode(int index){
            return indexTable.get(index);
        }
        /**
         * Start tracking the detected face. The obstacle avoidance is carried out here
         * when a new face is detected, the estimation as well
         */
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
            tts.resetWarnings();
            CurrentFaceID = item.getId();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
            tts.Warning(item.getWidth());

            if (obstacleAvoidance == true) {
                Point x = mObs.getObstacleCoordinateTest(200, item.getWidth());
                if (x != null) {
                    if(findNode(x.getIndex())!= null) {
                        aStar.unblockAllNode();
                        obstacleOutput.setText("obstacle at " + findNode(x.getIndex()));
                        Log.d("obstacle", String.valueOf(findNode(x.getIndex())));
                        aStar.blockNode(x.getIndex());
                        PathOutput.setText(aStar.performAStar(x.getIndex() - 1, 170));
                    }
                } else {
                    obstacleOutput.setText("Obstacle not within range");
                }

            }
        }



        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);



        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
        }


        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }
}
