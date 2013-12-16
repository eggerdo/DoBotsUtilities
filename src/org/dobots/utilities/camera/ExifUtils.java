package org.dobots.utilities.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.BinaryConstants;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputField;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;

/**
 * Uses the library from http://code.google.com/p/sanselanandroid/ to read/write EXIF for jpeg
 * from a byte array
 * 
 * @author dominik
 *
 */
public class ExifUtils {
	
	public static final String TAG = "CameraUtils";
	
    public static ExifRewriter mExifWriter = new ExifRewriter();
    
    public static byte[] encodeExifRotation(byte[] rgb, int rotation) {
    	
    	try {
            // get all metadata stored in EXIF format from JPEG byte array
	    	IImageMetadata metaData = Sanselan.getMetadata(rgb);
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metaData;

			// Jpeg EXIF metadata is stored in a TIFF-based directory structure
            // and is identified with TIFF tags.
			TiffOutputSet outputSet = null;
			if (jpegMetadata != null) {

                // exif is null if no EXIF metadata is found.
				TiffImageMetadata exif = jpegMetadata.getExif();
				if (exif != null) {
					outputSet = exif.getOutputSet();
				}
			}
			
            // if file does not contain any exif metadata, we create an empty
            // set of exif metadata. Otherwise, we keep all of the other
            // existing tags.
			if (outputSet == null) {
	            outputSet = new TiffOutputSet();
	        }
			
//			{
//				TiffOutputField outputField = outputSet.findField(ExifTagConstants.EXIF_TAG_ORIENTATION);
//				if (outputField != null) {
//					Log.d(TAG, outputField.toString());
//				}
//			}
			
			// the orientation is stored in the EXIF directory
			TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();

			// first remove the field/tag if it already exists in this directory, or 
			// we may end up with duplicate tags
			exifDirectory.removeField(ExifTagConstants.EXIF_TAG_ORIENTATION);
			
			// determine the orientation number based on the orientation value. we only support
			// orientation with multiples of 90. For any other orientation, the image will be
			// returned as is and no EXIF metadata will be written
			Number orientation;
			switch (rotation) {
			case 90:
				orientation = ExifTagConstants.ORIENTATION_VALUE_ROTATE_90_CW;
				break;
			case 180:
				orientation = ExifTagConstants.ORIENTATION_VALUE_ROTATE_180;
				break;
			case 270:
				orientation = ExifTagConstants.ORIENTATION_VALUE_ROTATE_270_CW;
				break;
			default:
				return rgb;
			}
			
			// create the EXIF field with the orientation
			TiffOutputField outputField = TiffOutputField.create(ExifTagConstants.EXIF_TAG_ORIENTATION, 
						BinaryConstants.BYTE_ORDER_LITTLE_ENDIAN, orientation);
			exifDirectory.add(outputField);

			// create a new byte including the EXIF metadata
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			mExifWriter.updateExifMetadataLossless(rgb, os, outputSet);
			return os.toByteArray();
	
		} catch (ImageReadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImageWriteException e) {
			e.printStackTrace();
		}
    	
    	return rgb;
    }
    
	public static int getExifRotation(byte[] rgb) {
		try {
			if (rgb != null) {
				
	            // get all metadata stored in EXIF format from JPEG byte array
				IImageMetadata metaData = Sanselan.getMetadata(rgb);
				JpegImageMetadata jpegMetadata = (JpegImageMetadata) metaData;

				// Jpeg EXIF metadata is stored in a TIFF-based directory structure
	            // and is identified with TIFF tags.
				TiffOutputSet outputSet = null;
				if (jpegMetadata != null) {

	                // exif is null if no EXIF metadata is found.
					TiffImageMetadata exif = jpegMetadata.getExif();
					if (exif != null) {
						outputSet = exif.getOutputSet();
					}
				}

				if (outputSet == null) {
					// if no EXIF metadata is found we exit and return the base orientation 0
					return 0;
	            }
				
				// obtain the orientation from the orientation field
				TiffField field = jpegMetadata.findEXIFValue(ExifTagConstants.EXIF_TAG_ORIENTATION);
				if (field == null) {
					// if the field is not found, return the base orientation 0
					//   Log.d(TAG, ExifTagConstants.EXIF_TAG_ORIENTATION.name + ": " + "Not Found.");
					return 0;
				} else {
					//   Log.d(TAG, ExifTagConstants.EXIF_TAG_ORIENTATION.name + ": " + field.getIntValue());
					switch (field.getIntValue()) {
					case ExifTagConstants.ORIENTATION_VALUE_ROTATE_90_CW:
						return 90;
					case ExifTagConstants.ORIENTATION_VALUE_ROTATE_180:
						return 180;
					case ExifTagConstants.ORIENTATION_VALUE_ROTATE_270_CW:
						return 270;
					default:
						return 0;
					}
				}
			}
		} catch (ImageReadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImageWriteException e) {
			e.printStackTrace();
		}
		
		return 0;
	};
	
}
