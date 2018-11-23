package org.messtin.rpc.registry;

/**
 * The interface to registry a service.
 *
 * @author majinliang
 */
public interface ServiceRegistry {

    /**
     * Register the service for the service name and address.
     *
     * @param name    the name of service
     * @param address the address of the service
     * @throws Exception the exception when register service.
     */
    void register(String name, String address) throws Exception;
}
