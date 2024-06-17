package com.fu.weddingplatform.custom.customGenerateId;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class PromotionIdGenerate implements IdentifierGenerator {

    private static final AtomicInteger counter = new AtomicInteger(1);


    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        int count = counter.getAndIncrement();
        return String.format("PROMOTION-%d", count);
    }
}