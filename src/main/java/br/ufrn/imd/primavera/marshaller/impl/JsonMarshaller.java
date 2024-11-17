package br.ufrn.imd.primavera.marshaller.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufrn.imd.primavera.marshaller.exceptions.SerializationException;
import br.ufrn.imd.primavera.marshaller.interfaces.Marshaller;

public class JsonMarshaller implements Marshaller<String> {
	private final ObjectMapper objectMapper;

	public JsonMarshaller() {
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public String marshal(Object obj) throws SerializationException {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new SerializationException("Failed to serialize object to JSON", e);
		}
	}

	@Override
	public <T> T unmarshal(String data, Class<T> targetClass) throws IOException, SerializationException {
		try {
			return objectMapper.readValue(data, targetClass);
		} catch (JsonMappingException e) {
			throw new SerializationException("Failed to map JSON to object", e);
		} catch (JsonProcessingException e) {
			throw new SerializationException("Failed to deserialize JSON data", e);
		}
	}
}
