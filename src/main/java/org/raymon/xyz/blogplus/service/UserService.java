package org.raymon.xyz.blogplus.service;

import org.raymon.xyz.blogplus.model.user.User;

/**
 * Created by lilm on 18-3-11.
 */
public interface UserService {
	
	boolean createUser(User user);
	
	// boolean updateUser(User user);
	
	User queryByUserId(String userId);
	
	User queryByUsername(String username);
	
	User userCheck(String username, String password);
	
}
