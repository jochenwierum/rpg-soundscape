package de.jowisoftware.rpgsoundscape.player.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {
    @Bean
    public ParameterNamesModule parameterNamesModule() {
        return new ParameterNamesModule(JsonCreator.Mode.PROPERTIES);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.annotationIntrospector(new RecordJacksonAnnotationIntrospector())
                // https://github.com/spring-projects/spring-framework/issues/25987
                .featuresToDisable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }
}
