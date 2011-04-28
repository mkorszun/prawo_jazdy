package pl.greenislanddev.prawojazdy.utils;

import android.content.res.Resources;
import android.content.res.TypedArray;

public class ResourcesUtils {

	public static boolean[] getBoolArray(Resources res, int size, int resId) {
		boolean[] elements = new boolean[size];
		TypedArray array = res.obtainTypedArray(resId);
		for (int i = 0; i < size; i++) {
			elements[i] = array.getBoolean(i, false);
		}
		return elements;
	}

	public static String[] getStringArray(Resources res, int size, int resId) {
		String[] elements = new String[size];
		TypedArray array = res.obtainTypedArray(resId);
		for (int i = 0; i < size; i++) {
			elements[i] = array.getString(i);
		}
		return elements;
	}

	public static String getString(Resources res, int resId) {
		return res.getText(resId).toString();
	}
}
