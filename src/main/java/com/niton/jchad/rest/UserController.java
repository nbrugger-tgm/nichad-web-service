package com.niton.jchad.rest;

import com.niton.jchad.ImageService;
import com.niton.jchad.rest.model.LoginResponse;
import com.niton.jchad.rest.model.UserInformation;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController("/user")
public interface UserController {

	ImageService getImageService();

	@PutMapping("/{id}")
	void register(
			@PathVariable
			String id,
			@RequestBody
			UserInformation info
	);

	@GetMapping("/{id}/session")
	LoginResponse login(
			@PathVariable
			String id,
			@RequestParam
			String password
	);

	@PostMapping("/{id}/profile_image")
	String changeProfileImage(
			@PathVariable
            String id,
			@RequestParam("image")
			MultipartFile multipartFile
	);

	@PostMapping("/{id}/display_name")
	void changeDisplayName(
			@PathVariable
			String id,
			@RequestParam
			String displayName
	);
}
