package org.raymon.xyz.blogplus.service;

import org.raymon.xyz.blogplus.model.file.FileVO;

import java.util.List;

/**
 * Created by lilm on 18-3-11.
 */
public interface FileService {
	
	List<FileVO> fileList(String path);
	
	boolean uploadFile();
	
	void downloadFile();
	
}
