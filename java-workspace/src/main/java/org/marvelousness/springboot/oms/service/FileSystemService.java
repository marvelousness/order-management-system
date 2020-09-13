package org.marvelousness.springboot.oms.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.marvelousness.springboot.basic.exception.ServiceInvokeException;
import org.marvelousness.springboot.basic.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件系统服务
 * 
 * @author 981247127@qq.com
 * @time 2020-09-12 17:26
 */
@Service
public class FileSystemService {
	@Value("${fs.path:}")
	private String path;

	/**
	 * 获取资源的绝对地址
	 * 
	 * @param name
	 * @return
	 */
	public String getFileSystemStoreAbsolutePath(String name) {
		String path = StringUtils.isBlank(this.path) ? "/fs/resources" : this.path.trim();
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		name = name == null ? "" : name.trim();
		return path.concat(name.startsWith("/") ? name : "/" + name);
	}

	/**
	 * 存储图片文件
	 * 
	 * @param file
	 * @return
	 */
	public Map<String, Object> storeImage(MultipartFile file) {
		if (file == null) {
			return new HashMap<String, Object>();
		}

		String contentType = file.getContentType();
		String filename = file.getOriginalFilename();
		if (!(contentType != null && contentType.startsWith("image/"))) {
			throw new ServiceInvokeException("上传的文件不是图片类型禁止上传");
		}
		if (StringUtils.isBlank(filename)) {
			throw new ServiceInvokeException("上传的文件没有文件名称！");
		}

		return storeFile(file);
	}

	public Map<String, Object> storeFile(MultipartFile file) {
		if (file == null) {
			return new HashMap<String, Object>();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		String contentType = file.getContentType();
		String name = file.getName();
		String filename = file.getOriginalFilename();
		map.put("contentType", contentType);
		map.put("name", name);
		map.put("originalFilename", filename);
		map.put("size", file.getSize());

		if (StringUtils.isBlank(contentType)) {
			throw new ServiceInvokeException("上传文件类型错误");
		}

		if (StringUtils.isBlank(filename)) {
			throw new ServiceInvokeException("上传文件名称不允许为空");
		}
		contentType = contentType.startsWith("/") ? contentType.substring(1) : contentType;
		String path = contentType + "/" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssS") + "/" + StringUtils.randomAlphanumeric(32) + "-" + filename;
		map.put("url", "/fs/resources/" + path);

		try {
			File target = new File(getFileSystemStoreAbsolutePath(path));
			File dir = target.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(target));
		} catch (IOException e) {
			throw new ServiceInvokeException(e.getMessage());
		}

		return map;
	}
}