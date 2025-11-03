package fi.haagahelia.expensetracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import fi.haagahelia.expensetracker.model.AppUser;
import fi.haagahelia.expensetracker.model.AppUserRepository;
import fi.haagahelia.expensetracker.model.Category;
import fi.haagahelia.expensetracker.model.Expense;
import fi.haagahelia.expensetracker.model.ExpenseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest(classes = ExpensetrackerApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExpensetrackerRepositoryTest {

    @Autowired
    private ExpenseRepository expenserepository;

    @Autowired
    private AppUserRepository userrepository;

    @BeforeEach
    public void clearDatabase() {
        expenserepository.deleteAll();
    }

    @Test
    public void findByNameShouldReturnCategory() {

        Expense expense = new Expense("Postcards", Category.Other, 10, LocalDate.parse("2025-11-01"), "for friends");
        expenserepository.save(expense);

        List<Expense> expenses = expenserepository.findByName("Postcards");

        assertThat(expenses).hasSize(1);
        assertThat(expenses.get(0).getCategory()).isEqualTo(Category.Other);
    }

    @Test
    public void createNewExpense() {
        Expense expense = new Expense("Postcards", Category.Other, 10, LocalDate.parse("2025-11-01"), "for friends");
        expenserepository.save(expense);
        assertThat(expense.getId()).isNotNull();
    }

    @Test
    public void deleteNewExpense() {

        Expense expense = new Expense("Postcards", Category.Other, 10, LocalDate.parse("2025-11-01"), "for friends");
        expenserepository.save(expense);

        List<Expense> expenses = expenserepository.findByName("Postcards");
        assertThat(expenses).hasSize(1);

        expenserepository.delete(expenses.get(0));

        List<Expense> newExpenses = expenserepository.findByName("Postcards");
        assertThat(newExpenses).isEmpty();
    }

    @Test
    public void findByUsernameShouldReturnUser() {
        AppUser appuser = userrepository.findByUsername("user");

        assertThat(appuser).isNotNull();
        assertThat(appuser.getRole()).isEqualTo("USER");
    }
}