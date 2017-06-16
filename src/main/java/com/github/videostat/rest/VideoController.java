package com.github.videostat.rest;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.videostat.rest.dto.VideoFileDTO;
import com.github.videostat.rest.dto.VideoMetadataDTO;
import com.github.videostat.service.VideoService;
import com.github.videostat.validation.VideoFileValidator;

@RequestMapping("/video")
@RestController
public class VideoController {
	
	private VideoService videoService;

	private VideoFileValidator validator;
	
	public VideoController(VideoService videoService, VideoFileValidator validator) {
		this.videoService = videoService;
		this.validator = validator;
	}
	
	@InitBinder
    protected void initBinderFileBucket(WebDataBinder binder) {
		binder.addValidators(validator);
    }
	
	@PostMapping("/stat")
	public VideoMetadataDTO stat(@Valid VideoFileDTO videoDTO) throws IOException {
		return videoService.process(videoDTO);
	}

}
