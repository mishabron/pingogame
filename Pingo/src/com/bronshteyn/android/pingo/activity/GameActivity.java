package com.bronshteyn.android.pingo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bronshteyn.android.pingo.R;
import com.bronshteyn.android.pingo.model.Pingo;
import com.bronshteyn.android.pingo.model.Pingo.AnimatorCallnack;

public class GameActivity extends Activity {
	
	private final String ROTATE_VERTICAL = "rotationY";
	private final String ROTATE_HORIZONTAL = "rotationX";
	private Pingo pingo1;
	private Pingo pingo2;
	private Pingo pingo3;
	private Pingo pingo4;
	private int activePingo;
	private final int LIMIT = 3;
	private final int NUMBERS = 9;
	private Pingo[] pingos = new Pingo[4];

	private int curentNUmber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		pingo1 = new Pingo(10,false,false,false,(ImageView)findViewById(R.id.pingo1));
		pingo2 = new Pingo(10,false,false,false,(ImageView)findViewById(R.id.pingo2));
		pingo3 = new Pingo(10,false,false,false,(ImageView)findViewById(R.id.pingo3));
		pingo4 = new Pingo(10,false,false,false,(ImageView)findViewById(R.id.pingo4));	
		
		pingos[0] = pingo1;
		pingos[1] = pingo2;		
		pingos[2] = pingo3;	
		pingos[3] = pingo4;	
		
		activePingo = 0;
								
		//define game controls
		final Button nextButton = (Button)findViewById(R.id.selectNext);		
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(activePingo < LIMIT){
					pingos[activePingo].select(false);
					activePingo++;
					pingos[activePingo].select(true);					
				}				
			}
		});
		
		nextButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				nextButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});
		
		final Button previousButton = (Button)findViewById(R.id.selectPrevious);		
		previousButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(activePingo >0){
					pingos[activePingo].select(false);
					activePingo--;
					pingos[activePingo].select(true);					
				}
				
			}
		});
		
		previousButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				previousButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});	
		
		final Button upButton = (Button)findViewById(R.id.upNumber);		
		upButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				curentNUmber = pingos[activePingo].getNumber();
				if(curentNUmber == NUMBERS +1 || curentNUmber == NUMBERS ){
					curentNUmber = 0;
				}
				else{
					curentNUmber++;
				}
				
				AnimatorCallnack flippCallback = new AnimatorCallnack(){
					@Override
					public void onAnimationEnd() {
						setFace(curentNUmber);					
					}

					@Override
					public void onAnimationStart() {
						
					}					
				};

				pingos[activePingo].rotate(ROTATE_HORIZONTAL,1000,0,0,360,flippCallback);								
			}
		});
		upButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				upButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});			
		
		final Button downButton = (Button)findViewById(R.id.downNumber);		
		downButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				curentNUmber = pingos[activePingo].getNumber();
				if(curentNUmber == NUMBERS +1 || curentNUmber == 0 ){
					curentNUmber = 9;
				}
				else{
					curentNUmber--;
				}
				
				AnimatorCallnack flippCallback = new AnimatorCallnack(){
					@Override
					public void onAnimationEnd() {
						setFace(curentNUmber);					
					}

					@Override
					public void onAnimationStart() {
						
					}					
				};				
				pingos[activePingo].rotate(ROTATE_HORIZONTAL,1000,0,360,0,flippCallback);									
			}
		});
		
		downButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				downButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});
		
		final Button hitButton = (Button)findViewById(R.id.hit);		
		hitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(getApplicationContext(), "Hit Pressed",
				Toast.LENGTH_LONG).show();
				
			}
		});
		hitButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hitButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				return false;
			}
		});		
	}

	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		final AnimatorCallnack spinn1Callback4 = new AnimatorCallnack(){
			@Override
			public void onAnimationEnd() {
			
			}

			@Override
			public void onAnimationStart() {

				
			}					
		};
		
		final AnimatorCallnack spinn1Callback3 = new AnimatorCallnack(){
			@Override
			public void onAnimationEnd() {
			
			}

			@Override
			public void onAnimationStart() {

				pingo4.rotate(ROTATE_HORIZONTAL,800,6,90,360,spinn1Callback4);					
			}					
		};
		
		final AnimatorCallnack spinn1Callback2 = new AnimatorCallnack(){
			@Override
			public void onAnimationEnd() {
				
			}

			@Override
			public void onAnimationStart() {
				pingo3.rotate(ROTATE_HORIZONTAL,800,5,20,360,spinn1Callback3);				
			}					
		};
		
		final AnimatorCallnack spinn1Callback1 = new AnimatorCallnack(){
			@Override
			public void onAnimationEnd() {
				pingos[activePingo].select(true);	
			}

			@Override
			public void onAnimationStart() {
				pingo2.rotate(ROTATE_HORIZONTAL,800,4,90,360,spinn1Callback2);
				
			}					
		};
				
						
		pingo1.rotate(ROTATE_HORIZONTAL,800,3,0,360,spinn1Callback1);				
	}



	private void setFace(int number){
		pingos[activePingo].setNumber(number);		
	}
	
}
