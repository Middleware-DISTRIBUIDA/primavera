package br.ufrn.imd.primavera.remoting.entities;

public class ObjectID {
    private final Class<?> clazz;

    public ObjectID(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

}
