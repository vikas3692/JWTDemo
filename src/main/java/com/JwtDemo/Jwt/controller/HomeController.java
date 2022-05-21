package com.JwtDemo.Jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.JwtDemo.Jwt.Utility.JWTUtility;
import com.JwtDemo.Jwt.model.JWTRequest;
import com.JwtDemo.Jwt.model.JWTResponse;
import com.JwtDemo.Jwt.service.UserService;

@RestController
public class HomeController {

	@Autowired
	private JWTUtility jwtUtility;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String home()
	{
		return "Welcome to the first spring security app with JWT";
	}
	
	
	@PostMapping("/authenticate")
	public JWTResponse authonticate(@RequestBody JWTRequest jwtRequest) throws Exception
	{
		System.out.println("in authenticate API");
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
					);
		} catch (BadCredentialsException e) {
			// TODO: handle exception
			throw new Exception("Invalid Credentials",e);
		}
		
		final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
		
		final String token = jwtUtility.generateToken(userDetails);
		
		System.out.println(token);
		
		return new JWTResponse(token);
		
	}
}
