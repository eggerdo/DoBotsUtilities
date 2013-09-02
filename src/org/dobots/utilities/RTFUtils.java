package org.dobots.utilities;

import java.util.Arrays;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BulletSpan;

public class RTFUtils {
	
	public static CharSequence recursive(CharSequence text, String[] list) {
		// iterate recursively until the list is empty
		if (list.length == 0) {
			return text;
		} else {
			// add the next item from the list to the already assembled text
			text = assemble(text, list[0]);
			// continue iterating
			return recursive(text, Arrays.copyOfRange(list, 1, list.length));
		}
	}
	
	public static CharSequence assemble(CharSequence s1, CharSequence s2) {
		SpannableString ss1 = new SpannableString(s1);
		SpannableString ss2 = checkForBullet(s2);
		return TextUtils.concat(ss1, ss2);
	}
	
	public static SpannableString checkForBullet(CharSequence s) {
		SpannableString ss;
		// if the first element of the string is a '-' it means
		// it should be displayed as a bullet. we then remove the
		// '-' from the list and trim the spaces
		s = ((String)s).trim();
		if (s.length() > 0 && s.charAt(0) == '-') {
			s = ((String)s.subSequence(1, s.length())).trim();
			ss = new SpannableString(s + "\n");
			ss.setSpan(new BulletSpan(15), 0, s.length(), 0);
		} else {
			ss = new SpannableString(s + "\n");
		}
		return ss;
	}

}
