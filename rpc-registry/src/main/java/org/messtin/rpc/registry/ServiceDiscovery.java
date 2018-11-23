package org.messtin.rpc.registry;

/**
 * The interface for discover service.
 *
 * @author majinliang
 */
public interface ServiceDiscovery {

    /**
     * Discover an address of the service according to the service name.
     *
     * @param name the service name
     * @return the address for service
     * @throws Exception the exception when discover service.
     */
    String discover(String name) throws Exception;
}
