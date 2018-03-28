package org.raymon.xyz.blogplus.common.exception;

import org.raymon.xyz.blogplus.common.result.Result;
import org.raymon.xyz.blogplus.common.result.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by lilm on 18-3-11.
 */
public class ErrorHandler {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@ResponseBody
	@ExceptionHandler(BlogPlusException.class)
	@ResponseStatus(value= HttpStatus.BAD_REQUEST)
	public Result handleServerError(Exception e) {
		return ResultUtils.error(((BlogPlusException) e).getE());
	}
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
	public Result handleUnknownError(Exception e) {
		log.error("SERVER ERROR! MSG: ", e);
		return ResultUtils.error(ExceptionEnum.SERVER_ERROR);
	}
}
