package org.marvelousness.springboot.oms.controller.fs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.marvelousness.springboot.basic.annotation.Authorization;
import org.marvelousness.springboot.basic.enumer.AuthorizationType;
import org.marvelousness.springboot.oms.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

/**
 * 文件系统 —— 上载程序控制器
 * 
 * @author 981247127@qq.com
 * @time 2020-09-12 17:15
 */
@RestController
@RequestMapping("fs/uploader")
@Authorization(value = AuthorizationType.Anon)
public class UploaderController {

	@Autowired
	private FileSystemService service;

	/**
	 * 上传图片，支持批量上传
	 * 
	 * @return
	 */
	@PostMapping("image")
	public List<Map<String, Object>> image(MultipartRequest request) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		if (request != null) {
			Map<String, MultipartFile> files = request.getFileMap();
			if (files != null && !files.isEmpty()) {
				Set<Entry<String, MultipartFile>> entries = files.entrySet();
				for (Entry<String, MultipartFile> entry : entries) {
					maps.add(service.storeImage(entry.getValue()));
				}
			}
		}
		return maps;
	}

	/**
	 * 上传文件，支持批量上传
	 * 
	 * @return
	 */
	@PostMapping("file")
	public List<Map<String, Object>> file(MultipartRequest request) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		if (request != null) {
			Map<String, MultipartFile> files = request.getFileMap();
			if (files != null && !files.isEmpty()) {
				Set<Entry<String, MultipartFile>> entries = files.entrySet();
				for (Entry<String, MultipartFile> entry : entries) {
					maps.add(service.storeFile(entry.getValue()));
				}
			}
		}
		return maps;
	}
}