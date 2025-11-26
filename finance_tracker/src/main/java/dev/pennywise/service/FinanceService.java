package main.java.dev.pennywise.service;
// Import other classes

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/*
* Business logic layer demonstrating:
* - method overloading
* - LVTI (var)
* - lambdas, method references, predicates
* - checked and unchecked exceptions usage delegated (see addTransaction)
*/

public class FinanceService{
    private final TransactionRepository repo;

    public FinanceService(TransactionRepository repo){
        this.repo = repo;
    }

    // Overloaded: create from components
    public void addTransaction(String description, Money money , Category category, LocalDate date) throws Exception {
        if (money.amount().sigum() < 0) throw new NegativeAmountException("Amount cannot be negative");
        var id = UUID.randomUUID().toString();
        var record = new TransactionRecord(id, description, money, category, date);
        repo.add(record);
    }

    // Overloading: simple variant
    public void addTransaction(String description, Money money, Category category) throws Exception {
        addTransaction(description, money, category, LocalDate.now()); // this() style chaining (mehtod chaining)
    }

    // variable args example - add multiple records
    public void addTransactions(TransactionRecord...records){
        repo.add(records);
    }

    // Filtering using Predicate and lambdas
    public List<TransactionRecord> find(Predicate<TransactionRecord> predicate){
        return repo.filter(predicate);
    }

    // Return an immutable report demonstrating custom immutable type usage
    public Report monthlyReport(int year, int month){
        var items = repo.filter(tr -> tr.date().getYear() == year && tr.date(). getMonthValue() == month);
        var total = items.stream()
            .map(tr -> tr.money().amount())
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        return new Report(year, month, List.copyOf(items), total, "GBP");
    }

    // Example uncheck exception thrown for demonstration
    public void riskyOperation(){
        throw new RuntimeException("Demo unchecked exception")
    }
}