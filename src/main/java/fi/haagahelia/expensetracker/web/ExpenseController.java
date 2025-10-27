package fi.haagahelia.expensetracker.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;

import fi.haagahelia.expensetracker.model.Category;
import fi.haagahelia.expensetracker.model.Expense;
import fi.haagahelia.expensetracker.model.ExpenseRepository;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseRepository repository;

    @GetMapping("/expenselist")
    public String expenseList(Model model) {
        model.addAttribute("expenses", repository.findAll());
        return "expenselist";
    }

    @GetMapping("/addexpense")
    public String addExpenseForm(Model model) {
        model.addAttribute("expense", new Expense());
        model.addAttribute("categories", Category.values());
        return "addexpense";
    }

    @PostMapping("/saveexpense")
    public String saveExpense(@ModelAttribute Expense expense) {
        repository.save(expense);
        return "redirect:/expenselist";
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deleteexpense/{id}")
    public String deleteExpense(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return "redirect:/expenselist";
    }

    @GetMapping("/editexpense/{id}")
    public String editExpense(@PathVariable("id") Long id, Model model) {
        Expense expense = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expense Id:" + id));
        model.addAttribute("expense", expense);
        model.addAttribute("categories", Category.values());
        return "editexpense";
    }

    // @GetMapping("/login")
    // public String login() {
    // return "login"; // returns login.html
    // }
}