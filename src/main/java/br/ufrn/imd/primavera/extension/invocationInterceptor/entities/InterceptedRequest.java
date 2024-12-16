package br.ufrn.imd.primavera.extension.invocationInterceptor.entities;

import java.util.Map;

public class InterceptedRequest extends AbstractInterceptedData {
	private Map<String, String> queryParams;

	public InterceptedRequest() {
	}

	public InterceptedRequest(String body, Map<String, String> headers, String path, Map<String, String> queryParams, Context context) {
		super(body, headers, path, context);
		this.queryParams = queryParams;
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

}
