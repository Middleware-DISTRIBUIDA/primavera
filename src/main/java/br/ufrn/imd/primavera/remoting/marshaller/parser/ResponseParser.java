package br.ufrn.imd.primavera.remoting.marshaller.parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ResponseParser {

	public static HTTPResponse parseHTTPResponse(String httpResponse) {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new java.io.ByteArrayInputStream(httpResponse.getBytes())));
		return parseHTTPResponse(reader);
	}

	public static HTTPResponse parseHTTPResponse(BufferedReader reader) {
		try {
			String statusLine = reader.readLine();
			String[] statusParts = statusLine.split(" ", 3);
			int statusCode = Integer.parseInt(statusParts[1]);
			String reasonPhrase = statusParts[2];

			Map<String, String> headers = new HashMap<>();
			String line;
			while ((line = reader.readLine()) != null && !line.isEmpty()) {
				String[] parts = line.split(": ", 2);
				headers.put(parts[0], parts[1]);
			}

			StringBuilder body = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				body.append(line);
			}

			return new HTTPResponse(statusCode, reasonPhrase, headers, body.toString());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to parse HTTP response", e);
		}
	}
}
