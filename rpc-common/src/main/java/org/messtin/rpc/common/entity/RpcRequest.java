package org.messtin.rpc.common.entity;

import java.util.Arrays;
import java.util.Objects;

/**
 * The entity to pass from client to server.
 *
 * @author majinliang
 */
public class RpcRequest {
    private String requestId;
    private String interfaceName;
    private String serviceVersion;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] params;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcRequest that = (RpcRequest) o;
        return Objects.equals(requestId, that.requestId) &&
                Objects.equals(interfaceName, that.interfaceName) &&
                Objects.equals(serviceVersion, that.serviceVersion) &&
                Objects.equals(methodName, that.methodName) &&
                Arrays.equals(paramTypes, that.paramTypes) &&
                Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(requestId, interfaceName, serviceVersion, methodName);
        result = 31 * result + Arrays.hashCode(paramTypes);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
