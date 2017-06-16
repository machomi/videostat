package com.github.videostat.meta;

import java.util.Arrays;

import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import net.bramp.ffmpeg.probe.FFmpegStream.CodecType;

public class VideoExtractorTestData {

	public static final Double DURATION = Double.MAX_VALUE;
	public static final Integer WIDTH = 100;
	public static final Integer HEIGHT = 200;
	public static final String CODEC_VIDEO = "video";
	public static final Long VIDEO_BIT_RATE = 500l;
	public static final String CODEC_AUDIO = "audio";
	public static final Long AUDIO_BIT_RATE = 600l;

	public static FFmpegProbeResult dummyProbeResult() {
		FFmpegProbeResult expectedResult = new FFmpegProbeResult();
		expectedResult.format = new FFmpegFormat();
		expectedResult.format.duration = DURATION;

		FFmpegStream videoStream = new FFmpegStream();
		videoStream.codec_type = CodecType.VIDEO;
		videoStream.width = WIDTH;
		videoStream.height = HEIGHT;
		videoStream.codec_name = CODEC_VIDEO;
		videoStream.bit_rate = VIDEO_BIT_RATE;

		FFmpegStream audioStream = new FFmpegStream();
		audioStream.bit_rate = AUDIO_BIT_RATE;
		audioStream.codec_type = CodecType.AUDIO;
		audioStream.codec_name = CODEC_AUDIO;

		expectedResult.streams = Arrays.asList(videoStream, audioStream);

		return expectedResult;
	}

}