package main.java.dev.pennywise.cli;

import dev.pennywise.model.*;
import dev.pennywise.service.FinanceService;
import dev.pennywise.repo.TransactionRepository;
import dev.pennywise.report.Report;
import dev.pennywise.util.ConsoleUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

/**
 * Main CLI program demonstrating:
 * - switch expressions for command handling
 * - pattern matching in instanceof
 * - method references, LVTI (var)
 * - catches checked and unchecked exceptions
 */
public class FinanceApp {
    public static void main(String[] args) {
        var repo = new TransactionRepository();
        var service = new FinanceService(repo);

        ConsoleUtils.println("Welcome to PennyWise (console)");
        boolean running = true;
        while (running) {
            var cmd = ConsoleUtils.readLine("\nEnter command (add, list, filter, report, clear, exit): ");
            switch (cmd.strip().toLowerCase()) {
                case "add" -> handleAdd(service);
                case "list" -> handleList(repo);
                case "filter" -> handleFilter(repo);
                case "report" -> handleReport(service);
                case "clear" -> { repo.clear(); ConsoleUtils.println("Cleared repo."); }
                case "exit" -> { ConsoleUtils.println("Goodbye."); running = false; }
                default -> ConsoleUtils.println("Unknown command");
            }
        }
    }

    private static void handleAdd(FinanceService service) {
        try {
            var desc = ConsoleUtils.readLine("Description: ");
            var amtStr = ConsoleUtils.readLine("Amount (e.g. 12.34): ");
            var catStr = ConsoleUtils.readLine("Category (FOOD,RENT,TRANSPORT,ENTERTAINMENT,UTILITIES,SALARY,MISC): ");
            var dateStr = ConsoleUtils.readLine("Date (YYYY-MM-DD) leave empty for today: ");

            var money = new Money(new BigDecimal(amtStr), "GBP");
            var category = Category.valueOf(catStr.toUpperCase());
            var date = dateStr.isBlank() ? LocalDate.now() : LocalDate.parse(dateStr);

            // demonstrate checked exception possibility
            service.addTransaction(desc, money, category, date);
            ConsoleUtils.println("Added transaction.");
        } catch (IllegalArgumentException iae) {
            ConsoleUtils.println("Invalid input: " + iae.getMessage());
        } catch (Exception e) {
            ConsoleUtils.println("Error adding transaction (checked): " + e.getMessage());
        }
    }

    private static void handleList(TransactionRepository repo) {
        var all = repo.all();
        if (all.isEmpty()) {
            ConsoleUtils.println("No transactions found.");
            return;
        }
        all.forEach(tr -> ConsoleUtils.println(formatRecord(tr)));
    }

    private static void handleFilter(TransactionRepository repo) {
        var catStr = ConsoleUtils.readLine("Category to filter by (leave empty for none): ");
        Predicate<TransactionRecord> p;
        if (catStr.isBlank()) {
            p = tr -> true;
        } else {
            try {
                var cat = Category.valueOf(catStr.toUpperCase());
                p = tr -> tr.category() == cat;
            } catch (IllegalArgumentException e) {
                ConsoleUtils.println("Unknown category; showing all.");
                p = tr -> true;
            }
        }
        var results = repo.filter(p);
        results.forEach(tr -> ConsoleUtils.println(formatRecord(tr)));
    }

    private static void handleReport(FinanceService service) {
        try {
            var yStr = ConsoleUtils.readLine("Year (e.g. 2025): ");
            var mStr = ConsoleUtils.readLine("Month (1-12): ");
            int y = Integer.parseInt(yStr);
            int m = Integer.parseInt(mStr);
            Report r = service.monthlyReport(y, m);
            ConsoleUtils.println(r);
            r.items().forEach(tr -> ConsoleUtils.println(formatRecord(tr)));
        } catch (NumberFormatException nfe) {
            ConsoleUtils.println("Invalid numbers.");
        }
    }

    private static String formatRecord(TransactionRecord tr) {
        return String.format("[%s] %s | %s | %s | %s",
                tr.id(), tr.description(), tr.money(), tr.category(), tr.date());
    }
}
