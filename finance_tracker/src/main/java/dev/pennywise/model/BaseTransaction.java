package main.java.dev.pennywise.model;

import java.time.LocalDate;
import java.util.UUID;

/*
 * BaseTransaction demonstrates:
 * - Inheritance
 * - Constructor chaining (this())
 * - super() usage is shown in children calling this parent constructor
 * - overriding and polymorphism via the abstract summary() method
 * 
 * This class implements Entry (sealed interface). It's declared non-sealed to allow 
 * IncomeTransaction and ExpenseTransaction to extend it.
 */
public non-sealed abstract class BaseTransaction implements Entry{
    protected final String id;
    protected final String description;    
    protected final Money amount;
    protected final Category category;
    protected final LocalDate date;
    
    // this() constructor chaining example
    public BaseTransaction(Money amount, Category category) {
        this(UUID.randomUUID().toString(), "No Description", amount, category, LocalDate.now());
    }

    public BaseTransaction(String id, String description, Money amount, Category category, LocalDate date){
        this.id = id;
        this.description = description == null? "" : description;
        this.amount = amount;
        this.category = category;
        this.date = date == null ? LocalDate.now() : date;
    }

    public String id() {
        return id;
    }
    public String description() {
        return description;
    }
    public Money amount() {
        return amount;
    }
    public Category category() {
        return category;
    }
    public LocalDate date() {
        return date;
    }

    // abstract method to be overwritten by subclasses (polynorphism)
    public abstract String summary();
}
