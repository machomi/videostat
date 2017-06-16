package com.github.videostat.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

@RunWith(MockitoJUnitRunner.class)
public class VideoLocalStorageTest {

	@Spy
	private MultipartFile file = new MockMultipartFile("Video", "video.avi", "video content",
			"videocontent".getBytes());

	@InjectMocks
	private VideoLocalStorage videoLocalStorage;

	@Before
	public void setUp() throws Exception {
		ReflectionTestUtils.setField(videoLocalStorage, "path", System.getProperty("java.io.tmpdir"));
	}

	@Test
	public void testStore() throws Exception {
		String path = videoLocalStorage.store(file);
		File tmpFile = new File(path);
		assertThat(tmpFile).exists().isFile();
		assertThat(contentOf(tmpFile)).contains("videocontent");
		tmpFile.delete();
	}

	@Test
	public void testClean() throws Exception {
		File f = File.createTempFile("test", ".dummy");
		Files.write("testcontent".getBytes(), f);
		videoLocalStorage.clean(f.getAbsolutePath());
		assertThat(f).doesNotExist();
	}

}
