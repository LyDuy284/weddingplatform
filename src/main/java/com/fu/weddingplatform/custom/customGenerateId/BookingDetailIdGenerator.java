package com.fu.weddingplatform.custom.customGenerateId;

import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class BookingDetailIdGenerator implements IdentifierGenerator {

    @SneakyThrows
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o)
            throws HibernateException {
        Connection connection = sharedSessionContractImplementor.connection();
        int count = 1;
        Statement statement = connection.createStatement();
        ResultSet rs = statement
                .executeQuery("SELECT MAX(CAST(SUBSTRING_INDEX(id, '-', -1) AS UNSIGNED)) AS number \n" +
                        "FROM booking_detail \n" +
                        "ORDER BY id DESC \n");
        if (rs.next()) {
            int maxId = rs.getInt("number") + 1;
            return String.format("BOOKING-DETAIL-%d", maxId);

        } else {
            return String.format("BOOKING-DETAIL-%d", count);
        }
    }
}