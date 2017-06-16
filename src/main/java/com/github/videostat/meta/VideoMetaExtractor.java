package com.github.videostat.meta;

import java.io.IOException;

import com.github.videostat.rest.dto.VideoMetadataDTO;

public interface VideoMetaExtractor {
	
	VideoMetadataDTO extract(String filePath) throws IOException;

}
