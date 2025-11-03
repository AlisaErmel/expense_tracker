package fi.haagahelia.expensetracker;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fi.haagahelia.expensetracker.web.ExpenseController;
import fi.haagahelia.expensetracker.web.ExpenseRestController;

@SpringBootTest
class ExpensetrackerApplicationTests {

	private final ExpenseController expensecontroller;
	private final ExpenseRestController restcontroller;

	@Autowired
	public ExpensetrackerApplicationTests(ExpenseController expensecontroller, ExpenseRestController restcontroller) {
		this.expensecontroller = expensecontroller;
		this.restcontroller = restcontroller;
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void controllerLoads() throws Exception {
		assertThat(expensecontroller).isNotNull();
		assertThat(restcontroller).isNotNull();
	}

}