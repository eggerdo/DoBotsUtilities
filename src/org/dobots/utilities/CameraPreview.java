package org.dobots.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class CameraPreview extends ScalableSurfaceView implements SurfaceHolder.Callback, PreviewCallback {
	
	private static final String TAG = "CameraPreview";
	
	public interface CameraPreviewCallback {
		public void onFrame(byte[] rgb, int width, int height);
	}
	
    SurfaceHolder mHolder;  
      
    Camera mCamera = null;  
      
    //This variable is responsible for getting and setting the camera settings  
    private Parameters mParameters;  
    //this variable stores the camera preview size   
    private Size mPreviewSize = null;  
    
    private CameraPreviewCallback mFrameListener = null;

	private int mCameraId;
	
	private int initWidth = -1, initHeight = -1;
    
    public CameraPreview(Context context) {  
        super(context);  
          
        // Install a SurfaceHolder.Callback so we get notified when the  
        // underlying surface is created and destroyed.  
        mHolder = getHolder();  
        mHolder.addCallback(this);  
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
    }  

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Install a SurfaceHolder.Callback so we get notified when the  
        // underlying surface is created and destroyed.  
        mHolder = getHolder();  
        mHolder.addCallback(this);  
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Install a SurfaceHolder.Callback so we get notified when the  
        // underlying surface is created and destroyed. 
        mHolder = getHolder();  
        mHolder.addCallback(this);  
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
    }
    
    public void setFrameListener(CameraPreviewCallback listener) {
    	mFrameListener = listener;
    }
    
    private void stopCamera() {
    	if (mCamera != null) {
			mCamera.setPreviewCallback(null);  
	    	mCamera.stopPreview();
	    	mCamera.release();
	    	mCamera = null;
    	}
    }
    
    public void toggleCamera() {
    	try {
    		stopCamera();
        	
    		// select next camera
        	mCameraId = (mCameraId + 1) % 2;
        	
        	// open camera
        	mCamera = Camera.open(mCameraId);
        	
        	// assign preview size
        	mParameters = mCamera.getParameters();
        	mParameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);  
	    	mCamera.setParameters(mParameters);
        	
        	// set screen preview settings 
        	mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(this.getHolder());
			mCamera.setPreviewCallback(this); 
			
			// start preview
	    	mCamera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }
  
    public void surfaceCreated(SurfaceHolder holder) {  
        // The Surface has been created, acquire the camera and tell it where  
        // to draw.  
    	mCameraId = Camera.getNumberOfCameras() - 1;
        mCamera = Camera.open(mCameraId);  
        try {  
        	mCamera.setDisplayOrientation(90);
        	mCamera.setPreviewDisplay(holder);  
             
			//sets the camera callback to be the one defined in this class  
			mCamera.setPreviewCallback(this);  
			
			///initialize the variables  
			mParameters = mCamera.getParameters();  
//			List<Size> previewSizes = mCamera.getParameters().getSupportedPreviewSizes();
//			Size size = previewSizes.get(previewSizes.size()-1);
//			mParameters.setPreviewSize(size.width, size.height);
			
			if (initWidth != -1 && initHeight != -1) {
				mParameters.setPreviewSize(initWidth, initHeight);
				mCamera.setParameters(mParameters);
			}
			mPreviewSize = mParameters.getPreviewSize();
			
			// start camera preview
			mCamera.startPreview();
        } catch (IOException exception) {  
            mCamera.release();  
            mCamera = null;  
            // TODO: add more exception handling logic here  
        }  
    }
    
    public void setPreviewSize(int width, int height) {
    	if (mPreviewSize == null || mCamera == null) {
    		initWidth = width;
    		initHeight = height;
    	} else {
	    	mPreviewSize.height = height;
	    	mPreviewSize.width = width;

	    	mParameters.setPreviewSize(width, height);
	    	mCamera.setParameters(mParameters);
    	}
    }
    
    public void surfaceDestroyed(SurfaceHolder holder) {  
        // Surface will be destroyed when we return, so stop the preview.  
        // Because the CameraDevice object is not a shared resource, it's very  
        // important to release it when the activity is paused.  
		stopCamera();
    }  
  
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {  
        // Now that the surface is created, start the preview. 
    	// Note: not all phones support arbitrary preview sizes, so we leave
    	// the default size for now
//    	mPreviewSize.height = h;
//    	mPreviewSize.width = w;
//    	mParameters.setPreviewSize(w, h);
//    	mCamera.setParameters(mParameters);
//      mCamera.startPreview();  
    }  
    
    @Override  
    public void onPreviewFrame(byte[] data, Camera camera) {  
        //transforms NV21 pixel data into RGB pixels 
        byte[] frame = decodeYUV(data, mPreviewSize.width,  mPreviewSize.height);
        if (mFrameListener != null) {
        	mFrameListener.onFrame(frame, mPreviewSize.width, mPreviewSize.height);
        }
    }
    
    private byte[] decodeYUV(byte[] yuv, int width, int height) {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	YuvImage yuvImage = new YuvImage(yuv, ImageFormat.NV21, width, height, null);
    	yuvImage.compressToJpeg(new Rect(0, 0, width, height), 50, out);
    	byte[] imageBytes = out.toByteArray();
    	return imageBytes;
    }
      
}  