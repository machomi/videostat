package com.github.videostat.rest;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.github.videostat.VideostatApplication;
import com.github.videostat.service.VideoService;
import com.github.videostat.validation.VideoFileValidator;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VideostatApplication.class)
public class VideoControllerIntTest {

	@Autowired
	private VideoFileValidator validator;

	@Autowired
	private VideoService videoService;

	private VideoController videoController;

	private MockMvc restMvc;

	@Value("classpath:sample.mp4")
	private Resource video;

	@Before
	public void setUp() throws Exception {
		videoController = new VideoController(videoService, validator);
		this.restMvc = MockMvcBuilders.standaloneSetup(videoController).build();
	}

	@Test
	public void testStat() throws Exception {
		restMvc.perform(fileUpload("/video/stat")
				.file(new MockMultipartFile("video", "sample.mp4", "video/mp4", video.getInputStream()))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.duration").value(closeTo(30.0, 0.5)))
				.andExpect(jsonPath("$.size.width").value("320"))
				.andExpect(jsonPath("$.size.height").value("240")).andExpect(jsonPath("$.videoBitRate").value("381504"))
				.andExpect(jsonPath("$.videoCodec").value("h264")).andExpect(jsonPath("$.audioBitRate").value("32122"))
				.andExpect(jsonPath("$.audioCodec").value("aac"));
	}

}
