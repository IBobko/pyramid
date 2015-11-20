package ru.todo100;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author Igor Bobko <limit-speed@yandex.ru>
 */
public class ClientSocket implements Runnable
{
	private static Logger logger = LoggerFactory.getLogger(ClientSocket.class);
	private Socket        socket;
	private WeightService weightService;
	private final static int MAX_REQUEST_SIZE = 1024;

	public WeightService getWeightService()
	{
		return weightService;
	}

	public ClientSocket(Socket socket, WeightService weightService)
	{
		this.socket = socket;
		this.weightService = weightService;
	}

	public String getRequestString(DataInputStream in)
	{
		StringBuilder builder = new StringBuilder();
		for (int j = 0; j <= MAX_REQUEST_SIZE; j++)
		{
			try
			{
				int i = in.read();
				if (i == 13)
				{
					break;
				}
				builder.append((char) i);
			}
			catch (IOException e)
			{
				logger.error(e.getMessage());
				break;
			}
		}
		return builder.toString();
	}

	public String[] getSlashParams(String str) throws ClientException {
		if (str != null && str.length() > 0) {
			return str.split("/");
		}
		throw new ClientException("BAD REQUEST",400);
	}

	public String[] getQueryParams(String str) throws ClientException {
		if (str != null && str.length() > 0) {
			str = str.replace("?","");

			Pattern pattern = Pattern.compile("level=(\\d+)&index=(\\d+)");
			Matcher matcher = pattern.matcher(str);
			if (!matcher.matches()) {
				throw new ClientException("BAD REQUEST",400);
			}
			return new String[]{matcher.group(1),matcher.group(2)};
		}
		throw new ClientException("BAD REQUEST",400);
	}
	public void run()
	{
		logger.info("Got a client :)");
		try
		{
			JSONObject jsonResult = new JSONObject();
			OutputStream sout = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(sout);
			try
			{
				InputStream sin = socket.getInputStream();

				DataInputStream in = new DataInputStream(sin);


				String request = getRequestString(in);


				int getPosition = request.indexOf("GET");
				if (getPosition == -1) {
					throw new ClientException("BAD REQUEST",400);
				}
				int httpVersionPosition = request.indexOf("HTTP");
				if (httpVersionPosition == -1 || getPosition > httpVersionPosition){
					throw new ClientException("BAD REQUEST",400);
				}
				String query = request.substring(getPosition + 3, httpVersionPosition).trim();

				Pattern pattern = Pattern.compile("/weight(\\?|/)(.*)");
				Matcher matcher = pattern.matcher(query);
				if (!matcher.matches()) {
					throw new ClientException("RESOURCE IS NOT FOUND",404);
				}

				String[] params;

				switch(matcher.group(1)) {
					case "?": params = getQueryParams(matcher.group(2)); break;
					case "/": params = getSlashParams(matcher.group(2)); break;
					default : throw new ClientException("BAD REQUEST",400);
				}

				Integer level;
				Integer index;
				try
				{
					level = Integer.parseInt(params[0]);
					index = Integer.parseInt(params[1]);
				} catch(NumberFormatException e) {
					throw new ClientException("Can't convert to integer " + params[0] + " or " + params[1],400);
				}

				float weight = getWeightService().getWeight(level, index);
				jsonResult.put("level",level);
				jsonResult.put("index",index);
				jsonResult.put("result",weight);
				jsonResult.put("code","200 OK");
			}
			catch (ClientException e)
			{
				jsonResult.put("error",e.getMessage());
				jsonResult.put("code",e.getCode());
			}
			finally
			{
				String result = "HTTP/1.1 " + jsonResult.getString("code") + "\n" +
						"Content-Type: application/json; charset=UTF-8\n" +
						"Content-Length: " + (jsonResult.toString().length()+1) + "\n" +
						"Connection: keep-alive\n\n" +
						"\n" +
						jsonResult.toString() + "\n";

				System.out.println(jsonResult.toString());
				out.writeUTF(result);
				out.flush();
				out.close();
				socket.close();
			}
		}
		catch (Exception x)
		{
			x.printStackTrace();
		}
	}
}
