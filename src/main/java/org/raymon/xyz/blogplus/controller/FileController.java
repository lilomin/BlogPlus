package org.raymon.xyz.blogplus.controller;

import org.raymon.xyz.blogplus.common.result.Result;
import org.raymon.xyz.blogplus.common.result.ResultUtils;
import org.raymon.xyz.blogplus.model.file.FileVO;
import org.raymon.xyz.blogplus.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lilm on 18-3-11.
 */
@Controller
@RequestMapping("file")
public class FileController {
	
	@Resource
	private FileService fileService;
	
	@RequestMapping(value = "{path}", method = RequestMethod.GET)
	@ResponseBody
	public Result fileList(@PathVariable("path") String path) {
		return ResultUtils.success(fileService.fileList(path));
	}
	
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(Model model) {
		List<FileVO> list = fileService.fileList("");
		model.addAttribute("files", list);
		return "index";
	}
}
