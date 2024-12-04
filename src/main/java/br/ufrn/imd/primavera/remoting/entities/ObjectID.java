package br.ufrn.imd.primavera.remoting.entities;

import java.util.UUID;

public class ObjectID {
    private final String id;

    public ObjectID() {
        this.id = UUID.randomUUID().toString(); // Gera um ID único
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
