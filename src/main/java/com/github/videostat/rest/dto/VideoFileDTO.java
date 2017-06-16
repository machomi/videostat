package com.github.videostat.rest.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class VideoFileDTO {
	
	@NotNull
	private MultipartFile video;

}
