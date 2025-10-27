package fi.haagahelia.expensetracker.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.expensetracker.model.Expense;
import fi.haagahelia.expensetracker.model.ExpenseRepository;

@RestController
@RequestMapping("/api")
public class ExpenseRestController {

    @Autowired
    private ExpenseRepository expenseRepository;

    // REST service that returns all expenses
    @GetMapping("/expenses")
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // REST service that returns one expense by id
    @GetMapping("/expenses/{id}")
    public Optional<Expense> findExpenseRest(@PathVariable("id") Long expenseId) {
        return expenseRepository.findById(expenseId);
    }

    // POST create a new expense
    @PostMapping("/expenses")
    public Expense createExpense(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    // PUT update an expense
    @PutMapping("/expenses/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody Expense expenseDetails) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense != null) {
            expense.setName(expenseDetails.getName());
            expense.setCategory(expenseDetails.getCategory());
            expense.setAmount(expenseDetails.getAmount());
            expense.setDate(expenseDetails.getDate());
            expense.setComment(expenseDetails.getComment());
            expenseRepository.save(expense);
        }
        return expense;
    }

    // DELETE an expense
    @DeleteMapping("/expenses/{id}")
    public void deleteExpense(@PathVariable Long id) {
        expenseRepository.deleteById(id);
    }
}
