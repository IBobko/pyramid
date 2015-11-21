
package ru.todo100.pyramid.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import ru.todo100.pyramid.socket.Server;
import ru.todo100.pyramid.socket.impl.ServerImpl;
import ru.todo100.pyramid.service.WeightService;
import ru.todo100.pyramid.service.impl.WeightServiceImpl;

@Configuration
@PropertySource("file:config.properties")
@ComponentScan("ru.todo100.pyramid")
public class Config
{
	@Autowired
	private Environment env;

	@Bean
	public Server server() {
		return new ServerImpl(Integer.parseInt(env.getProperty("server.port")));
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public WeightService weightService() {
		int maxLevel = env.getProperty("weightService.maxLevel",Integer.class);
		int pieceWeight = env.getProperty("weightService.pieceWeight",Integer.class);
		return new WeightServiceImpl(maxLevel, pieceWeight);
	}
}
