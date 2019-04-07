package com.google.android.gms.samples.vision.face.facetracker.ObjectDetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;

import java.io.ByteArrayOutputStream;

public class ObjectDetector extends Detector<Face> {
    private Detector<Face> mDelegate;

    int i = 0;
    Network network = Network.getInstance();
    public ObjectDetector(Detector<Face> delegate) {
        mDelegate = delegate;
    }

    public SparseArray<Face> detect(Frame frame) {
        i++;
        if(frame != null) {
            if (i == 150 && network.getToken() != "") {
                int width = frame.getMetadata().getWidth();
                int height = frame.getMetadata().getHeight();
                YuvImage yuvImage = new YuvImage(frame.getGrayscaleImageData().array(), ImageFormat.NV21, width, height, null);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, out);
                byte[] bytes = out.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                network.callDetection(bitmap);
                i = 0;

            }
        }

        return mDelegate.detect(frame);
    }

    public boolean isOperational() {
        return mDelegate.isOperational();
    }

    public boolean setFocus(int id) {
        return mDelegate.setFocus(id);
    }
}
