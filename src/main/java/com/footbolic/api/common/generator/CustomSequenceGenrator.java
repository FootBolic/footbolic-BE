package com.footbolic.api.common.generator;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class CustomSequenceGenrator implements IdentifierGenerator {

    @Override
    public Serializable generate(
            SharedSessionContractImplementor session,
            Object object
    ) throws HibernateException {
        Connection connection = null;
        try {
            ConnectionProvider connectionProvider = session
                    .getFactory()
                    .getServiceRegistry()
                    .getService(ConnectionProvider.class);
            connection = connectionProvider.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT nextval()");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String generatedId = resultSet.getString(1);
                return generatedId;
            }
        } catch (SQLException e) {
            throw new HibernateException("Unable to generate sequence", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.debug("Unexpected error occurs while closing the connection");
                }
            }
        }
        return null;
    }
}
