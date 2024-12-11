package br.ufrn.imd.primavera.tests.entities;

import java.io.Serializable;

public class RemoteRequest implements Serializable {
	private static final long serialVersionUID = 7836628557562234507L;

	private String requestId;
	private String operationName;

	public RemoteRequest() {
	}

	public RemoteRequest(String requestId, String operationName) {
		this.requestId = requestId;
		this.operationName = operationName;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getOperationName() {
		return operationName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		RemoteRequest that = (RemoteRequest) obj;
		return requestId.equals(that.requestId) && operationName.equals(that.operationName);
	}
}
