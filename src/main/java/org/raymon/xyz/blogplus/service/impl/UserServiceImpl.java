package org.raymon.xyz.blogplus.service.impl;

import org.raymon.xyz.blogplus.common.exception.BlogPlusException;
import org.raymon.xyz.blogplus.common.exception.ExceptionEnum;
import org.raymon.xyz.blogplus.common.utils.EncryptUtils;
import org.raymon.xyz.blogplus.common.utils.UUIDUtils;
import org.raymon.xyz.blogplus.dao.UserDao;
import org.raymon.xyz.blogplus.model.user.User;
import org.raymon.xyz.blogplus.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by lilm on 18-3-11.
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Resource
	private UserDao userDao;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean createUser(User user) {
		String password = user.getPassword();
		user.setPassword(EncryptUtils.encryptBasedDes(password));
		User data = userDao.selectByUsername(user.getUsername());
		if (data != null) {
			throw new BlogPlusException(ExceptionEnum.USER_EXISTS);
		}
		user.setUserId(UUIDUtils.createUUID());
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		int flag = userDao.insert(user);
		return flag > 0;
	}
	
	@Override
	public User queryByUserId(String userId) {
		return userDao.selectById(userId);
	}
	
	@Override
	public User queryByUsername(String username) {
		return userDao.selectByUsername(username);
	}
	
	@Override
	public User userCheck(String username, String password) {
		User u = userDao.selectByUsername(username);
		if (u != null) {
			String pw = u.getPassword();
			
			String encodePwd = EncryptUtils.encryptBasedDes(password);
			if (encodePwd != null && encodePwd.equals(pw)) {
				return u;
			}
		}
		return null;
	}
}
