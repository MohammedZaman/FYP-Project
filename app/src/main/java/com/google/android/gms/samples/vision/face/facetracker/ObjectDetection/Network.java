package com.google.android.gms.samples.vision.face.facetracker.ObjectDetection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.samples.vision.face.facetracker.Accessibility.WarningSystemTTS;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Network {

    private static Network network = null;
    private OkHttpClient client;
    private Request request;
    private String url;
    private WarningSystemTTS tts;

    public String getToken() {
        return token;
    }



    private String token;


    public static final MediaType JSON
            = MediaType.parse("Content-Type: application/json");


    public static Network getInstance()
    {
        if (network == null)
           network = new Network();

        return network;
    }


    public void setUp(String token, Context context){
        tts = new WarningSystemTTS(context,"");
      client   = new OkHttpClient();
      this.token = token;
      url ="https://automl.googleapis.com/v1beta1/projects/fyp-indoornav/locations/us-central1/models/ICN3638499721899927878:predict -d";
     }


    @SuppressLint("StaticFieldLeak")
    public final void callDetection(final Bitmap bitmap) {


        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {

                    JSONObject imgBytes= new JSONObject();
                    try {

                        imgBytes.put("imageBytes",encodeTobase64(bitmap));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONObject img= new JSONObject();
                    try {
                        img.put("image" ,imgBytes );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JSONObject payloadx = new JSONObject();
                    try {
                        payloadx.put("payload",img);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    Gson gson = new Gson();
                    String json = payloadx.toString();


                    RequestBody body = RequestBody.create(JSON,json);

                    request = new Request.Builder()
                            .header("Authorization","Bearer " + token)
                            .url("https://automl.googleapis.com/v1beta1/projects/fyp-indoornav/locations/us-central1/models/ICN1183747073149982906:predict")
                            .post(body)
                            .build();


                    Log.w("json",json);


                    Response response = client.newCall(request).execute();


                    String resStr = response.body().string().toString();
                    JSONObject responceJson  = null;
                    try {
                        if(resStr != null) {
                            responceJson = new JSONObject(resStr);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray payload;
                    if(responceJson != null) {
                        payload = new JSONArray(responceJson.get("payload").toString());
                        JSONObject classification = payload.getJSONObject(0);
                        responceJson = null;
                        resStr = "";
                        return classification.getString("displayName");
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

            protected void onPostExecute(String response) {
                String x = null;
                if(response != null) {
                        Log.d("classify", response);
                        tts.speak(response + " detected");
                }
                   else{
                    Log.d("classify", "nothing Detected");
                }





            }

        }.execute();
    }

    class Payload {


        public String getImageBase64() {
            return imageBase64;
        }

        public void setImageBase64(String imageBase64) {
            this.imageBase64 = imageBase64;
        }

        private String imageBase64;


    }



    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = android.util.Base64.encodeToString(b, android.util.Base64.NO_WRAP);
        return imageEncoded;
    }


    public String sendRequest(Payload image) throws IOException {




//         JSONObject img= new JSONObject();
//         try {
//             img.put("imageBytes" , image.getImageBytes().toString());
//         } catch (JSONException e) {
//             e.printStackTrace();
//         }

         JSONObject imgBytes= new JSONObject();
         try {

             imgBytes.put("imageBytes",image.getImageBase64());
         } catch (JSONException e) {
             e.printStackTrace();
         }

         JSONObject img= new JSONObject();
         try {
             img.put("image" ,imgBytes );
         } catch (JSONException e) {
             e.printStackTrace();
         }


         JSONObject payload = new JSONObject();
         try {
             payload.put("payload",img);
         } catch (JSONException e) {
             e.printStackTrace();
         }



         Gson gson = new Gson();
         String json = payload.toString();


         RequestBody body = RequestBody.create(JSON,json);

         request = new Request.Builder()
                 .header("Authorization","Bearer " + token)
                 .url("https://automl.googleapis.com/v1beta1/projects/fyp-indoornav/locations/us-central1/models/ICN3638499721899927878:predict")
                 .post(body)
                 .build();


         Log.w("json",json);
         Response response = client.newCall(request).execute();

        String resStr = response.body().string().toString();
        JSONObject jsonx  = null;
        try {
            jsonx = new JSONObject(resStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return jsonx.toString();
     }





}
