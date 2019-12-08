/**
 * <h1>Network </h1>
 *
 * <p>This class is used to access the Google Auto ML Rest API,
 * The the class requires the setUp Method to
 * be called for the class to perform detection.</p>
 *
 * @author  Mohammed Zaman
 *
 */
package com.example.mohammedzaman.FYP.ObjectDetection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.mohammedzaman.FYP.Accessibility.WarningSystemTTS;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Network {
    private static Network network = null;
    private OkHttpClient client;
    private Request request;
    private WarningSystemTTS tts;
    private String token;
    private Context mContext;
    private static final MediaType JSON
            = MediaType.parse("Content-Type: application/json");

    /**
     * This class is a singleton. The same instance can be accessed
     * throughout the application.
     * @return The network class is returned if it has already been initialized.
     */
    public static Network getInstance()
    {
        if (network == null)
            network = new Network();

        return network;
    }

    /**
     * This Method sets up the class so that it can perform the detection.
     * Also, the Text-to-speech class is initialized within this method.
     *
     * This method must be called or the detection will not function!
     * @param token This is required, in order to access the rest API
     * @param context  This parameter is the context of the activity in which the class is accessed from
     */
    public void setUp(String token, Context context){
        tts = new WarningSystemTTS(context,"");
        client   = new OkHttpClient();
        mContext = context;
        this.token = token;
    }


    /**
     * This method performs the request to the API.The image is encoded to base64 format
     * and prepared into the format which is specified by the API.
     *
     * The response is unwrapped from the JSON format in which it arrives. The Data is taken
     * out and validated, the score of the classification must be 0.8 to be outputted through
     * the custom Text-to-speech class Called WarningSystemTTS.
     *
     * The classification is only outputted once the request has been made.
     *
     * @param bitmap An image in bitmap format must be provided to be classified
     */
    @SuppressLint("StaticFieldLeak")
    public final void callDetection(final Bitmap bitmap) {
        // AsyncTask used to ensure the specific code is executed after Post
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    // setting up Request
                    // Image encoded to Base64 and put inside the JSONObject
                    JSONObject imgBytes= new JSONObject();
                    try {

                        imgBytes.put("imageBytes",encodeTobase64(bitmap));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Image to be sent tot he API
                    JSONObject img= new JSONObject();
                    try {
                        img.put("image" ,imgBytes );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // payload with image JSON object Within it
                    JSONObject payloadx = new JSONObject();
                    try {
                        payloadx.put("payload",img);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String json = payloadx.toString();
                    RequestBody body = RequestBody.create(JSON,json);
                    request = new Request.Builder()
                            .header("Authorization","Bearer " + token)
                            .url("https://automl.googleapis.com/v1beta1/projects/fyp-indoornav/locations/us-central1/models/ICN1183747073149982906:predict")
                            .post(body)
                            .build();




                    // Response
                    Response response = client.newCall(request).execute();
                    String resStr = response.body().string().toString();
                    JSONObject responseJson  = null;
                    try {
                        if(resStr != null) {
                            responseJson = new JSONObject(resStr);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray payload;
                    if(responseJson != null) {
                        payload = responseJson.getJSONArray("payload");
                        if(payload != null) {
                            JSONObject classification = payload.getJSONObject(0);
                            double score = classification.getJSONObject("classification").getDouble("score");
                            // Score can be adjusted, to suit the needs of the use case
                            if (score <= 0.8) {
//                                // response  time
//                                long tx = response.sentRequestAtMillis();
//                                long rx = response.receivedResponseAtMillis();
//                                long seconds = TimeUnit.MILLISECONDS.toSeconds(rx - tx);
                                return classification.getString("displayName").toString();
                            } else {
                                return null;
                            }
                        }
                    }else{
                        return null;
                    }


                } catch (GoogleJsonResponseException e) {
                    Log.d("error", "Request error: " + e.getContent());
                } catch (IOException e) {
                    Log.d("error", "Request error: " + e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String objectName) {
                if(objectName != null) {
                        tts.speak(objectName + " detected");
                    Toast.makeText(mContext, objectName + " Detected", Toast.LENGTH_LONG).show();
                }
                   else{
                    Log.d("classify", "nothing Detected");
                }
            }

        }.execute();
    }


    /**
     * This method is used to encode the bitmap image into Base64
     * @param image the parameter must be in bitmap format
     * @return This returns the encoded image as a string
     */
   private static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = android.util.Base64.encodeToString(b, android.util.Base64.NO_WRAP);
        return imageEncoded;
    }


    /**
     * This getter is used to check if the token exist from external classes
     *
     * @return the token which this class is set up with is returned
     */
    public String getToken() {
        return token;
    }



}
