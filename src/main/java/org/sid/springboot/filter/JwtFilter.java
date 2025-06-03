package org.sid.springboot.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sid.springboot.configuration.JwtUtils;
import org.sid.springboot.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Notre requête va passer par plusieurs filtre avant d'arriver à notre server.
//On va définir un filtre spécifique JWT pour permettre à chaque requête qui va arriver soit filtrer par Jwt.
// Comme c'est un filtre, il doit être du coup un composant pour qu'il puisse être charger comme bean dans spring.
@Component //
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    //Pour filtrer la requête et de vérifier que le token est correcte.
    private final CustomUserDetailsService customUserDetailsService;

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1er on récupére le token. Pour cela, il faut récupérer le header dans la requête.
        final String header = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if(header !=null && header.startsWith("Bearer ")){
            token = header.substring(7);
            username = jwtUtils.extractUsername(token); //Pour extraire le username à partir du token.
        }
        //Il faut vérifier que le user existe et qu'il n'est pas authentifier.
        if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null){
            // On récupére les infos de l'utilisateur
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username); //on charge les infos de l'utilisateur.
            if(jwtUtils.validateToken(token,userDetails)){
                //Si le token est valide, on charge le context et définir que le user est bien authentifié.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                //Pour vérifier avec quelle requête j'ai authentifié mon utilisateur.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // On va setter les infos dans SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // du fait qu'il y'a beaucoup de filtre qui s'effectue à la chaine, pour passer au filtre suivant on met ceci
        filterChain.doFilter(request,response);
    }
}
