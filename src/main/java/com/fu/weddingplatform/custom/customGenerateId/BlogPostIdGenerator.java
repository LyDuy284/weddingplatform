package com.fu.weddingplatform.custom.customGenerateId;

import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

public class BlogPostIdGenerator implements IdentifierGenerator {

    @SneakyThrows
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        Connection connection = sharedSessionContractImplementor.connection();
        int count = 1;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT RIGHT(id, LENGTH(id) - LOCATE('-', id)) AS number \n" +
                "FROM blog_post \n" +
                "ORDER BY id DESC \n" +
                "LIMIT 1; ");
        if (rs.next()) {
            int maxId = rs.getInt("number") + 1;
            return String.format("BLOG-POST-%d", maxId);

        } else {
            return String.format("BLOG-POST-%d", count);
        }
    }
}