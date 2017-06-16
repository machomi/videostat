package com.github.videostat.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.github.videostat.rest.dto.VideoFileDTO;

@RunWith(MockitoJUnitRunner.class)
public class VideoFileValidatorTest {

	@Spy
	private List<String> supportedFormats = new ArrayList<>();

	@InjectMocks
	private VideoFileValidator videoFileValidator;

	@InjectMocks
	private VideoFileDTO videoFileDTO;

	@Mock
	private MultipartFile video;

	@Mock
	private Errors errors;

	private static final String VIDEO_FORMAT = "Video/Type";

	@Before
	public void setUp() throws Exception {
		supportedFormats.add(VIDEO_FORMAT);
	}

	@Test
	public void testSupports() throws Exception {
		assertThat(videoFileValidator.supports(VideoFileDTO.class)).isTrue();
	}

	@Test
	public void testValidateEmptyFile() throws Exception {
		when(video.isEmpty()).thenReturn(true);
		videoFileValidator.validate(videoFileDTO, errors);
		verify(errors).rejectValue(eq("video"), eq("video.empty"), anyString());
	}

	@Test
	public void testValidateWrongContentType() throws Exception {
		when(video.isEmpty()).thenReturn(false);
		when(video.getContentType()).thenReturn("wrong format");
		videoFileValidator.validate(videoFileDTO, errors);
		verify(errors).rejectValue(eq("video"), eq("video.format"), anyObject(), anyString());
	}

	@Test
	public void testValidate() throws Exception {
		when(video.isEmpty()).thenReturn(false);
		when(video.getContentType()).thenReturn(VIDEO_FORMAT);
		videoFileValidator.validate(videoFileDTO, errors);
		verify(errors, never()).rejectValue(anyString(), anyString(), anyString());
		verify(errors, never()).rejectValue(anyString(), anyString(), anyObject(), anyString());
	}

}
