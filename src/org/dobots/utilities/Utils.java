package org.dobots.utilities;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.Assert;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Utils {
	
	public static Activity CONTEXT;
	
	public static void setContext(Activity i_oActivity) {
		CONTEXT = i_oActivity;
	}
	
	public static Activity getContext() {
		Assert.assertNotNull("call setContext first with your main activity!", CONTEXT);
		
		return CONTEXT;
	}
	
	public static int setBit(int i_nVal, int i_nBit) {
		return i_nVal | 1 << i_nBit;
	}
	
	public static int clearBit(int i_nVal, int i_nBit) {
		return i_nVal & ~(1 << i_nBit);
	}
	
	public static boolean IsBitSet(int i_nVal, int i_nBit) {
		return ((i_nVal >> i_nBit) & 1) == 1;
	}
	
	public static short HighLowByteToShort(byte i_byHighByte, byte i_byLowByte) {
		return (short)(((i_byHighByte & 0xFF) << 8) | (i_byLowByte & 0xFF));
	}
	
	public static short ConvertEndian(short i_sValue) {
		return (short)(((i_sValue >> 8) & 0xFF) | ((i_sValue & 0xFF) << 8));
	}

	public static int convertEndian(int i_nValue) {
		return (int) (((i_nValue >> 24) & 0xFF) |
					  ((i_nValue >> 8) & 0xFF00) |
					  ((i_nValue & 0xFF00) << 8) |
					  ((i_nValue & 0xFF) << 24));
	}

	public static int byteToInt(byte byteValue) {
		int intValue = (byteValue & (byte) 0x7f);

		if ((byteValue & (byte) 0x80) != 0)
			intValue |= 0x80;

		return intValue;
	}
	

	public static String bytesToHex(byte[] bytes) {
	    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static byte[] stringToByteArray(String i_strText) {
		byte [] result = new byte[i_strText.length()];
		
        for (int i = 0; i < i_strText.length(); i++)
            result[i] = (byte) i_strText.charAt(i);

        return result;
	}

	public static String intToHex(int value) {
		return Integer.toHexString(value);
	}
	
	public static short getUnsignedByte(ByteBuffer buffer) {
		return (short) (buffer.get() & 0xFF);
	}

	public static void writeUnsignedByte(ByteBuffer buffer, int value) {
		buffer.put((byte)value);
	}
	
	public static int getUnsignedShort(ByteBuffer buffer) {
		return buffer.getShort() & 0xFFFF;
	}
	
	public static boolean getBoolean(ByteBuffer buffer) {
		return buffer.get() != 0;
	}
	
	public static String byteArrayToString(byte[] i_rgbyString) {
		String result = "";
		
		for (int i= 0; i < i_rgbyString.length; i++)
			result += (char) i_rgbyString[i];

		// remove CR and LF from string
		result.replaceAll("\\r\\n", "");
		
		return result;
	}

	public static String byteArrayToString(byte[] i_rgbyString, int i_nOffset, int i_nLength) {
		byte[] arr = Arrays.copyOfRange(i_rgbyString, i_nOffset, i_nOffset + i_nLength);
		return new String(arr).trim();
	}

    public static void waitSomeTime(int millis) {
        try {
            Thread.sleep(millis);

        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
    }

	public static void sendMessage(Handler i_oTarget, int i_nMsgID, Object i_oData) {
		Message msg = Message.obtain();
		msg.what = i_nMsgID;
		msg.obj = i_oData;
		
		if (i_oTarget != null) {
			i_oTarget.sendMessage(msg);
		}
	}
	
//    public static void sendBundle(Handler target, Bundle bundle) {
//        Message myMessage = Message.obtain();
//        myMessage.setData(bundle);
//        target.sendMessage(myMessage);
//    }
//    
//    public static void sendDataBundle(Handler target, Bundle bundle, Object data) {
//        Message myMessage = Message.obtain();
//        myMessage.setData(bundle);
//        myMessage.obj = data;
//        target.sendMessage(myMessage);
//    }
    
	public static void showLayout(LinearLayout i_oLayout, boolean i_bShow) {
//    	if (i_bShow) {
//    		i_oLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//    	} else {
//    		i_oLayout.setLayoutParams(new LinearLayout.LayoutParams(0,0));
//    	}
		if (i_bShow) {
			i_oLayout.setVisibility(View.VISIBLE);
		} else {
			i_oLayout.setVisibility(View.GONE);
		}
	}
	
	public static void showView(View i_oView, boolean i_bShow) {
//		if (i_bShow) {
//			i_oView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//    	} else {
//    		i_oView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT));
//    	}
		if (i_bShow) {
			i_oView.setVisibility(View.VISIBLE);
		} else {
			i_oView.setVisibility(View.GONE);
		}
	}
	
    public static void showToast(String textToShow, int duration) {
    	Toast oToast = Toast.makeText(getContext(), textToShow, duration);
		oToast.show();
	}
    
    public static void writeToImageView(Context context, ImageView i_oImage, String i_strText, boolean i_bCentered) {
    	Bitmap bmp = i_oImage.getDrawingCache();
    	if (bmp == null) {
    		bmp = Bitmap.createBitmap(i_oImage.getWidth(), i_oImage.getHeight(), Bitmap.Config.RGB_565);
    	}
		Canvas canvas = new Canvas(bmp);
		Utils.writeToCanvas(context, canvas, i_strText, i_bCentered);
		i_oImage.setImageBitmap(bmp);
    }
    
    public static void writeToSurfaceView(Context context, SurfaceView i_oView, String i_strText, boolean i_bCentered) {
    	Canvas canvas = i_oView.getHolder().lockCanvas();
		if (canvas != null) {
			try {
				Utils.writeToCanvas(context, canvas, i_strText, i_bCentered);
			} finally {
				i_oView.getHolder().unlockCanvasAndPost(canvas);
			}
		}
    }
    
	public static void writeToCanvas(Context context, Canvas i_oCanvas, String i_strText, boolean i_bCentered) {
		if (i_oCanvas == null) {
			return;
		}
		
		i_oCanvas.drawColor(Color.BLACK);
		LinearLayout layout = new LinearLayout(context);
		if (i_bCentered) {
			layout.setGravity(Gravity.CENTER);
		}
		
		TextView text = new TextView(context);
		text.setVisibility(View.VISIBLE);
		text.setText(i_strText);
		text.setTextAppearance(context, android.R.style.TextAppearance_Large);
		layout.addView(text);
		
		layout.measure(i_oCanvas.getWidth(), i_oCanvas.getHeight());
		layout.layout(0, 0, i_oCanvas.getWidth(), i_oCanvas.getHeight());
		
		layout.draw(i_oCanvas);
	}
	
	public static void updateOnOffMenuItem(MenuItem item, boolean i_bOn) {
		if (item != null) {
			if (i_bOn) {
				item.setIcon(android.R.drawable.button_onoff_indicator_on);
				item.setTitle(item.getTitle().toString().replace("ON", "OFF"));
			} else {
				item.setIcon(android.R.drawable.button_onoff_indicator_off);
				item.setTitle(item.getTitle().toString().replace("OFF", "ON"));
			}
		}
	}
	

	public static void waitForTaskCompletion(Collection<Callable<Object>> tasks) {
				
		ExecutorService es = Executors.newCachedThreadPool();
		
		try {
			es.invokeAll(tasks);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static boolean waitForTaskCompletionWithResult(Collection<Callable<Object>> tasks) {
		
		ExecutorService es = Executors.newCachedThreadPool();
		
		try {
			for (Future<Object> future : es.invokeAll(tasks)) {
				future.get();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static AlertDialog CreateAdapterDialog(Context context, String i_strTitle, ArrayAdapter i_oAdapter, DialogInterface.OnClickListener i_OnClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle(i_strTitle);
		builder.setAdapter(i_oAdapter, i_OnClickListener);
		return builder.create();
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	public static boolean inInterval(double i_dblValue, double i_dblCenter, double i_dblDeviation) {
		return (i_dblValue >= i_dblCenter - i_dblDeviation) &&
			   (i_dblValue <= i_dblCenter + i_dblDeviation);
	}
	
	////// Alternative, does not care about success
//	public void waitForTasksCompletion() {
//		
//		ExecutorService es = Executors.newCachedThreadPool();
//		
//		for (final RobotEntry entry : DancingMain.getInstance().getRobotList()) {
//				es.execute(new Runnable() {
//					
//					@Override
//					public void run() {
//						...
//					}
//				});
//		}
//		
//		es.shutdown();
//		try {
//			es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	public static void runAsyncTask(Runnable runner) {
		new Thread(runner).start();
	}
	
	public static void runAsyncUiTask(Runnable runner) {
		Handler oHandler = new Handler(Looper.getMainLooper());
		oHandler.post(runner);
	}

	public static boolean isDebugVersion(Activity i_oContext) {
		return ( 0 != ( i_oContext.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
	}
	
	public static boolean isInt(String str) {
		try {
			int n = Integer.valueOf(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public static void setEnabledRecursive(ViewGroup i_oView, boolean i_bEnabled) {
		for (int i = 0; i < i_oView.getChildCount(); ++i) {
			View view = i_oView.getChildAt(i);
			if (view instanceof ViewGroup) {
				setEnabledRecursive((ViewGroup)view, i_bEnabled);
			} else {
				view.setEnabled(i_bEnabled);
			}
		}
	}
	
	public static void setSpinnerSelectionWithoutCallingListener(final Spinner spinner, final int selection) {
	    final OnItemSelectedListener l = spinner.getOnItemSelectedListener();
	    spinner.setOnItemSelectedListener(null);
	    spinner.post(new Runnable() {
	        @Override
	        public void run() {
	            spinner.setSelection(selection);
	            spinner.post(new Runnable() {
	                @Override
	                public void run() {
	                    spinner.setOnItemSelectedListener(l);
	                }
	            });
	        }
	    });
	}

	public static String[] getNames(Class<? extends Enum<?>> e) {
	    return Arrays.toString(e.getEnumConstants()).replaceAll("\\[|]", "").split(", ");
	}
	
}
