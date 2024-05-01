package uk.co.tso.tilda.web.config;

import org.eclipse.rdf4j.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import uk.co.tso.tilda.core.api.Endpoint;
import uk.co.tso.tilda.core.config.ItemEndpointConfigurationContext;
import uk.co.tso.tilda.core.config.ItemsEndpointConfigurationContext;
import uk.co.tso.tilda.web.config.properties.TildaProperties;
import uk.co.tso.tilda.web.router.ItemEndpointRouterFactory;
import uk.co.tso.tilda.web.router.ItemsEndpointRouterFactory;
import uk.co.tso.tilda.web.support.Configurations;

import java.util.UUID;

@Configuration
@DependsOn("tilda-configuration-model")
public class TildaRouteFactory implements BeanFactoryPostProcessor, EnvironmentAware, BeanFactoryAware {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ConfigurableEnvironment environment;
    private BeanFactory beanFactory;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final var props = Binder.get(environment).bind("tilda", TildaProperties.class).get();
        final var model = (Model) beanFactory.getBean("tilda-configuration-model");

        final var configuration = Configurations.configuration(props, model);

        configuration.apis().forEach((name, api) -> {
            api.endpoints().forEach(endpointIRI -> {
                if (configuration.endpoints().containsKey(endpointIRI)) {
                    var endpoint  = configuration.endpoints().get(endpointIRI);

                    if (endpoint instanceof Endpoint.Items items) {
                        var config = ItemsEndpointConfigurationContext.build(configuration, api, items);
                        var route = ItemsEndpointRouterFactory.build(config);
                        beanFactory.registerSingleton(UUID.randomUUID().toString(), route);
                    }

                    if (endpoint instanceof Endpoint.Item item) {
                        var config = ItemEndpointConfigurationContext.build(configuration, api, item);
                        var route = ItemEndpointRouterFactory.build(config);
                        beanFactory.registerSingleton(UUID.randomUUID().toString(), route);
                    }
                } else {
                    logger.warn("Endpoint {} was defined in API {} but no matching definition found", endpointIRI, name);
                }
            });
        });

    }
}
