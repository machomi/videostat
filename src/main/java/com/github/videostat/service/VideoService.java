package com.github.videostat.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.videostat.meta.VideoMetaException;
import com.github.videostat.meta.VideoMetaExtractor;
import com.github.videostat.rest.dto.VideoFileDTO;
import com.github.videostat.rest.dto.VideoMetadataDTO;
import com.github.videostat.storage.VideoStorage;

@Service
public class VideoService {
	
	@Autowired
	VideoStorage videoStorage;
	
	@Autowired
	VideoMetaExtractor videoMetaService;
	
	public VideoMetadataDTO process(VideoFileDTO videoFileDTO) throws IOException, VideoMetaException {
		// store file for further processing 
		String path = videoStorage.store(videoFileDTO.getVideo());
		// extract metadata
		VideoMetadataDTO meta = videoMetaService.extract(path);
		// remove file since it's no longer needed
		videoStorage.clean(path);
		return meta;
	}

}
