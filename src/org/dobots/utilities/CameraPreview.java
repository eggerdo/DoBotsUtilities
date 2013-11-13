package org.dobots.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

/*
 * See following link for an improved CameraPreview, but which requires Android API >= 11
 * http://stackoverflow.com/questions/10775942/android-sdk-get-raw-preview-camera-image-without-displaying-it/10776349#10776349
 */

public class CameraPreview extends ScalableSurfaceView implements SurfaceHolder.Callback, PreviewCallback {
	
	private static final String TAG = "CameraPreview";
	
	private static final int[] ROTATIONS = { 90, 270 };
	
	public interface CameraPreviewCallback {
		public void onFrame(byte[] rgb, int width, int height, int rotation);
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
	
	private boolean m_bStopOnHide = true;

	private boolean m_bHidden = false;
    
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
    
    public void setStopOnHide(boolean bStop) {
    	m_bStopOnHide = bStop;
    }
    
    public void stopCamera() {
    	if (mCamera != null) {
			mCamera.setPreviewCallback(null);  
	    	mCamera.stopPreview();
	    	mCamera.release();
	    	mCamera = null;
    	}
    }

	public void setHidden() {
		m_bHidden = true;
	}
	
	public boolean isHidden() {
		return m_bHidden;
	}
     
    LayoutParams mOldParams;
    public void hideCameraDisplay() {
    	// we can't remove the surface without stopping the preview,
    	// so instead we make the size of the surface 0 and give
    	// it back it's original size in the startCameraDisplay
    	mOldParams = (LayoutParams)getLayoutParams();
    	setLayoutParams(new LayoutParams(0,  0));
    	m_bHidden = true;
    }
    
    public void showCameraDisplay() {
    	if (mOldParams != null) {
    		setLayoutParams(mOldParams);
    	}
    	mOldParams = null;
    	m_bHidden = false;
    }
    
    public void startCamera() {
    	try {
	    	if (mCamera == null) {
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
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
    	
    	if (mCamera == null) {  
            // The Surface has been created, acquire the camera and tell it where  
            // to draw.  
	    	mCameraId = Camera.getNumberOfCameras() - 1;
	        mCamera = Camera.open(mCameraId);  
	        try {  
	        	mCamera.setPreviewDisplay(holder);  
	             
				//sets the camera callback to be the one defined in this class  
				mCamera.setPreviewCallback(this);  
	        	mCamera.setDisplayOrientation(90);
				
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
				
				if (m_bHidden) {
					hideCameraDisplay();
				}
	        } catch (IOException exception) {  
	            mCamera.release();  
	            mCamera = null;  
	            // TODO: add more exception handling logic here  
	        }  
    	} else {
        	try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
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
    	if (m_bStopOnHide) {
    		stopCamera();
    	}
    }  
  
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {  
        // Now that the surface is created, start the preview. 
    	// Note: not all phones support arbitrary preview sizes, so we leave
    	// the default size for now
    }  
    
    @Override  
    public void onPreviewFrame(byte[] data, Camera camera) {  
        //transforms NV21 pixel data into RGB pixels 
        byte[] frame = decodeYUV(data, mPreviewSize.width,  mPreviewSize.height);
        if (mFrameListener != null) {
        	mFrameListener.onFrame(frame, mPreviewSize.width, mPreviewSize.height, ROTATIONS[mCameraId]);
        }
    }
    
    private byte[] decodeYUV(byte[] yuv, int width, int height) {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	YuvImage yuvImage = new YuvImage(yuv, ImageFormat.NV21, width, height, null);
    	yuvImage.compressToJpeg(new Rect(0, 0, width, height), 50, out);
    	byte[] imageBytes = out.toByteArray();

//		// decode the received frame from jpeg to a bitmap
//		ByteArrayInputStream stream = new ByteArrayInputStream(imageBytes);
//		Bitmap bmp = BitmapFactory.decodeStream(stream);
//		Matrix matrix = new Matrix();
//		matrix.postRotate(-90);
//		Bitmap bmp_rotated = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
//
//		out.reset();
//		bmp_rotated.compress(CompressFormat.JPEG, 50, out);
//		imageBytes = out.toByteArray();
    	
    	return imageBytes;
    }
 
}  