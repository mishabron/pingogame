package com.bronshteyn.android.pingo.model;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class Pingo {
	
	private int number;	
	private Boolean selected;
	private Boolean los;
	private Boolean won;	
	private ImageView view;
	
	private ObjectAnimator animation;

	public interface AnimatorCallnack{		
		public void onAnimationEnd();
		public void onAnimationStart();		
	}
	
	public Pingo(int number, Boolean selected, Boolean los, Boolean won,
			ImageView view) {
		super();
		this.number = number;
		this.selected = selected;
		this.los = los;
		this.won = won;
		this.view = view;
		
		setUI();
	}
	
	private void setUI() {

		Drawable face = ResourcesCache.pingoFaces.get(number);		
		view.setImageDrawable(face);
		
		if(selected){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.SELECTED.toString()));
		}
		else if (los){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.LOST.toString()));
		}
		else if(won){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.WON.toString()));
		}
		else{
			view.setBackground(null);
		}
	
	}

	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
		Drawable face = ResourcesCache.pingoFaces.get(number);		
		view.setImageDrawable(face);
		view.refreshDrawableState();
	}
	

	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public Boolean getLos() {
		return los;
	}
	public void setLos(Boolean los) {
		this.los = los;
	}

	public ImageView getView() {
		return view;
	}

	public void setView(ImageView view) {
		this.view = view;
	}
	
	
	
	public Boolean getWon() {
		return won;
	}

	public void setWon(Boolean won) {
		this.won = won;
	}

	public void rotate(String axis, int duration, int count, int angleFrom, int angleTo, final AnimatorCallnack callback) {

		animation = ObjectAnimator.ofFloat(view, axis, angleFrom, angleTo);	
		
		animation.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {	
				if(callback != null){
					callback.onAnimationStart();
				}
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
					
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				if(callback != null){
					callback.onAnimationEnd();
				}
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		
		animation.setDuration(duration);
		animation.setRepeatCount(count);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());	
		animation.start();		
		
	}

	public void stopRotation(){
		
		if(animation != null){
			animation.end();

		}
	}
	
	public void select(boolean select) {

		selected = select;
		
		if(selected){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.SELECTED.toString()));
		}
		else if (los){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.LOST.toString()));
		}
		else if(won){
			view.setBackground(ResourcesCache.pingoBackgrounds.get(PingoState.WON.toString()));
		}
		else{
			view.setBackground(null);
		}
		
	}
	
}
