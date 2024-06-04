package reactiongame.infrastructure.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactiongame.infrastructure.security.PlayerTokenBaseAccessTokenArgumentResolver;
import reactiongame.infrastructure.security.PlayerTokenBaseAccessTokenBindInterceptor;
import reactiongame.infrastructure.security.PlayerTokenBaseSecureInterceptor;

import java.util.List;

@Configuration
class WebConfiguration implements WebMvcConfigurer {

    private final PlayerTokenBaseAccessTokenBindInterceptor playerTokenBaseAccessTokenBindInterceptor;

    private final PlayerTokenBaseSecureInterceptor playerTokenBaseSecureInterceptor;

    private final PlayerTokenBaseAccessTokenArgumentResolver playerTokenBaseAccessTokenArgumentResolver;

    public WebConfiguration(
            final PlayerTokenBaseAccessTokenBindInterceptor playerTokenBaseAccessTokenBindInterceptor,
            final PlayerTokenBaseSecureInterceptor playerTokenBaseSecureInterceptor,
            final PlayerTokenBaseAccessTokenArgumentResolver playerTokenBaseAccessTokenArgumentResolver
    ) {
        this.playerTokenBaseAccessTokenBindInterceptor = playerTokenBaseAccessTokenBindInterceptor;
        this.playerTokenBaseSecureInterceptor = playerTokenBaseSecureInterceptor;
        this.playerTokenBaseAccessTokenArgumentResolver = playerTokenBaseAccessTokenArgumentResolver;
    }

    @Override
    public void addInterceptors(@NonNull final InterceptorRegistry registry) {
        final var interceptors = List.of(
                playerTokenBaseAccessTokenBindInterceptor,
                playerTokenBaseSecureInterceptor
        );

        interceptors.forEach(it -> registry.addInterceptor(it)
                .addPathPatterns("/**")
                .excludePathPatterns("/health", "/error"));
    }

    @Override
    public void addArgumentResolvers(@NonNull final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(playerTokenBaseAccessTokenArgumentResolver);
    }

    @Override
    public void addCorsMappings(@NonNull final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowCredentials(true);
    }
}
