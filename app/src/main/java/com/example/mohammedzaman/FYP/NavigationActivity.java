package com.example.mohammedzaman.FYP;
/**
 * This activity is the main navigation for the system,
 * this uses haptic feedback and text to speech.
 * The A* algorithm is used here to provide the location a destination
 */

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.mohammedzaman.FYP.Accessibility.WarningSystemTTS;
import com.example.mohammedzaman.FYP.FaceDetection.FaceDetectionActivity;
import com.example.mohammedzaman.FYP.GeneralClasses.Point;
import com.example.mohammedzaman.FYP.Localization.UserLocation;
import com.example.mohammedzaman.FYP.ObjectDetection.GetOAuthToken;
import com.example.mohammedzaman.FYP.ObjectDetection.Network;
import com.example.mohammedzaman.FYP.ShortestPathAlgorithm.AStar;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private Button topLeftBtn;
    private Button topRightBtn;
    private Button bottomLeftBtn;
    private Button bottomRigthBtn;



    static final int REQUEST_CODE_PICK_ACCOUNT = 101;
    static final int REQUEST_ACCOUNT_AUTHORIZATION = 102;
    static final int REQUEST_PERMISSIONS = 13;

    private Account mAccount;

    // text to speech
    private WarningSystemTTS tts;
    private UserLocation userLocation;
    private AStar aStar;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setTitle("FYP");

        final String explain = "There are four Locations Available, click on the screen to get the location names and distance. To start the navigation hold the location.";
        ActivityCompat.requestPermissions(NavigationActivity.this,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                REQUEST_PERMISSIONS);

        tts  = new WarningSystemTTS(this,explain);
        userLocation = UserLocation.getInstance();
        aStar = new AStar();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);






        // top row buttons

        topLeftBtn= (Button)findViewById(R.id.optTlBtn);
        topRightBtn = (Button)findViewById(R.id.optTrBtn);

        topLeftBtn.setOnClickListener(this);
        topRightBtn.setOnClickListener(this);

        // bottom buttons

        bottomLeftBtn= (Button)findViewById(R.id.optBlBtn);
        bottomRigthBtn = (Button)findViewById(R.id.optBrBtn);

        // click event
        bottomLeftBtn.setOnClickListener(this);
        bottomRigthBtn.setOnClickListener(this);

        // long click event
        bottomRigthBtn.setOnLongClickListener(this);
        bottomLeftBtn.setOnLongClickListener(this);
        topLeftBtn.setOnLongClickListener(this);
        topRightBtn.setOnLongClickListener(this);





    }


    /**
     * this method is the handler for button clicks
     * @param v view
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        Point x = userLocation.getCurrentXY();
        switch (v.getId()) {
            case R.id.optTlBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if(x != null) {
                    aStar.performAStar(x.getIndex(),181);
                    tts.speak(String.valueOf("location 1 is "+aStar.getWeight()) + "meters");
                }else{
                    tts.speak("hold on, location is being found");
                }
                break;
            case R.id.optTrBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if(x != null) {
                    aStar.performAStar(x.getIndex(),185);
                    tts.speak(String.valueOf(aStar.getWeight()) + "meters");
                    tts.speak(String.valueOf("location 2 is "+aStar.getWeight()) + "meters");
                }else{
                    tts.speak("hold on, location is being found");
                }
                break;
            case R.id.optBlBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if(x != null) {
                    aStar.performAStar(x.getIndex(),6);
                    tts.speak(String.valueOf("location 3 is "+aStar.getWeight()) + "meters");
                }else{
                    tts.speak("hold on, location is being found");
                }
                break;
            case R.id.optBrBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if(x != null) {
                    aStar.performAStar(x.getIndex(),2);
                    tts.speak(String.valueOf("location 4 is " +aStar.getWeight()) + "meters");
                }else{
                    tts.speak("hold on, location is being found");
                }
                break;
            default:
                break;
        }
    }

    /**
     * This is the event handler for long clicks,
     * all four button can be changed here
     * @param v view
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        Point userLoc = userLocation.getCurrentXY();
        switch (v.getId()) {
            case R.id.optTlBtn:
                if(userLoc  != null) {
                    // the location is passed to the face detection activity
                    Intent intent = new Intent(getBaseContext(), FaceDetectionActivity.class);
                    intent.putExtra("src", userLoc.getIndex());
                    intent.putExtra("dest", 181);
                    startActivity(intent);
                    tts.pause();

                }else{
                    tts.speak("location initialising");
                }
                break;
            case R.id.optTrBtn:
                if(userLoc != null) {
                    Intent intent = new Intent(getBaseContext(), FaceDetectionActivity.class);
                    intent.putExtra("src", userLoc.getIndex());
                    intent.putExtra("dest", 185);
                    startActivity(intent);
                    tts.pause();

                }else{
                    tts.speak("location initialising");
                }
                break;
            case R.id.optBlBtn:
                if(userLoc != null) {
                    Intent intent = new Intent(getBaseContext(), FaceDetectionActivity.class);
                    intent.putExtra("src", userLoc.getIndex());
                    intent.putExtra("dest", 6);
                    startActivity(intent);
                    tts.pause();

                }else{
                    tts.speak("location initialising");
                }
                break;
            case R.id.optBrBtn:
                if(userLoc  != null) {
                    Intent intent = new Intent(getBaseContext(), FaceDetectionActivity.class);
                    intent.putExtra("src", userLoc.getIndex());
                    intent.putExtra("dest", 2);
                    startActivity(intent);
                    tts.pause();

                }else{
                    tts.speak("location initialising");
                }
                break;
            default:
                break;
        }
        return false;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAuthToken();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                AccountManager am = AccountManager.get(this);
                Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                for (Account account : accounts) {
                    if (account.name.equals(email)) {
                        mAccount = account;
                        break;
                    }
                }
                getAuthToken();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "No Account Selected", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == REQUEST_ACCOUNT_AUTHORIZATION) {
            if (resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                onTokenReceived(extra.getString("authtoken"));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Authorization Failed", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    /**
     * this method is used to get the access token
     */
    private void getAuthToken() {
        String SCOPE = "oauth2:https://www.googleapis.com/auth/cloud-platform";
        if (mAccount == null) {
            pickUserAccount();
        } else {
            new GetOAuthToken(this, mAccount, SCOPE, REQUEST_ACCOUNT_AUTHORIZATION)
                    .execute();
        }
    }

    /**
     * the user must select an account, this is used to obtain a service account
     */
    private void pickUserAccount() {
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }


    /**
     * This is called from get OAuth class, the google access token is provided here
     * @param token
     */
    public void onTokenReceived(final String token) {
        Log.w("Auth", token);
        final Network net = Network.getInstance();
        net.setUp(token,this);


    }
}
