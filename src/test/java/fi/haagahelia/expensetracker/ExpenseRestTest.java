package fi.haagahelia.expensetracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import fi.haagahelia.expensetracker.model.Expense;
import fi.haagahelia.expensetracker.model.ExpenseRepository;
import fi.haagahelia.expensetracker.model.Category;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "server.port=0")
@AutoConfigureMockMvc(addFilters = false)
public class ExpenseRestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ExpenseRepository expenserepository;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/expenses"; // must match your controller @RequestMapping
    }

    @Test
    public void getAllExpensesShouldReturnList() {
        ResponseEntity<Expense[]> response = restTemplate.getForEntity(baseUrl(), Expense[].class);

        // Check HTTP status
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check body is not null and contains some expenses
        Expense[] expenses = response.getBody();
        assertThat(expenses).isNotNull();
        assertThat(expenses.length).isGreaterThan(0);
    }

    @Test
    public void getExpenseByIdShouldReturnExpense() {
        // First get all expenses to find a valid ID
        ResponseEntity<Expense[]> allExpensesResponse = restTemplate.getForEntity(baseUrl(), Expense[].class);
        Expense[] expenses = allExpensesResponse.getBody();

        assertThat(expenses).isNotNull();
        assertThat(expenses.length).isGreaterThan(0);

        Long firstId = expenses[0].getId();

        // GET expense by ID
        ResponseEntity<Expense> response = restTemplate.getForEntity(baseUrl() + "/" + firstId, Expense.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(firstId);
        assertThat(response.getBody().getName()).isEqualTo(expenses[0].getName());
    }

    // ---- POST /api/expenses ----
    @Test
    public void createExpenseShouldReturnCreatedExpense() {

        Expense newExpense = new Expense("Flowers", Category.Other, 20, LocalDate.parse("2025-10-27"),
                "for the birthday");

        ResponseEntity<Expense> response = restTemplate.postForEntity(baseUrl(), newExpense, Expense.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Flowers");
    }

    // ---- PUT /api/expenses/{id} ----
    @Test
    public void updateExpenseShouldReturnUpdatedExpense() {
        // First create a expense

        Expense expense = new Expense("Postcards", Category.Other, 10, LocalDate.parse("2025-11-01"), "for friends");
        expenserepository.save(expense);

        expense.setName("Updated Title");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Expense> entity = new HttpEntity<>(expense, headers);

        ResponseEntity<Expense> response = restTemplate.exchange(
                baseUrl() + "/" + expense.getId(),
                HttpMethod.PUT,
                entity,
                Expense.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Title");
    }

    // ---- DELETE /api/expenses/{id} ----
    @Test
    public void deleteExpenseShouldRemoveExpense() {
        // First create a expense

        Expense expense = new Expense("Cake", Category.Food, 17, LocalDate.parse("2025-11-02"), "for family");
        expenserepository.save(expense);

        // Delete
        restTemplate.delete(baseUrl() + "/" + expense.getId());

        // Verify deletion
        ResponseEntity<Expense> response = restTemplate.getForEntity(baseUrl() + "/" + expense.getId(), Expense.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}