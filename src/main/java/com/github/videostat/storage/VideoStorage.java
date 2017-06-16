package com.github.videostat.storage;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface VideoStorage {
	
	/**
	 * Save uploaded file in some storage accessible by ffmpeg and return path
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String store(MultipartFile file) throws IOException;
	
	/**
	 * Remove file if is no longer needed
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void clean(String path) throws IOException;

}
