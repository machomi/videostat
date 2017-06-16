package com.github.videostat.rest;

import static com.github.videostat.meta.VideoExtractorTestData.AUDIO_BIT_RATE;
import static com.github.videostat.meta.VideoExtractorTestData.CODEC_AUDIO;
import static com.github.videostat.meta.VideoExtractorTestData.CODEC_VIDEO;
import static com.github.videostat.meta.VideoExtractorTestData.DURATION;
import static com.github.videostat.meta.VideoExtractorTestData.HEIGHT;
import static com.github.videostat.meta.VideoExtractorTestData.VIDEO_BIT_RATE;
import static com.github.videostat.meta.VideoExtractorTestData.WIDTH;
import static com.github.videostat.meta.VideoExtractorTestData.dummyProbeResult;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.github.videostat.VideostatApplication;
import com.github.videostat.meta.VideoMetaFfmpegExtractor;
import com.github.videostat.service.VideoService;
import com.github.videostat.validation.VideoFileValidator;

import net.bramp.ffmpeg.FFprobe;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VideostatApplication.class)
public class VideoControllerTest {

	@Autowired
	private VideoFileValidator validator;

	@Autowired
	private VideoService videoService;

	@Mock
	private FFprobe ffProbe;

	private VideoController videoController;

	private MockMvc restMvc;

	@Before
	public void setUp() throws Exception {
		when(ffProbe.probe(anyString())).thenReturn(dummyProbeResult());
		ReflectionTestUtils.setField(videoService, "videoMetaService", new VideoMetaFfmpegExtractor(ffProbe));
		
		videoController = new VideoController(videoService, validator);
		this.restMvc = MockMvcBuilders.standaloneSetup(videoController).build();
	}

	@Test
	public void testStat() throws Exception {
		restMvc.perform(fileUpload("/video/stat")
				.file(new MockMultipartFile("video", "sample.mp4", "video/mp4", "videoDummyContent".getBytes()))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.duration").value(DURATION)).andExpect(jsonPath("$.size.width").value(WIDTH))
				.andExpect(jsonPath("$.size.height").value(HEIGHT))
				.andExpect(jsonPath("$.videoBitRate").value(VIDEO_BIT_RATE))
				.andExpect(jsonPath("$.videoCodec").value(CODEC_VIDEO))
				.andExpect(jsonPath("$.audioBitRate").value(AUDIO_BIT_RATE))
				.andExpect(jsonPath("$.audioCodec").value(CODEC_AUDIO));
	}

}
