package com.google.android.gms.samples.vision.face.facetracker;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.samples.vision.face.facetracker.ObjectDetection.GetOAuthToken;
import com.google.android.gms.samples.vision.face.facetracker.ObjectDetection.Network;

import java.util.Locale;

public class Navigation extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private Button topLeftBtn;
    private Button topRightBtn;
    private Button bottomLeftBtn;
    private Button bottomRigthBtn;

    static final int REQUEST_CODE_PICK_ACCOUNT = 101;
    static final int REQUEST_ACCOUNT_AUTHORIZATION = 102;
    static final int REQUEST_PERMISSIONS = 13;

    private Account mAccount;

    // text to speech
    private TextToSpeech tts;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        final String explain = "There are four Locations Available, click on the screen to get the location names. To start the navigation hold on the location";
        ActivityCompat.requestPermissions(Navigation.this,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                REQUEST_PERMISSIONS);
        // text to speech
        this.tts = new TextToSpeech(this, new OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                    tts.setPitch(1.3f);
                    tts.setSpeechRate(1f);
                    tts.speak(explain, TextToSpeech.QUEUE_FLUSH, null);
                }

            }
        });


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

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.optTlBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                speak("location 1");
                break;
            case R.id.optTrBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                speak("location 2");
                break;
            case R.id.optBlBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                speak("location 3");
                break;
            case R.id.optBrBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                speak("location 4");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.optTlBtn:
                break;
            case R.id.optTrBtn:
                break;
            case R.id.optBlBtn:
                break;
            case R.id.optBrBtn:
                Intent intent = new Intent(this, FaceTrackerActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return false;
    }

    private void speak(String speachText){
        tts.setLanguage(Locale.UK);
        tts.setPitch(1.3f);
        tts.setSpeechRate(1f);
        tts.speak(speachText, TextToSpeech.QUEUE_FLUSH, null);

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


    private void getAuthToken() {
        String SCOPE = "oauth2:https://www.googleapis.com/auth/cloud-platform";
        if (mAccount == null) {
            pickUserAccount();
        } else {
            new GetOAuthToken(this, mAccount, SCOPE, REQUEST_ACCOUNT_AUTHORIZATION)
                    .execute();
        }
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }




    public void onTokenReceived(final String token) {
        Log.w("Auth", token);
        final Network net = Network.getInstance();
        net.setUp(token,this);


    }
}
