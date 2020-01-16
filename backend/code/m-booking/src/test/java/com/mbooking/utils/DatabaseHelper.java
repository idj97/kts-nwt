package com.mbooking.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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


    public void dropAndImport(String sqlScript) {
    	Connection connection = null;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            entityManager.getMetamodel().getEntities().stream().forEach(type -> entityManager.createQuery("DELETE FROM " + type.getName()).executeUpdate());
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
            Resource script = new ClassPathResource(sqlScript);
            connection = dataSource.getConnection();
            ScriptUtils.executeSqlScript(connection, script);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        entityManager.close();
        if (connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    }

    
}
