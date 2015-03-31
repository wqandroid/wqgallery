package com.wq.photo.widget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by chaochen on 15/1/12.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    public static String TAG = "CameraPreview";

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "created");
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public Camera getCameraInstance() {
        try {

           int n=  Camera.getNumberOfCameras();
            if(n>0){
                mCamera=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }else{
                mCamera = Camera.open(); // attempt to get a Camera instance
            }
            Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            if (display.getRotation() == Surface.ROTATION_0) {
                mCamera.setDisplayOrientation(90);
            } else if (display.getRotation() == Surface.ROTATION_270) {
                mCamera.setDisplayOrientation(180);
            }
        } catch (Exception e) {
            Log.e(TAG, "摄像头不可用");
            // Camera is not available (in use or does not exist)
        }
        return mCamera; // returns null if camera is unavailable
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stopAndReleaseCamera();
        Log.e(TAG, "destroy");
    }

    public void stopAndReleaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            Log.d(TAG, "surfaceChanged");
            if (checkCameraHardware(getContext())) {
                mCamera = getCameraInstance();
                if(mCamera==null){
                    mCamera=Camera.open();
                }
                Camera.Parameters parameters = mCamera.getParameters();
                List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
                Camera.Size firstSize = sizes.get(0);
                Camera.Size lastSize = sizes.get(sizes.size() - 1);
                Camera.Size minSize = firstSize.width < lastSize.width ? firstSize : lastSize;
                parameters.setPreviewSize(minSize.width, minSize.height);
                mCamera.setParameters(parameters);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }

            if (mHolder.getSurface() == null) {
                return;
            }

            mCamera.stopPreview();
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}