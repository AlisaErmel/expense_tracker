package fi.haagahelia.expensetracker;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.haagahelia.expensetracker.model.AppUser;
import fi.haagahelia.expensetracker.model.AppUserRepository;
import fi.haagahelia.expensetracker.model.Category;
import fi.haagahelia.expensetracker.model.Expense;
import fi.haagahelia.expensetracker.model.ExpenseRepository;

@SpringBootApplication
public class ExpensetrackerApplication {

	private static final Logger log = LoggerFactory.getLogger(ExpensetrackerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ExpensetrackerApplication.class, args);
	}

	@Bean
	public CommandLineRunner bookDemo(ExpenseRepository repository, AppUserRepository arepository) {
		return (args) -> {

			log.info("Saving some example books...");
			repository.save(new Expense("Taxi", Category.Transport, 34, LocalDate.parse("2025-10-20"), "from airport"));
			repository.save(new Expense("Coffee", Category.Food, 5, LocalDate.parse("2025-10-21"), "in uni"));
			repository.save(
					new Expense("Museum", Category.Entertainment, 12, LocalDate.parse("2025-10-18"), "Impressionism"));

			// Create users: admin/admin user/user
			AppUser user1 = new AppUser("user",
					"$2a$12$aqclPxIymLl/qtLBqsW4GeuRr6MYjYnWnrp9iWOfV3LVLt9qaN1Wy",
					"USER");
			AppUser user2 = new AppUser("admin",
					"$2a$12$T4nNEcyYBxYoKy1vkI0EzeIdfgrMMJdy5m9JIMr/63PZ/RNvJnHL2",
					"ADMIN");
			arepository.save(user1);
			arepository.save(user2);

			log.info("Fetching all books:");
			for (Expense expense : repository.findAll()) {
				log.info(expense.toString());
			}

		};
	}

}
