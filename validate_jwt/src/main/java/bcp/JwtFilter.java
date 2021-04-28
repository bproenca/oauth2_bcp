package bcp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

public class JwtFilter extends GenericFilterBean {

    private final String keycloakHostPort;

    public JwtFilter(String keycloakHostPort) {
        this.keycloakHostPort = keycloakHostPort;
    }

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
            return;
        }

        final String token = authHeader.substring(7); // The part after "Bearer "

        try {
            DecodedJWT decodedJwt = JWT.decode(token);
            JwkProvider provider = new UrlJwkProvider(getUrl(req));
            Jwk jwk = provider.get(decodedJwt.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(decodedJwt);
            
            // Check expiration
            if (decodedJwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
                ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Exired token.");
                return;
            }
        }
        catch (JWTDecodeException e) {
            ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT doesn't have a valid JSON format.");
            return;
        } catch (JwkException e) {
            ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " + e.getMessage());
            return;
        } catch (MalformedURLException e) {
            ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        chain.doFilter(req, res);
    }

   

    private URL getUrl(ServletRequest req) throws MalformedURLException {
        String requestURI = ((HttpServletRequest) req).getRequestURI();
        Pattern pattern = Pattern.compile("/api/tenant/(.*?)/");
        Matcher matcher = pattern.matcher(requestURI);
        String tenant;
        if (matcher.find()) {
            tenant = matcher.group(1);
        } else {
            throw new MalformedURLException("Tenant not found in Request URI");
        }

        return new URL(keycloakHostPort + "/auth/realms/" + tenant + "/protocol/openid-connect/certs");
    }

}
