package com.bronshteyn.android.pingo.model;

import com.bronshteyn.android.pingo.R;

import java.util.HashMap;
import java.util.Map;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.SparseArray;

public class ResourcesCache {
	
	public static SparseArray<Drawable> pingoFaces = new SparseArray<Drawable>();
	public static Map<String, Drawable> pingoBackgrounds = new HashMap<String, Drawable>();	
	
	public static void init(Resources resources){
		
		//init pingo faces
		pingoFaces.put(0, ResourcesCompat.getDrawable(resources, R.drawable.zero, null));
		pingoFaces.put(1, ResourcesCompat.getDrawable(resources, R.drawable.one, null));
		pingoFaces.put(2, ResourcesCompat.getDrawable(resources, R.drawable.two, null));			
		pingoFaces.put(3, ResourcesCompat.getDrawable(resources, R.drawable.three, null));
		pingoFaces.put(4, ResourcesCompat.getDrawable(resources, R.drawable.four, null));
		pingoFaces.put(5, ResourcesCompat.getDrawable(resources, R.drawable.five, null));
		pingoFaces.put(6, ResourcesCompat.getDrawable(resources, R.drawable.six, null));
		pingoFaces.put(7, ResourcesCompat.getDrawable(resources, R.drawable.seven, null));
		pingoFaces.put(8, ResourcesCompat.getDrawable(resources, R.drawable.eight, null));
		pingoFaces.put(9, ResourcesCompat.getDrawable(resources, R.drawable.nine, null));
		pingoFaces.put(10, ResourcesCompat.getDrawable(resources, R.drawable.pingo, null));
		
		//init pingo backgrounds
		pingoBackgrounds.put(PingoState.SELECTED.toString(), ResourcesCompat.getDrawable(resources, R.drawable.blue, null));
		pingoBackgrounds.put(PingoState.WON.toString(), ResourcesCompat.getDrawable(resources, R.drawable.green_ring, null));		
		pingoBackgrounds.put(PingoState.LOST.toString(), ResourcesCompat.getDrawable(resources, R.drawable.red_ring, null));		
	}

}
