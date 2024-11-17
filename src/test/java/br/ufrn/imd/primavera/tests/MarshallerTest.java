package br.ufrn.imd.primavera.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import br.ufrn.imd.primavera.marshaller.MarshallerFactory;
import br.ufrn.imd.primavera.marshaller.MarshallerType;
import br.ufrn.imd.primavera.marshaller.interfaces.Marshaller;
import br.ufrn.imd.primavera.tests.entities.RemoteRequest;

public class MarshallerTest {

	@Test
	public void testJsonMarshallerSerialization() throws Exception {
		// Configuração do objeto de teste
		RemoteRequest request = new RemoteRequest("123", "getData");

		// Obter o JsonMarshaller do MarshallerFactory
		@SuppressWarnings("unchecked")
		Marshaller<String> jsonMarshaller = (Marshaller<String>) MarshallerFactory.getMarshaller(MarshallerType.JSON);

		// Serializar o objeto
		String json = jsonMarshaller.marshal(request);
		assertNotNull(json, "JSON Marshaller retornou nulo");

		// Desserializar o objeto
		RemoteRequest deserializedRequest = jsonMarshaller.unmarshal(json, RemoteRequest.class);
		assertEquals(request, deserializedRequest, "Objeto desserializado não é igual ao original");
	}

	@Test
	public void testBinaryMarshallerSerialization() throws Exception {
		// Configuração do objeto de teste
		RemoteRequest request = new RemoteRequest("456", "updateData");

		// Obter o BinaryMarshaller do MarshallerFactory
		@SuppressWarnings("unchecked")
		Marshaller<byte[]> binaryMarshaller = (Marshaller<byte[]>) MarshallerFactory.getMarshaller(MarshallerType.BYTE);

		// Serializar o objeto
		byte[] binaryData = binaryMarshaller.marshal(request);
		assertNotNull(binaryData, "Binary Marshaller retornou nulo");

		// Desserializar o objeto
		RemoteRequest deserializedRequest = binaryMarshaller.unmarshal(binaryData, RemoteRequest.class);
		assertEquals(request, deserializedRequest, "Objeto desserializado não é igual ao original");
	}

	@Test
	public void testMarshallerTypeFromName() {
		MarshallerType jsonType = MarshallerType.fromName("JSON");
		assertEquals(MarshallerType.JSON, jsonType, "MarshallerType.fromName não retornou o tipo correto para JSON");

		MarshallerType byteType = MarshallerType.fromName("BYTE");
		assertEquals(MarshallerType.BYTE, byteType, "MarshallerType.fromName não retornou o tipo correto para BYTE");

		assertThrows(IllegalArgumentException.class, () -> MarshallerType.fromName("UNKNOWN"),
				"MarshallerType.fromName não lançou exceção para tipo desconhecido");
	}

	@Test
	public void testMarshallerTypeDescription() {
		assertEquals("JSON Marshaller", MarshallerType.JSON.getDescription(),
				"Descrição incorreta para JSON Marshaller");
		assertEquals("Binary Marshaller", MarshallerType.BYTE.getDescription(),
				"Descrição incorreta para Binary Marshaller");
	}
}
