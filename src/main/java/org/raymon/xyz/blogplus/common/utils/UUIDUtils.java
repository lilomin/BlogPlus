package org.raymon.xyz.blogplus.common.utils;

import java.util.UUID;

/**
 * Created by lilm on 18-3-11.
 */
public class UUIDUtils {
	
	public static String createUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
	
}
