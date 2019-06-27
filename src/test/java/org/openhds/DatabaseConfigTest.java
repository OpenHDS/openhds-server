package org.openhds;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.openhds.web.beans.DatabaseConfigBean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@PropertySource({"classpath:database.properties"})
@SpringBootTest
public class DatabaseConfigTest {
	@Autowired
	Environment env;
	
//	@Test
//	public void databaseConfigBeanTest() {
//		DatabaseConfigBean bean = new DatabaseConfigBean();
//		assertEquals(bean.getDbPassword(), "data");
//	}
	
	@Test
	public void propertiesTest() {
		System.out.println(env.getProperty("dbPass"));
		assertNotNull(env.getProperty("dbPass"), "data");
	}
}
