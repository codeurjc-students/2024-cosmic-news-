package es.codeurjc.cosmic_news.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.security.jwt.AuthResponse;
import es.codeurjc.cosmic_news.security.jwt.AuthResponse.Status;
import es.codeurjc.cosmic_news.security.jwt.LoginRequest;
import es.codeurjc.cosmic_news.security.jwt.UserLoginService;
import es.codeurjc.cosmic_news.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class LoginRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserLoginService loginService;

	@Operation(summary = "Login user")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Login successful",
			content = @Content(schema = @Schema(implementation = AuthResponse.class))
		)
	})
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@CookieValue(name = "accessToken", required = false) String accessToken,
			@CookieValue(name = "refreshToken", required = false) String refreshToken,
			@RequestBody LoginRequest loginRequest) {
		
		return loginService.login(loginRequest, accessToken, refreshToken);
	}

	@Operation(summary = "Refresh access token")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Refresh token successful",
			content = @Content(schema = @Schema(implementation = AuthResponse.class))
		)
	})
	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(
			@CookieValue(name = "refreshToken", required = false) String refreshToken) {

		return loginService.refresh(refreshToken);
	}

	@Operation(summary = "Logout user")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Logout successful",
			content = @Content(schema = @Schema(implementation = AuthResponse.class))
		)
	})
	@PostMapping("/logout")
	public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, loginService.logout(request, response)));
	}

	@Operation(summary = "Get user profile")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "User profile retrieved successfully",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)),
			}
		),
		@ApiResponse(
			responseCode = "400",
			description = "Bad Request: Missing or invalid authentication"
		),
		@ApiResponse(
			responseCode = "404",
			description = "Not Found: User not found"
		)
	})
	@GetMapping("/me")
	public ResponseEntity<Object> me(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String mail = authentication.getName();
		if (mail != null){
			User user = userService.findUserByMail(mail);
	
			if (user != null) {
				return ResponseEntity.status(HttpStatus.OK).body(user);
	
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		}else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
}
