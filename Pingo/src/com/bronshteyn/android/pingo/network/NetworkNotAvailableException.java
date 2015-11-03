package com.bronshteyn.android.pingo.network;

public class NetworkNotAvailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8775005242734930373L;

	public NetworkNotAvailableException() {
		super();
	}

	public NetworkNotAvailableException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NetworkNotAvailableException(String detailMessage) {
		super(detailMessage);
	}

	public NetworkNotAvailableException(Throwable throwable) {
		super(throwable);
	}
}
