package org.messtin.rpc.common.entity;

import java.util.Objects;

/**
 * The response to send from server to client.
 *
 * @author majinliang
 */
public class RpcResponse {

    private String requestId;
    private Exception exception;
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcResponse that = (RpcResponse) o;
        return Objects.equals(requestId, that.requestId) &&
                Objects.equals(exception, that.exception) &&
                Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, exception, result);
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", exception=" + exception +
                ", result=" + result +
                '}';
    }
}
