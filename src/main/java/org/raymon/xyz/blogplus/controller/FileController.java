package org.raymon.xyz.blogplus.controller;

import org.raymon.xyz.blogplus.common.exception.BlogPlusException;
import org.raymon.xyz.blogplus.common.exception.ExceptionEnum;
import org.raymon.xyz.blogplus.common.result.Result;
import org.raymon.xyz.blogplus.common.result.ResultUtils;
import org.raymon.xyz.blogplus.common.utils.ImgUtils;
import org.raymon.xyz.blogplus.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lilm on 18-3-11.
 */
@Controller
@RequestMapping("api/v1/file")
public class FileController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Resource
	private FileService fileService;
	
	@RequestMapping(value = "{path}", method = RequestMethod.GET)
	@ResponseBody
	public Result fileList(@PathVariable("path") String path) {
		return ResultUtils.success(fileService.fileList(path));
	}
	
	@GetMapping("{blog}/{filename}")
	@ResponseBody
	public Result download(@PathVariable("blog") String blog, @PathVariable("filename") String filename, ServletResponse response) {
		try {
			// response.addHeader("Content-Disposition", "attachment;fileName=" + filename);// 设置文件名
			OutputStream os = response.getOutputStream();
			
			File file = fileService.downloadFile(blog, filename);
			// 将文件流输出
			ImgUtils.getImg(file, os);
			response.setContentType("image/png");
			os.flush();
			os.close();
		} catch (IOException e) {
			logger.error("Download file error:", e);
		}
		return ResultUtils.success("");
	}
	
	@PutMapping("upload")
	@ResponseBody
	public Result uploadImg(@RequestParam("file") MultipartFile multipartFile, @RequestParam("blogId") String blogId)  {
		if (multipartFile == null || multipartFile.isEmpty() || multipartFile.getOriginalFilename() == null) {
			throw new BlogPlusException(ExceptionEnum.PARAMS_EMPTY);
		}
		String contentType = multipartFile.getContentType();
		if (contentType == null || "".equals(contentType)) {
			throw new BlogPlusException(ExceptionEnum.PARAMS_EMPTY);
		}
		
		String filename = fileService.uploadBlogImg(multipartFile, blogId);
		if (filename != null) {
			return ResultUtils.success(filename);
		} else {
			return ResultUtils.fail("failed");
		}
	}
	
}
