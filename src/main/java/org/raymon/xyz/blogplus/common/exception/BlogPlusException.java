package org.raymon.xyz.blogplus.common.exception;

/**
 * Created by lilm on 18-3-11.
 */
public class BlogPlusException extends RuntimeException {
	
	private ExceptionEnum e;
	
	public BlogPlusException(ExceptionEnum e) {
		super(e.detail);
		this.e = e;
	}
	
	public ExceptionEnum getE() {
		return e;
	}
}
