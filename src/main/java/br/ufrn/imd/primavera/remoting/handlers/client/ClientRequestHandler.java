package br.ufrn.imd.primavera.remoting.handlers.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientRequestHandler {
	private HttpClient httpClient;
	private ExecutorService executorService;
	private Duration timeout;

	public ClientRequestHandler() {
		this.executorService = Executors.newVirtualThreadPerTaskExecutor();

		this.httpClient = HttpClient.newBuilder().executor(executorService).version(HttpClient.Version.HTTP_2).build();

		this.timeout = Duration.ofSeconds(30);
	}

	public void setTimeout(Duration timeout) {
		this.timeout = timeout;
	}

	public Response sendRequest(Request request) throws IOException, InterruptedException, URISyntaxException {
		HttpRequest httpRequest = buildHttpRequest(request);
		HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		return new Response(httpResponse.statusCode(), httpResponse.body(),
				extractHeaders(httpResponse.headers().map()));
	}

	private HttpRequest buildHttpRequest(Request request) throws URISyntaxException {
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(request.buildUri()).timeout(timeout);

		for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
			builder.header(header.getKey(), header.getValue());
		}

		switch (request.getMethod().toUpperCase()) {
		case "GET":
			builder.GET();
			break;
		case "POST":
			builder.POST(HttpRequest.BodyPublishers.ofString(request.getBody() != null ? request.getBody() : ""));
			break;
		case "PUT":
			builder.PUT(HttpRequest.BodyPublishers.ofString(request.getBody() != null ? request.getBody() : ""));
			break;
		case "DELETE":
			builder.DELETE();
			break;
		default:
			throw new IllegalArgumentException("Método HTTP não suportado: " + request.getMethod());
		}

		return builder.build();
	}

	private Map<String, String> extractHeaders(Map<String, java.util.List<String>> headersMap) {
		Map<String, String> headers = new HashMap<>();
		headersMap.forEach((k, v) -> headers.put(k, String.join(", ", v)));
		return headers;
	}

	public void shutdown() {
		executorService.shutdown();
	}
}
