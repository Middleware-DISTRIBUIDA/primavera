package br.ufrn.imd.primavera.remoting.entities;

public class AbsoluteObjectReference {
    private final String host;
    private final int port;
    private final ObjectID objectId;

    public AbsoluteObjectReference(String host, int port, ObjectID objectId) {
        this.host = host;
        this.port = port;
        this.objectId = objectId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public ObjectID getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return String.format("%s://%d/%s", host, port, objectId);
    }
}
