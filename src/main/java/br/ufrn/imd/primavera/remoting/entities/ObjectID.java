package br.ufrn.imd.primavera.remoting.entities;

import java.util.UUID;

public class ObjectID {
        private final Map<Class<?>, Object> instancesId = new HashMap<>();
    
        public <T> void addId(Class<T> clazz, Object id) {
            instancesId.put(clazz, id);
        }
    
        // Método para recuperar o ID associado a uma classe
        public <T> Object getId(Class<T> clazz) {
            return sharedInstances.get(clazz);
        }
    
        // Método para verificar se uma classe já possui um ID associado
        public <T> boolean containsId(Class<T> clazz) {
            return sharedInstances.containsKey(clazz);
        }
    
    
}
