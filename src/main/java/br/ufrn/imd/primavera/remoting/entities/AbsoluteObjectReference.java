package br.ufrn.imd.primavera.remoting.entities;

public class AbsoluteObjectReference {
    private final String host;
    private final int port;
    private final String protocol;
    private final ObjectID objectId;

    public AbsoluteObjectReference(String host, int port, String protocol, ObjectID objectId) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.objectId = objectId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public ObjectID getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return String.format("%s://%s:%d/%s", protocol, host, port, objectId);
    }
}
