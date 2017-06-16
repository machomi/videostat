package com.github.videostat.validation;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.github.videostat.rest.dto.VideoFileDTO;

public class VideoFileValidator implements Validator {
	
	private List<String> supportedFormats;

	public VideoFileValidator(List<String> supportedFormats) {
		this.supportedFormats = supportedFormats;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return VideoFileDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object value, Errors errors) {
		VideoFileDTO dto = (VideoFileDTO) value;
		if (dto.getVideo() == null || dto.getVideo().isEmpty()) {
			errors.rejectValue("video", "video.empty", "Empty video file");
		} else if (supportedFormats != null && !supportedFormats.contains(dto.getVideo().getContentType())) {
			errors.rejectValue("video", "video.format", new Object[] { dto.getVideo().getContentType() },
					"Unsupported content type");
		}
	}

}
