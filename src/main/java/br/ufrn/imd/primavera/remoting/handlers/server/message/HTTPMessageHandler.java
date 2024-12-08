package br.ufrn.imd.primavera.remoting.handlers.server.message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import br.ufrn.imd.primavera.remoting.entities.ResponseWrapper;
import br.ufrn.imd.primavera.remoting.enums.HTTPStatus;
import br.ufrn.imd.primavera.remoting.enums.Verb;
import br.ufrn.imd.primavera.remoting.handlers.server.Response;
import br.ufrn.imd.primavera.remoting.marshaller.MarshallerFactory;
import br.ufrn.imd.primavera.remoting.marshaller.MarshallerType;
import br.ufrn.imd.primavera.remoting.marshaller.interfaces.Marshaller;

public final class HTTPMessageHandler extends MessageHandler {

	private static final String SERVER_HEADER = "Server: WebServer\r\n";
	private static final String CONTENT_TYPE_JSON = "Content-Type: application/json\r\n";
	private static final String CONNECTION_CLOSE = "Connection: close\r\n";
	private static final String BAD_REQUEST_MESSAGE = "Bad Request";

	private final Socket socket;

	public HTTPMessageHandler(String taskName, Socket socket) {
		super(taskName);
		this.socket = socket;
	}

	@Override
	public void run() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String verb;
			String path;
			String context = "";

			try {
				String headerLine = in.readLine();
				if (headerLine == null || headerLine.isEmpty()) {
					sendErrorResponse(HTTPStatus.BAD_REQUEST, BAD_REQUEST_MESSAGE);
					return;
				}

				StringTokenizer tokenizer = new StringTokenizer(headerLine);
				verb = tokenizer.nextToken().toUpperCase();
				path = tokenizer.nextToken();

			} catch (Exception e) {
				logAndRespondError("Error parsing request line", HTTPStatus.BAD_REQUEST);
				return;
			}

			Map<String, String> headers = parseHeaders(in);

			if(headers.containsKey("Context")) {
				context = headers.get("Context");
				headers.remove("Context");
			}

			String body = readBody(in, headers);

			processRequest(verb, path, body, headers, context);

		} catch (IOException e) {
			logAndRespondError("IO Error handling request", HTTPStatus.INTERNAL_SERVER_ERROR);
		} finally {
			closeSocket();
		}
	}

	private void processRequest(String verb, String path, String body, Map<String, String> headers, String context) {
		try {

			Response<Object> response = new Response<>();

			@SuppressWarnings("unchecked")
			ResponseWrapper<Object> responseEntity = (ResponseWrapper<Object>) requestDispatcher
					.dispatchRequest(Verb.valueOf(verb), path, body, headers, context);

			Map<String, String> responseHeaders = responseEntity.getHeaders().toMap();

			response.setCode(responseEntity.getStatus());
			response.setMessage(responseEntity.getStatus().getReasonPhrase());
			response.setEntity(responseEntity.getBody());

			@SuppressWarnings("unchecked")
			Marshaller<String> m = (Marshaller<String>) MarshallerFactory.getMarshaller(MarshallerType.JSON);

			String bodyResponse = m.marshal(response.getEntity());
			sendResponse(response.getStatus(), bodyResponse, responseHeaders);
		} catch (Exception e) {
			e.printStackTrace();
			logAndRespondError("Error processing request", HTTPStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Map<String, String> parseHeaders(BufferedReader in) throws IOException {
		Map<String, String> headers = new HashMap<>();
		String line;
		while ((line = in.readLine()) != null && !line.isEmpty()) {
			int delimiterIndex = line.indexOf(":");
			if (delimiterIndex != -1) {
				String headerName = line.substring(0, delimiterIndex).trim();
				String headerValue = line.substring(delimiterIndex + 1).trim();
				headers.put(headerName, headerValue);
			}
		}
		return headers;
	}

	private String readBody(BufferedReader in, Map<String, String> headers) {
		StringBuilder body = new StringBuilder();
		try {
			int length = headers.getOrDefault("Content-Length", "0").isEmpty() ? 0
					: Integer.parseInt(headers.get("Content-Length"));
			if (length > 0) {
				char[] buffer = new char[length];
				in.read(buffer, 0, length);
				body.append(buffer);
			}
		} catch (IOException | NumberFormatException e) {
			System.err.println("Error reading request body: " + e.getMessage());
		}
		return body.toString();
	}

	private void sendResponse(HTTPStatus status, String responseBody, Map<String, String> additionalHeaders) {
		try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
			String statusLine = "HTTP/1.0 " + status.value() + " " + status.getReasonPhrase() + "\r\n";

			Map<String, String> headers = new HashMap<>();
			headers.put("Server", SERVER_HEADER.trim());
			headers.put("Content-Type", CONTENT_TYPE_JSON.trim());
			headers.put("Connection", CONNECTION_CLOSE.trim());
			headers.put("Content-Length", String.valueOf(responseBody.length()));

			if (additionalHeaders != null) {
				for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
					headers.put(entry.getKey(), entry.getValue());
				}
			}

			StringBuilder headersString = new StringBuilder();
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				headersString.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
			}

			out.writeBytes(statusLine);
			out.writeBytes(headersString.toString());
			out.writeBytes("\r\n");
			out.writeBytes(responseBody);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error sending response: " + e.getMessage(), e);
		}
	}

	private void sendErrorResponse(HTTPStatus status, String message) {
		sendResponse(status, "{\"error\": \"" + message + "\"}", null);
	}

	private void logAndRespondError(String logMessage, HTTPStatus status) {
		sendErrorResponse(status, logMessage);
	}

	private void closeSocket() {
		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			logger.error("Error closing socket: " + e.getMessage());
		}
	}
}
