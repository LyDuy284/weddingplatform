package com.fu.weddingplatform.custom.customGenerateId;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RequiredArgsConstructor
public class QuoteIdGenerate implements IdentifierGenerator {

    @SneakyThrows
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        Connection connection = sharedSessionContractImplementor.connection();
        int count = 1;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT CAST(SUBSTRING_INDEX(id, '-', -1) AS UNSIGNED) AS number \n" +
                "FROM quote \n" +
                "ORDER BY id DESC \n" +
                "LIMIT 1; ");
        if (rs.next()) {
            int maxId = rs.getInt("number") + 1;
            return String.format("QUOTE-%d", maxId);

        } else {
            return String.format("QUOTE-%d", count);
        }
    }
}