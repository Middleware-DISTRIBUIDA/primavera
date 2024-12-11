package br.ufrn.imd.primavera.remoting.marshaller.parser;

import java.util.Map;

public interface ResponseRequest {
    int getStatusCode();
    Map<String, String> getHeaders();
    String getBody();
}
