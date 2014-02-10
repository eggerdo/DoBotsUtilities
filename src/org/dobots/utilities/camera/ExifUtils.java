package org.dobots.utilities.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.util.Log;

import com.android.mms.exif.ExifInterface;
import com.android.mms.exif.ExifTag;

public class ExifUtils {

	public static final String TAG = "CameraUtils";

	/*public static void writeExif(byte[] jpeg, ExifData exif, String path) {
	 *     OutputStream os = null;
	 *     try {
	 *         os = new FileOutputStream(path);
	 *         ExifOutputStream eos = new ExifOutputStream(os);
	 *         // Set the exif header
	 *         eos.setExifData(exif);
	 *         // Write the original jpeg out, the header will be add into the file.
	 *         eos.write(jpeg);
	 *     } catch (FileNotFoundException e) {
	 *         e.printStackTrace();
	 *     } catch (IOException e) {
	 *         e.printStackTrace();
	 *     } finally {
	 *         if (os != null) {
	 *             try {
	 *                 os.close();
	 *             } catch (IOException e) {
	 *                 e.printStackTrace();
	 *             }
	 *         }
	 *     }
	 * }
	 * */

	public static byte[] encodeExifRotation(byte[] jpeg, int rotation) {

		ExifInterface exifIface = new ExifInterface();
		try {
			exifIface.readExif(jpeg);

			short orientation = ExifInterface.getOrientationValueForRotation(rotation);
			ExifTag tag = exifIface.buildTag(ExifInterface.TAG_ORIENTATION, orientation);
			if (tag == null) {
				return jpeg;
			}
			exifIface.setTag(tag);
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			exifIface.writeExif(jpeg, os);
			
			return os.toByteArray();

		} catch (IOException e) {
			Log.e(TAG, "encode Exif failed!");
			e.printStackTrace();
		}
		
		return jpeg;
	}

	public static int getExifRotation(byte[] jpeg) {
		
		ExifInterface exifIface = new ExifInterface();
		try {
			exifIface.readExif(jpeg);
			short orientation = (short)((long[])exifIface.getTagValue(ExifInterface.TAG_ORIENTATION))[0];
			return ExifInterface.getRotationForOrientationValue(orientation);
		} catch (IOException e) {
			Log.e(TAG, "read Exif failed");
			e.printStackTrace();
		}
		
		return 0;
		
	}

}
