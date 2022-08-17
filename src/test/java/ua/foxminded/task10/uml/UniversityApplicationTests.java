package ua.foxminded.task10.uml;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = {"classpath:drop-tables.sql", "classpath:create-table-students.sql", "classpath:create-table-groups.sql"})
class UniversityApplicationTests {

	@Test
	void contextLoads() {
	}

}
