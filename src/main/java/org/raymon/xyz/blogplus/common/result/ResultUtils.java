package org.raymon.xyz.blogplus.common.result;

import org.raymon.xyz.blogplus.common.exception.ExceptionEnum;

/**
 * Created by lilm on 18-3-11.
 */
public class ResultUtils {
	
	public static Result success(Object o) {
		return new Result(1, "success", true, o);
	}
	
	public static Result fail(String failMsg) {
		return new Result(-1, failMsg, false, null);
	}
	
	public static Result error(ExceptionEnum e) {
		return new Result(e.getCode(), e.getDetail(), false, null);
	}
}
