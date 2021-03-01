package bcp.photoappwebclient.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import bcp.photoappwebclient.response.AlbumRest;

@Controller
public class AlbumsController {

	@Autowired
	OAuth2AuthorizedClientService oauth2ClientService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	WebClient webClient;

	@GetMapping("/albums")
	public String getAlbums(Model model, @AuthenticationPrincipal OidcUser principal) {

		String url = "http://localhost:8082/albums";

		List<AlbumRest> albums = webClient.get().uri(url).retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<AlbumRest>>() {}).block();

		model.addAttribute("albums", albums);

		return "albums";
	}

	@GetMapping("/albums_rest")
	public String getAlbumsRest(Model model, @AuthenticationPrincipal OidcUser principal) {

		String jwtAccessToken = getAccessToken().getTokenValue();
		System.out.println("\njwtAccessToken = " + jwtAccessToken + "\n");

		System.out.println("\nPrincipal = " + principal + "\n");
		OidcIdToken idToken = principal.getIdToken();
		String idTokenValue = idToken.getTokenValue();
		System.out.println("\nidTokenValue = " + idTokenValue + "\n");

		String url = "http://localhost:8082/albums";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwtAccessToken);
		HttpEntity<List<AlbumRest>> entity = new HttpEntity<>(headers);

		ResponseEntity<List<AlbumRest>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<AlbumRest>>() {
				});
		List<AlbumRest> albums = responseEntity.getBody();
		model.addAttribute("albums", albums);

		return "albums";
	}

	@GetMapping("/albums_plain")
	public String getAlbumsPlain(Model model, @AuthenticationPrincipal OidcUser principal) {

		System.out.println("\njwtAccessToken = " + getAccessToken().getTokenValue() + "\n");
		System.out.println("\nPrincipal = " + principal + "\n");
		System.out.println("\nidTokenValue = " + principal.getIdToken().getTokenValue() + "\n");

		AlbumRest album = new AlbumRest();
		album.setAlbumId("albumOne");
		album.setAlbumTitle("Album one title");
		album.setAlbumUrl("http://localhost:8082/albums/1");
		AlbumRest album2 = new AlbumRest();
		album2.setAlbumId("albumTwo");
		album2.setAlbumTitle("Album two title");
		album2.setAlbumUrl("http://localhost:8082/albums/2");
		model.addAttribute("albums", Arrays.asList(album, album2));

		return "albums";
	}

	/**
	 * Get Access Token JWT to be used in HTTP requests to others protected resource
	 * servers
	 */
	private OAuth2AccessToken getAccessToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		System.out.println("\noauthToken = " + oauthToken + "\n");
		OAuth2AuthorizedClient oauth2Client = oauth2ClientService
				.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
		return oauth2Client.getAccessToken();
	}

}