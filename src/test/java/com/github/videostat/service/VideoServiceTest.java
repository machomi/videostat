package com.github.videostat.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.videostat.meta.VideoMetaExtractor;
import com.github.videostat.rest.dto.VideoFileDTO;
import com.github.videostat.storage.VideoStorage;

@RunWith(MockitoJUnitRunner.class)
public class VideoServiceTest {

	@Mock
	private VideoMetaExtractor videoMetaService;

	@Mock
	private VideoStorage videoStorage;

	@Mock
	private VideoFileDTO videoFileDTO;

	@InjectMocks
	private VideoService videoService;

	@Test
	public void testProcess() throws Exception {
		videoService.process(videoFileDTO);
		verify(videoStorage, times(1)).store(anyObject());
		verify(videoStorage, times(1)).clean(anyString());
		verify(videoMetaService, times(1)).extract(anyString());
	}

}
