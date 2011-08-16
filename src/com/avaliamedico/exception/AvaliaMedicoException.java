package com.avaliamedico.exception;

public class AvaliaMedicoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AvaliaMedicoException() {
	}

	public AvaliaMedicoException(String arg0) {
		super(arg0);
	}

	public AvaliaMedicoException(Throwable arg0) {
		super(arg0);
	}

	public AvaliaMedicoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
