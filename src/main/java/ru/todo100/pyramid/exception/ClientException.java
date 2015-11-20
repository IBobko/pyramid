package ru.todo100.pyramid.exception;

/**
 * @author Igor Bobko <limit-speed@yandex.ru>
 */

public class ClientException extends Exception
{
	int code;
	public ClientException(String str,int code){
		super(str);
		this.code = code;

	}
	public String getCode() {
		switch (code){
			case 400: return "400 BAD REQUEST";
			case 404: return "404 NOT FOUND";
			default: return "";
		}
	}
}

