package com.mbooking.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Component
public class DatabaseHelper {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Transactional
    public void rollback_database() {
        try {
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            entityManager.getMetamodel().getEntities().stream().forEach(type -> entityManager.createQuery("DELETE FROM " + type.getName()).executeUpdate());
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
            Resource script = new ClassPathResource("import_data_h2.sql");
            ScriptUtils.executeSqlScript(dataSource.getConnection(), script);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
