package com.niton.jchad;

import com.niton.jchad.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
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
			Path filePath = folder.resolve(fileName + ".png");
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}

	public void removeImage(String oldId) throws IOException {
		Path file = folder.resolve(oldId + ".png");
		Files.delete(file);
	}

	public InputStream getImageInputStream(String profilePictureId) throws IOException {
		Path file = folder.resolve(profilePictureId + ".png");
		return Files.newInputStream(file);
	}

	public boolean hasImage(User id) {
		return Files.exists(folder.resolve(id.getProfilePictureId()+".png"));
	}

	public BufferedImage render(String displayName) {
		BufferedImage image = new BufferedImage(80,80,BufferedImage.TYPE_INT_RGB);
		Graphics2D      graphics = (Graphics2D) image.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		char c = Character.toUpperCase(displayName.charAt(0));
		int
				r = (int) (Math.pow(2, c)%255),
				g = (int) (Math.pow(c, c)%255),
				b = (int) (Math.pow(c, 2)%255);
		boolean darkText = (r+g+b)/3 > (255/3)*2;
		graphics.setColor(new Color(r,g,b));
		graphics.fillRect(0,0,80,80);
		graphics.setColor(darkText?Color.DARK_GRAY:Color.WHITE);
		graphics.setFont(new Font(Font.MONOSPACED, Font.BOLD, 70));
		graphics.drawChars(new char[]{c},0,1,20,60);
		graphics.dispose();
		return image;
	}
}
