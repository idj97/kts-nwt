package com.mbooking.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

@Component
public class DatabaseHelper {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private DataSource dataSource;

    public void dropAndImport() {
        try {
            dropData();
            importData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dropData() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        entityManager.getMetamodel().getEntities().stream().forEach(type -> entityManager.createQuery("DELETE FROM " + type.getName()).executeUpdate());
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void importData() throws SQLException {
        Resource script = new ClassPathResource("import_data_h2.sql");
        Connection connection = dataSource.getConnection();
        ScriptUtils.executeSqlScript(connection, script);
        connection.close();
    }
}
