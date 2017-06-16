package com.github.videostat.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VideoMetadataDTO {

	private Double duration;
	
	private Size size;
	
	private Long videoBitRate;
	
	private String videoCodec;
	
	private Long audioBitRate;
	
	private String audioCodec;
	
	@AllArgsConstructor
	@Data
	public static class Size {
		Integer width;
		Integer height;
	}
	
}
