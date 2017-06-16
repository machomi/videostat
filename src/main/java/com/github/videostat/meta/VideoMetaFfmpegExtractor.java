package com.github.videostat.meta;

import java.io.IOException;

import com.github.videostat.rest.dto.VideoMetadataDTO;
import com.github.videostat.rest.dto.VideoMetadataDTO.Size;
import com.github.videostat.rest.dto.VideoMetadataDTO.VideoMetadataDTOBuilder;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

public class VideoMetaFfmpegExtractor implements VideoMetaExtractor {
	
	private FFprobe fFprobe;
	
	public VideoMetaFfmpegExtractor(FFprobe fFprobe) {
		this.fFprobe = fFprobe;
	}

	@Override
	public VideoMetadataDTO extract(String filePath) throws IOException {
		final FFmpegProbeResult result = fFprobe.probe(filePath);
		final VideoMetadataDTOBuilder builder = VideoMetadataDTO.builder();
		result.streams.forEach(stream -> {
			switch (stream.codec_type) {
			case AUDIO:
				builder.audioBitRate(stream.bit_rate);
				builder.audioCodec(stream.codec_name);
				break;
			case VIDEO:
				builder.videoBitRate(stream.bit_rate);
				builder.videoCodec(stream.codec_name);
				builder.size(new Size(stream.width, stream.height));
				break;
			}

		});
		return builder.duration(result.getFormat().duration).build();
	}

}
