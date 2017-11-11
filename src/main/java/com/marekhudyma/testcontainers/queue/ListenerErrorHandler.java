package com.marekhudyma.testcontainers.queue;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Log4j
@Component
public class ListenerErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        log.error(" ------------------------ handle error", t);
    }
}



