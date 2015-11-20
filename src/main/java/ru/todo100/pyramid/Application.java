package ru.todo100.pyramid;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.todo100.pyramid.config.Config;
import ru.todo100.pyramid.socket.Server;

/**
 * @author Igor Bobko <limit-speed@yandex.ru>
 */

public class Application
{
	final static String SERVER_BEAN = "server";
	public static void main(String[] args)
	{
		final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
		Server server = applicationContext.getBean(SERVER_BEAN, Server.class);
		server.start();
	}
}
