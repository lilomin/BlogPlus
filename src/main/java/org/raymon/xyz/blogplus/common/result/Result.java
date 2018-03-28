package org.raymon.xyz.blogplus.common.result;

import java.io.Serializable;

/**
 * Created by lilm on 18-3-11.
 */
public class Result implements Serializable {
	
	private static final long serialVersionUID = -3073447650910424753L;
	private Integer code;
	private String msg;
	private boolean success;
	private Object data;
	private long timestamp;
	
	Result(Integer code, String msg, boolean success, Object data) {
		this.code = code;
		this.msg = msg;
		this.success = success;
		this.data = data;
		this.timestamp = System.currentTimeMillis();
	}
	
	public Result() {
	}
	
	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return "CommonResponse{" +
				"code='" + code + '\'' +
				", msg='" + msg + '\'' +
				", success=" + success +
				", data=" + data +
				", timestamp=" + timestamp +
				'}';
	}
	
}
