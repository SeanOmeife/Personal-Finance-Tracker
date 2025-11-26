package main.java;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;

// ---------------- ENUM (categorising transactions) ----------------
enum Category {
    FOOD, TRANSPORT, ENTERTAINMENT, BILLS, OTHER
}

// ---------------- RECORD (Immutable DTO) ----------------
// Represents a single transaction
// Defensive validation keeps object safe and immutable
record Transaction(String description, double amount, LocalDate date, Category category) {
    public Transaction {
        Objects.requireNonNull(description);
        Objects.requireNonNull(date);
        Objects.requireNonNull(category);
    }
}

// ---------------- CUSTOM IMMUTABLE TYPE ----------------
// Demonstrates immutability + defensive practice
final class UserProfile {
    private final String username;

    public UserProfile(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username; // no modification possible
    }
}

// ---------------- INTERFACE + SEALED + DEFAULT/STATIC METHODS ----------------
sealed interface Tracker permits FinanceTracker {
    // required method to add a transaction
    void add(Transaction t);

    // default method uses polymorphism to calculate totals
    default double total() {
        return list().stream().mapToDouble(Transaction::amount).sum();
    }

    // static helper summarises tracker
    static void printSummary(Tracker t) {
        System.out.println("Total: " + t.total());
    }

    // must return transactions (defensive copy enforced by implementation)
    List<Transaction> list();
}

// ---------------- CLASS USING INHERITANCE + OVERRIDING ----------------
final class FinanceTracker implements Tracker {
    private final List<Transaction> transactions = new ArrayList<>();

    // method overloading + LVTI
    public void add(String description, double amount, Category category) {
        add(description, amount, LocalDate.now(), category);
    }

    public void add(String description, double amount, LocalDate date, Category category) {
        add(new Transaction(description, amount, date, category));
    }

    @Override
    public void add(Transaction t) {
        transactions.add(t);
    }

    @Override
    public List<Transaction> list() {
        return List.copyOf(transactions); // defensive copy
    }

    // varargs usage
    public void addMany(Transaction... txs) {
        for (var t : txs) {
            add(t);
        }
    }

    // lambda + Predicate
    public List<Transaction> filter(Predicate<Transaction> p) {
        return transactions.stream().filter(p).toList();
    }
}

// ---------------- CLI MENU IMPLEMENTATION ----------------
public class FinanceApp {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        var tracker = new FinanceTracker();
        var user = new UserProfile("Sean");

        System.out.println("Welcome, " + user.getUsername() + "!");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> addTransactionCLI(tracker);
                case 2 -> listTransactionsCLI(tracker);
                case 3 -> filterByCategoryCLI(tracker);
                case 4 -> Tracker.printSummary(tracker);
                case 0 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }

        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println("==== Finance Tracker Menu ====");
        System.out.println("1. Add Transaction");
        System.out.println("2. List All Transactions");
        System.out.println("3. Filter by Category");
        System.out.println("4. View Total");
        System.out.println("0. Exit");
    }

    private static void addTransactionCLI(FinanceTracker tracker) {
        System.out.print("Description: ");
        String desc = scanner.nextLine();

        double amount = readDouble("Amount: ");
        Category category = chooseCategory();

        tracker.add(desc, amount, category);
        System.out.println("Transaction added.");
    }

    private static void listTransactionsCLI(FinanceTracker tracker) {
        System.out.println("--- Transactions ---");
        tracker.list().forEach(System.out::println); // method reference
    }

    private static void filterByCategoryCLI(FinanceTracker tracker) {
        Category c = chooseCategory();
        var results = tracker.filter(t -> t.category() == c);

        System.out.println("Filtered Results:");
        results.forEach(System.out::println);
    }

    private static Category chooseCategory() {
        System.out.println("Categories:");
        for (int i = 0; i < Category.values().length; i++) {
            System.out.println(i + 1 + ". " + Category.values()[i]);
        }

        int choice = readInt("Choose category: ") - 1;
        if (choice < 0 || choice >= Category.values().length) return Category.OTHER;

        return Category.values()[choice];
    }

    private static int readInt(String msg) {
        System.out.print(msg);
        return Integer.parseInt(scanner.nextLine());
    }

    private static double readDouble(String msg) {
        System.out.print(msg);
        return Double.parseDouble(scanner.nextLine());
    }
}
