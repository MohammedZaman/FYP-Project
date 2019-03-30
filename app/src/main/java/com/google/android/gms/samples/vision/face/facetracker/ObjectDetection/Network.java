package com.google.android.gms.samples.vision.face.facetracker.ObjectDetection;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Network {

    private OkHttpClient client;
    private Request request;
    private String token;


    public Network (String token){
      client   = new OkHttpClient();
      this.token  = token;

     }


     public void setURL(String dataset,String img ){
         HttpUrl.Builder urlBuilder = HttpUrl.parse("https://automl.googleapis.com/v1beta1/").newBuilder();
         urlBuilder.addPathSegment(dataset);
         urlBuilder.addPathSegment(img);
         String url = urlBuilder.build().toString();
         sendRequest(url);
     }
     public void sendRequest(String url){
         request = new Request.Builder()
                 .header("Authorization","Bearer " + token)
                 .url(url)
                 .build();
     }





}
