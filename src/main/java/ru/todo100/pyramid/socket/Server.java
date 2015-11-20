package ru.todo100.pyramid.socket;

/**
 * A Server that accepts client requests for later processing service.
 *
 * @author Igor Bobko <limit-speed@yandex.ru>
 */
public interface Server
{
	/**
	 *  Starts the server and transfers the program to the waiting mode
	 */
	void start();
}
