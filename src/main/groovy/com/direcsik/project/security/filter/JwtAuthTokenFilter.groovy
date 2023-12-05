package com.direcsik.project.security.filter

import com.direcsik.project.exception.JwtTokenException
import com.direcsik.project.service.impl.UserServiceImpl
import com.direcsik.project.util.JwtTokenHelper
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

import static com.direcsik.project.util.Constants.AUTHORIZATION
import static com.direcsik.project.util.Constants.BEARER_TOKEN
import static com.direcsik.project.util.Constants.EMPTY_STRING
import static java.util.Optional.ofNullable

@Configuration
class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenHelper tokenHelper
    @Autowired
    private UserServiceImpl userService

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Optional<String> jwtToken = getJwtToken(request)
            if (jwtToken.isPresent()) {
                tokenHelper.validateJwtToken(jwtToken.get())
                String username = tokenHelper.getUserNameFromJwtToken(jwtToken.get())
                UserDetails userDetails = userService.loadUserByUsername(username)
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request))
                SecurityContextHolder.getContext().setAuthentication(authentication)
            }
        } catch (JwtTokenException exception) {
            logger.error(String.format("Cannot set user authentication -> Message: {%s}", exception.getMessage()))
        }
        filterChain.doFilter(request, response)
    }

    private Optional<String> getJwtToken(HttpServletRequest request) {
        return ofNullable(request.getHeader(AUTHORIZATION))
                .filter { header -> header.startsWith(BEARER_TOKEN) }
                .map { header -> header.replace(BEARER_TOKEN, EMPTY_STRING) }
    }
}
