package org.raymon.xyz.blogplus.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lilm on 18-4-16.
 */
public class ImgUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ImgUtils.class);
	
	public static String saveImg(MultipartFile multipartFile, String path, boolean compress) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			boolean flag = file.mkdirs();
			if (!flag) {
				return null;
			}
		}
		FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
		String fileName = multipartFile.getOriginalFilename();
		path = path + File.separator + fileName;
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
		try {
			if (compress) {
				Thumbnails.of(fileInputStream)
						.size(640, 480)
						.toOutputStream(bos);
			} else {
				Thumbnails.of(fileInputStream)
						.size(1000, 800)
						.toOutputStream(bos);
			}
		} catch (IOException e) {
			logger.error("Create image Thumbnail failed：", e);
		}
		byte[] bs = new byte[1024];
		int len;
		while ((len = fileInputStream.read(bs)) != -1) {
			bos.write(bs, 0, len);
		}
		bos.flush();
		bos.close();
		return fileName;
	}
	
	public static void getImg(File file, OutputStream os) {
		byte[] buffer = new byte[1024];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			int i = bis.read(buffer);
			while (i != -1) {
				os.write(buffer, 0, i);
				i = bis.read(buffer);
			}
		} catch (Exception e) {
			logger.error("Get image failed：", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
