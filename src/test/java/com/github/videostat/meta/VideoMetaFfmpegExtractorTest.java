package com.github.videostat.meta;

import static com.github.videostat.meta.VideoExtractorTestData.AUDIO_BIT_RATE;
import static com.github.videostat.meta.VideoExtractorTestData.CODEC_AUDIO;
import static com.github.videostat.meta.VideoExtractorTestData.CODEC_VIDEO;
import static com.github.videostat.meta.VideoExtractorTestData.DURATION;
import static com.github.videostat.meta.VideoExtractorTestData.HEIGHT;
import static com.github.videostat.meta.VideoExtractorTestData.VIDEO_BIT_RATE;
import static com.github.videostat.meta.VideoExtractorTestData.WIDTH;
import static com.github.videostat.meta.VideoExtractorTestData.dummyProbeResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.videostat.rest.dto.VideoMetadataDTO;

import net.bramp.ffmpeg.FFprobe;

@RunWith(MockitoJUnitRunner.class)
public class VideoMetaFfmpegExtractorTest {
	@Mock
	private FFprobe fFprobe;

	@InjectMocks
	private VideoMetaFfmpegExtractor videoMetaFfmpegExtractor;

	private static final String PATH = "path";

	@Before
	public void setUp() throws Exception {
		when(fFprobe.probe(PATH)).thenReturn(dummyProbeResult());
	}

	@Test
	public void testExtract() throws Exception {
		VideoMetadataDTO result = videoMetaFfmpegExtractor.extract(PATH);
		assertThat(result.getDuration()).isEqualTo(DURATION);
		assertThat(result.getSize()).hasFieldOrPropertyWithValue("width", WIDTH);
		assertThat(result.getSize()).hasFieldOrPropertyWithValue("height", HEIGHT);
		assertThat(result.getVideoCodec()).isEqualTo(CODEC_VIDEO);
		assertThat(result.getVideoBitRate()).isEqualTo(VIDEO_BIT_RATE);
		assertThat(result.getAudioCodec()).isEqualTo(CODEC_AUDIO);
		assertThat(result.getAudioBitRate()).isEqualTo(AUDIO_BIT_RATE);
	}

}
