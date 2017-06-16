package com.github.videostat.storage;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class VideoLocalStorage implements VideoStorage {
	
	@Value("#{ ${storage.path:} ?: systemProperties['java.io.tmpdir']}")
	String path;
	
	@Override
	public String store(MultipartFile file) throws IOException {
		
		String baseName = FilenameUtils.getBaseName(file.getOriginalFilename());
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		
		File tmp = File.createTempFile(baseName, "." + ext, new File(path));
		
		file.transferTo(tmp);
		
		return tmp.getAbsolutePath();
	}
	
	@Override
	@Async
	public void clean(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

}
