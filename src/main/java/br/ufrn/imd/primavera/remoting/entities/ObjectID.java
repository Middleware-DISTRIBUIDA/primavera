package br.ufrn.imd.primavera.remoting.entities;

public class ObjectID {
    private final Class<?> clazz;
    private final Object instance;

    public ObjectID(Class<?> clazz, Object instance) {
        this.clazz = clazz;
        this.instance = instance;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public Object getInstance() {
        return this.instance;
    }

}
