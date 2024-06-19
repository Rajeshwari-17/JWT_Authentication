package configurations;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.security.core.context.SecurityContextHolder;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.JwtServices;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private  HandlerExceptionResolver handlerExceptionResolver;
	private JwtServices jwtService;
	private UserDetailsService userDetailsService;
	
	public JwtAuthenticationFilter (
			JwtServices jwtService, UserDetailsService userDetailsService, HandlerExceptionResolver handlerExceeptionResolver){
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.handlerExceptionResolver = handlerExceptionResolver;
		
	}
	

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		if(authHeader==null || !authHeader.startsWith("Bearer"))
		{
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			final String jwt = authHeader.substring(7);
			final String userEmail = jwtService.extractUsername(jwt);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			if(userEmail!=null && authentication == null)
			{
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
				if(jwtService.isTokenValid(userEmail, userDetails))
				{
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
							);
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			filterChain.doFilter(request, response);
		}catch(Exception exception)
		{
			handlerExceptionResolver.resolveException(request,response,null,exception);
		}
		
	}
	
	

}
