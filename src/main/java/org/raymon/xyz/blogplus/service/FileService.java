package org.raymon.xyz.blogplus.service;

import org.raymon.xyz.blogplus.model.file.FileVO;
import org.raymon.xyz.blogplus.model.file.UploadParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by lilm on 18-3-11.
 */
public interface FileService {
	
	List<FileVO> fileList(String path);
	
	String uploadBlogImg(MultipartFile multipartFile, String blogId, boolean compress);
	
	File downloadFile(String blogId, String fileName);
	
}
