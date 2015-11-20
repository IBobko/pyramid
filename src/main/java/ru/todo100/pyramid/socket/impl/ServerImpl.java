package ru.todo100.pyramid.socket.impl;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;

import ru.todo100.pyramid.service.WeightService;
import ru.todo100.pyramid.socket.ClientSocket;
import ru.todo100.pyramid.socket.Server;

/**
 * A Server that accepts client requests for later processing service.
 *
 * @author Igor Bobko <limit-speed@yandex.ru>
 */


public class ServerImpl implements Server
{
	private static Logger logger = LoggerFactory.getLogger(ServerImpl.class);

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private WeightService weightService;
	private int port = 80;

	public ServerImpl(int port)
	{
		if (port > 0)
		{
			this.port = port;
		}
	}

	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	@Required
	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public WeightService getWeightService()
	{
		return weightService;
	}

	@Required
	public void setWeightService(final WeightService weightService)
	{
		this.weightService = weightService;
	}

	/**
	 * Starts the server and transfers the program to the waiting mode
	 */
	public void start()
	{
		try (ServerSocket ss = new ServerSocket(this.port))
		{
			logger.info(getMessageSource().getMessage("server.listen", new Object[] {this.port + ""}, null));
			logger.info(getMessageSource().getMessage("server.waiting", null, null));

			final ExecutorService service = Executors.newCachedThreadPool();
			//noinspection InfiniteLoopStatement
			while (true)
			{
				Socket socket = ss.accept();
				service.submit(new ClientSocket(socket, getWeightService()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
