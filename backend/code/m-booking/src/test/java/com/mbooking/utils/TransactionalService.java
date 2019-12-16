package com.mbooking.utils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class TransactionalService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> void runInNewTransaction(Runnable runnable) {
        runnable.run();
    }

}
