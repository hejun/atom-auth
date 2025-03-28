package io.github.hejun.atom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

/**
 * 授权服务器配置
 *
 * @author HeJun
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfigurer configurer = OAuth2AuthorizationServerConfigurer.authorizationServer();
		http
				.securityMatcher(configurer.getEndpointsMatcher())
				.with(configurer, authorizationServer ->
						authorizationServer
								.authorizationEndpoint(authorizationEndpoint ->
										authorizationEndpoint.consentPage("/consent")
								)
								.oidc(Customizer.withDefaults())
				)
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests.anyRequest().authenticated()
				)
				.exceptionHandling(exceptionHandling ->
						exceptionHandling
								.defaultAuthenticationEntryPointFor(
										new LoginUrlAuthenticationEntryPoint("/login"),
										new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
								)
				);
		return http.build();
	}

}
