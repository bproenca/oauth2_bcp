package bcp.photoappwebclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class PhotoappwebclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoappwebclientApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public WebClient webClient(ClientRegistrationRepository clientRegistrationrepository,
			OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository) 
	{
		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrationrepository, oAuth2AuthorizedClientRepository);

		oauth2.setDefaultOAuth2AuthorizedClient(true);

		// this configuration will make the access token be included in every http-request
		// (with webclient) 
		// Be carefull not to use this web-client with other 3rd party we services
		// because you will send out the "jwt access token"
		return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
	}

}
