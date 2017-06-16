package com.github.videostat.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.videostat.meta.VideoMetaExtractor;
import com.github.videostat.meta.VideoMetaFfmpegExtractor;
import com.github.videostat.validation.VideoFileValidator;

import net.bramp.ffmpeg.FFprobe;

@Configuration
public class VideostatConfig {
	
	@Autowired
	VideostatProperties videostatProperties;
	
	@Bean
	public FFprobe ffProbe() throws IOException {
		return new FFprobe(videostatProperties.getFfprobePath());
	}
	
	@Bean
	public VideoMetaExtractor videoMetaExtractor(FFprobe ffProbe) {
		return new VideoMetaFfmpegExtractor(ffProbe);
	}

	@Bean
	VideoFileValidator videoFileValidator() {
		return new VideoFileValidator(videostatProperties.getSupportedFormats());
	}

}
