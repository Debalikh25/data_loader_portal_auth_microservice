package com.cts.dlp.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import com.cts.dlp.services.CustomUserDetailsService;
import com.cts.dlp.utils.JwtUtil;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;



@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException , MalformedJwtException {
		
		  try{
			  
				// TODO Auto-generated method stub

				String header = request.getHeader("auth");

				String username = null;

				String jwt = null;

				if (header != null && header.startsWith("Bearer")) {

					jwt = header.substring(7);

					username = this.jwtUtil.extractUsername(jwt);

				}

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UserDetails user = this.customUserDetailsService.loadUserByUsername(username);

					if (this.jwtUtil.validateToken(jwt, user)) {

						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								user, null, user.getAuthorities());

						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

					}

				}

				filterChain.doFilter(request, response);
			  
		  }
		  catch(ExpiredJwtException ex){
			    
			   Map<String ,Boolean> res = new HashMap<String , Boolean>();
			    res.put("expired", true);
			   JSONObject jo = new JSONObject(res);
			    PrintWriter out = response.getWriter();
			    response.setContentType("application/json");
			    response.setStatus(200);
			    response.setCharacterEncoding("UTF-8");
			    out.print(jo);
			    out.flush();
		  }


	}

}
