package ru.todo100.pyramid.exception;

/**
 * @author Igor Bobko <limit-speed@yandex.ru>
 */

public class ServiceException extends Exception
{
	public ServiceException(String str) {
		super(str);
	}
}

