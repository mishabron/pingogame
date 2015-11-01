package com.bronshteyn.android.pingo.util;

import android.os.Handler;
import android.widget.ProgressBar;

public class ProgressIndicator implements Runnable {

	private ProgressBar progressBar;
	private Handler mHandler;	
	

	public ProgressIndicator(ProgressBar progressBar, Handler mHandler) {
		super();
		this.progressBar = progressBar;
		this.mHandler = mHandler;
	}


	@Override
	public void run() {

		try {
			while (!Thread.interrupted()) {
				for(int status =0; status < progressBar.getMax() ;status ++){
					Thread.sleep(50);
					final int progress = status;
					// Update the progress bar
					mHandler.post(new Runnable() {
						public void run() {
							progressBar.setProgress(progress);
						}
					});
				}
			}
		} catch (Exception e) {
		}
		finally{
			progressBar.setProgress(0);
		}
	}

}
