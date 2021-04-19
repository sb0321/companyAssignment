package com.assign.organization.persistence;

import com.assign.organization.config.JpaDatabaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Import(JpaDatabaseConfig.class)
@ExtendWith(SpringExtension.class)
public class DBConnectionTests {

    @Autowired
    private DataSource dataSource;

    @Test
    public void dbConnectionTest() throws SQLException {

        log.info(dataSource.getConnection().toString());

        assertNotNull(dataSource);
        assertNotNull(dataSource.getConnection());
    }


}
