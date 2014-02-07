package org.dobots.utilities.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.dobots.utilities.ScalableSurfaceView;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

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
    private CameraPreviewCallback mYuvListener = null;

	private int mCameraId = 0;
	
	private int initWidth = -1, initHeight = -1;
	
	private boolean m_bStopOnHide = true;

	private boolean m_bHidden = false;

	private boolean mAutoExposureEnabled = true;
	
	private int mJpegQuality = 90;
	
    public CameraPreview(Context context) {  
        super(context);  
          
        // Install a SurfaceHolder.Callback so we get notified when the  
        // underlying surface is created and destroyed.  
        mHolder = getHolder();  
        mHolder.addCallback(this);  
        
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }  

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Install a SurfaceHolder.Callback so we get notified when the  
        // underlying surface is created and destroyed.  
        mHolder = getHolder();  
        mHolder.addCallback(this);  
        
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Install a SurfaceHolder.Callback so we get notified when the  
        // underlying surface is created and destroyed. 
        mHolder = getHolder();  
        mHolder.addCallback(this);  
        
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    public void setFrameListener(CameraPreviewCallback listener) {
    	mFrameListener = listener;
    }

    public void setYuvListener(CameraPreviewCallback listener) {
    	mYuvListener = listener;
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
    
    public boolean isStopped() {
    	return mCamera == null;
    }
    
    public void destroy() {
    	stopCamera();
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
	        	
	        	if (isAutoExposureLockSupported()) {
	        		mParameters.setAutoExposureLock(!mAutoExposureEnabled);
	        	}
	        	
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
        	
        	if (isAutoExposureLockSupported()) {
        		mParameters.setAutoExposureLock(!mAutoExposureEnabled);
        	}
        	
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
//	    	mCameraId = Camera.getNumberOfCameras() - 1;
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

	        	if (isAutoExposureLockSupported()) {
	        		mParameters.setAutoExposureLock(!mAutoExposureEnabled);
	        	}
	        	
				mCamera.setParameters(mParameters);
				
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
    
    public Size getPreviewSize() {
    	return mPreviewSize;
    }
    
    public List<Size> getSupportedPreviewSizes() {
    	return mParameters.getSupportedPreviewSizes();
    }
    
	public void setAutoExposure(Boolean enable) {
		mAutoExposureEnabled = enable;
		
		if (isAutoExposureLockSupported()) {
			mParameters.setAutoExposureLock(!enable);
			if (mCamera != null) {
				mCamera.setParameters(mParameters);
			}
		}
	}
	
	public boolean isAutoExposureLockSupported() {
		if (mParameters != null) {
			if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				return mParameters.isAutoExposureLockSupported();
			}
		}
		return false;
	}
	
	public boolean isAutoExposureEnabled() {
		return mAutoExposureEnabled;
	}
	
	public void setJpegQuality(int quality) {
		mJpegQuality = quality;
	}
	
	public int getJpegQuality() {
		return mJpegQuality;
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
    public void onPreviewFrame(final byte[] data, Camera camera) {  
        //transforms NV21 pixel data into RGB pixels

        if (mYuvListener != null) {
        	mYuvListener.onFrame(data, mPreviewSize.width, mPreviewSize.height, ROTATIONS[mCameraId]);
        }
        if (mFrameListener != null) {
            byte[] frame = decodeYUV(data, mPreviewSize.width,  mPreviewSize.height);
            frame = ExifUtils.encodeExifRotation(frame, ROTATIONS[mCameraId]);
        	mFrameListener.onFrame(frame, mPreviewSize.width, mPreviewSize.height, ROTATIONS[mCameraId]);
        }
    }
    
    private byte[] decodeYUV(byte[] yuv, int width, int height) {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	YuvImage yuvImage = new YuvImage(yuv, ImageFormat.NV21, width, height, null);
    	yuvImage.compressToJpeg(new Rect(0, 0, width, height), mJpegQuality, out);
    	byte[] imageBytes = out.toByteArray();
    	
    	return imageBytes;
    }
    
    // don't forget to call destroy on the CameraPreview!
    public static CameraPreview createCameraWithoutSurface(Context context) {

		// On some Android devices, Camera Preview is only available if
		// a valid SurfaceView is provided. Valid meaning not a dummy SurfaceView.
		// to get a valid SurfaceView in a Service which doesn't have a layout we
		// create a dummy SurfaceView and assign it to the window manager. the window
		// manager then creates the underlying surface so that the camera preview can
		// be obtained.
    	class InvisibleCamera extends CameraPreview{

    		public WindowManager mWindowManager;
    		
			public InvisibleCamera(Context context) {
				super(context);

				mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				
				// we assign dummy values as width and height to the surfaceview. we actually
				// want it to be invisible, but we cannot assign 0 as width and hight or the
				// surface would not be created. once the surface is created we can then make
				// it invisible
		    	WindowManager.LayoutParams params = new WindowManager.LayoutParams(1, 1,
				            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				            PixelFormat.TRANSLUCENT);   
				
		    	mWindowManager.addView(this, params);
			}
    		
			@Override
    		public void destroy() {
    			// we have to make the camera preview visible again otherwise the
    			// surface doesn't get destroyed (and the camera continues running)
    			ViewGroup.LayoutParams params = getLayoutParams();
    			params.height = 1;
    			params.width = 1;
    			mWindowManager.updateViewLayout(this, params);
    			
    			// remove the view
    			mWindowManager.removeView(this);
    			
    			super.destroy();
    		}
    		
    		@Override
    		public void surfaceCreated(SurfaceHolder holder) {
    			super.surfaceCreated(holder);

    			// once the surface for the camera preview is created, we make it disappear 
    			// by setting the width and height to 0
    			ViewGroup.LayoutParams params = getLayoutParams();
    			params.height = 0;
    			params.width = 0;
    			mWindowManager.updateViewLayout(this, params);
    		}
    	}

    	return new InvisibleCamera(context.getApplicationContext());
    }

 
}  