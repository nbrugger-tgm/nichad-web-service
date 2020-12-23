package com.niton.jchad;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageService {
	private Path folder = Paths.get("images");

	public void saveFile(
			String fileName,
			MultipartFile multipartFile
	) throws IOException {

		if (!Files.exists(folder)) {
			Files.createDirectories(folder);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = folder.resolve(fileName+".png");
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}

	public void removeImage(String oldId) throws IOException {
		Path file = folder.resolve(oldId+".png");
		Files.delete(file);
	}

	public InputStream getImageInputStream(String profilePictureId) throws IOException {
		Path file = folder.resolve(profilePictureId+".png");
		return Files.newInputStream(file);
	}
}
