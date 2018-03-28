package org.raymon.xyz.blogplus.service.impl;

import org.raymon.xyz.blogplus.model.file.FileVO;
import org.raymon.xyz.blogplus.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lilm on 18-3-11.
 */
@Service
public class FileServiceImpl implements FileService {
	
	@Value("${file.root}")
	private String fileRoot;
	
	@Override
	public List<FileVO> fileList(String path) {
		if (path != null) {
			path = fileRoot + "/" + path;
			File file = new File(path);
			if (file.exists()) {
				if (file.isDirectory()) {
					return listFile(file);
				}
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
	public boolean uploadFile() {
		return false;
	}
	
	@Override
	public void downloadFile() {
	
	}
}
