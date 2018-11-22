package org.messtin.rpc.sample;

import org.messtin.rpc.server.RpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@RpcService(Hello.class)
@Component
public class HelloImp implements Hello {
    @Override
    public String say() {
        return "HelloImp say";
    }

    @Override
    public String talk() {
        return "HelloImp talk";
    }
}
