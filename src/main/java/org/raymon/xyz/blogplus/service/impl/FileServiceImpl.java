package org.raymon.xyz.blogplus.service.impl;

import org.raymon.xyz.blogplus.common.exception.BlogPlusException;
import org.raymon.xyz.blogplus.common.exception.ExceptionEnum;
import org.raymon.xyz.blogplus.common.utils.ImgUtils;
import org.raymon.xyz.blogplus.controller.FileController;
import org.raymon.xyz.blogplus.model.file.FileVO;
import org.raymon.xyz.blogplus.model.file.UploadParam;
import org.raymon.xyz.blogplus.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lilm on 18-3-11.
 */
@Service
public class FileServiceImpl implements FileService {
	
	private static final Logger logger = LoggerFactory.getLogger(FileService.class);
	
	@Value("${file.root}")
	private String fileRoot;
	
	@Override
	public List<FileVO> fileList(String path) {
		if (path != null && !path.trim().isEmpty()) {
			path = fileRoot + "/" + path;
		} else {
			path = fileRoot;
		}
		File file = new File(path);
		if (file.exists()) {
			if (file.isDirectory()) {
				return listFile(file);
			}
		}
		return new ArrayList<>();
	}
	
	private List<FileVO> listFile(File file) {
		if (file == null || !file.isDirectory()) {
			return new ArrayList<>();
		}
		
		File[] files = file.listFiles();
		if (files != null) {
			return Arrays.stream(files).map(f -> {
				FileVO fileVO = new FileVO();
				fileVO.setName(f.getName());
				fileVO.setDir(f.isDirectory());
				fileVO.setSize(f.length());
				fileVO.setUpdateTime(f.lastModified());
				return fileVO;
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
	
	@Override
	public String uploadBlogImg(MultipartFile multipartFile, String blogId) {
		String contentType = multipartFile.getContentType();
		String fileName = multipartFile.getOriginalFilename();
		logger.info("上传图片:name={},type={}", fileName, contentType);
		String filePath = fileRoot + File.separator + blogId;
		logger.info("图片保存路径={}", filePath);
		try {
			String fileUuidName = ImgUtils.saveImg(multipartFile, filePath);
			if (fileUuidName == null) {
				logger.warn("upload blog img:{} failed!", filePath);
			}
			return fileUuidName;
		} catch (IOException e) {
			throw new BlogPlusException(ExceptionEnum.SERVER_ERROR);
		}
	}
	
	@Override
	public File downloadFile(String blogId, String fileName) {
		return new File(fileRoot + File.separator + blogId + File.separator + fileName);
	}
}
