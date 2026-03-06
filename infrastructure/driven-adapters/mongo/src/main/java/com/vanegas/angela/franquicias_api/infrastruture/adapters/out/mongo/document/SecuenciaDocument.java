package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "secuencias")
public class SecuenciaDocument {
    

	@Id
	private String id;

	private long valor;

	public SecuenciaDocument() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getValor() {
		return valor;
	}

	public void setValor(long valor) {
		this.valor = valor;
	}
}
