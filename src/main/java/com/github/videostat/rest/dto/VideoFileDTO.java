package com.github.videostat.rest.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class VideoFileDTO {
	
	private MultipartFile video;

}
