package uk.co.tso.tilda.web.config;

import org.eclipse.rdf4j.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class TildaRoutes implements WebMvcConfigurer {

    @Autowired
    @Qualifier("tilda-configuration-model")
    private Model config;

    /*
    @Bean
    public RouterFunction<ServerResponse> hello() {
        return RouterFunctions.route()
                .GET("/hello-world",
                        RequestPredicates.accept(MediaType.TEXT_PLAIN),
                        request -> ServerResponse.ok().body("Hello, World!"))
                .build();
    }
    */

}
