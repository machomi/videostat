package com.github.videostat.config;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Component
@Validated
@ConfigurationProperties("videostat")
public class VideostatProperties {

	@NotEmpty(message = "Supported formats configuration is required")
	private List<String> supportedFormats;

	@NotEmpty(message = "Path to ffprobe cannot be empty")
	private String ffprobePath;

}
