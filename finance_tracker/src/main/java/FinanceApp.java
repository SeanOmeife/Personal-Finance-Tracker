import java.time.LocalDate; // Import class for handling dates
import java.util.ArrayList; // Import dynamic array implementation
import java.util.List; // Import interface for lists
import java.util.Objects; // Import utility class for object checks
import java.util.Scanner; // Import Scanner to read user input
import java.util.function.Predicate; // Import functional interface for filtering

// ---------------- ENUM (categorising transactions) ----------------
enum Category {
    FOOD, TRANSPORT, ENTERTAINMENT, BILLS, OTHER // Different types of spending
}

// ---------------- RECORD (Immutable DTO) ----------------
record Transaction(String description, double amount, LocalDate date, Category category) {
    public Transaction {
        Objects.requireNonNull(description); // Make sure description is not null
        Objects.requireNonNull(date); // Make sure date is not null
        Objects.requireNonNull(category); // Make sure category is not null
    }
}

// ---------------- CUSTOM IMMUTABLE TYPE ----------------
final class UserProfile {
    private final String username; // Store username

    public UserProfile(String username) {
        this.username = username; // Set username
    }

    public String getUsername() {
        return username; // Return username
    }
}

// ---------------- INTERFACE + SEALED + DEFAULT/STATIC METHODS ----------------
sealed interface Tracker permits FinanceTracker {
    void add(Transaction t); // Add a transaction

    default double total() {
        return list().stream().mapToDouble(Transaction::amount).sum(); // Sum all transaction amounts
    }

    static void printSummary(Tracker t) {
        System.out.println("Total: " + t.total()); // Print total of transactions
    }

    List<Transaction> list(); // Return list of transactions
}

// ---------------- CLASS USING INHERITANCE + OVERRIDING ----------------
final class FinanceTracker implements Tracker {
    private final List<Transaction> transactions = new ArrayList<>(); // Store transactions

    public void add(String description, double amount, Category category) {
        add(description, amount, LocalDate.now(), category); // Overloaded method using current date
    }

    public void add(String description, double amount, LocalDate date, Category category) {
        add(new Transaction(description, amount, date, category)); // Create transaction and add it
    }

    @Override
    public void add(Transaction t) {
        transactions.add(t); // Add transaction to list
    }

    @Override
    public List<Transaction> list() {
        return List.copyOf(transactions); // Return copy to prevent modification
    }

    public void addMany(Transaction... txs) {
        for (var t : txs) add(t); // Add multiple transactions using varargs
    }

    public List<Transaction> filter(Predicate<Transaction> p) {
        return transactions.stream().filter(p).toList(); // Return transactions that match predicate
    }
}

// ---------------- CLI MENU IMPLEMENTATION ----------------
public class FinanceApp {

    private static final Scanner scanner = new Scanner(System.in); // Scanner for user input

    public static void main(String[] args) {
        var tracker = new FinanceTracker(); // Create tracker object
        var user = new UserProfile("Sean"); // Create user object

        System.out.println("Welcome, " + user.getUsername() + "!"); // Greet user

        boolean running = true; // Control loop
        while (running) {
            printMenu(); // Show menu
            int choice = readInt("Choose an option: "); // Read user choice

            switch (choice) {
                case 1 -> addTransactionCLI(tracker); // Add transaction
                case 2 -> listTransactionsCLI(tracker); // List transactions
                case 3 -> filterByCategoryCLI(tracker); // Filter by category
                case 4 -> Tracker.printSummary(tracker); // Show total
                case 0 -> running = false; // Exit program
                default -> System.out.println("Invalid option."); // Invalid input
            }

            System.out.println(); // Add space after each menu iteration
        }

        System.out.println("Goodbye!"); // Exit message
    }

    private static void printMenu() {
        System.out.println("==== Finance Tracker Menu ===="); // Menu header
        System.out.println("1. Add Transaction"); // Option 1
        System.out.println("2. List All Transactions"); // Option 2
        System.out.println("3. Filter by Category"); // Option 3
        System.out.println("4. View Total"); // Option 4
        System.out.println("0. Exit"); // Option 0
    }

    private static void addTransactionCLI(FinanceTracker tracker) {
        System.out.print("Description: "); // Prompt for description
        String desc = scanner.nextLine(); // Read description

        double amount = readDouble("Amount: "); // Prompt and read amount
        Category category = chooseCategory(); // Ask for category

        tracker.add(desc, amount, category); // Add transaction
        System.out.println("Transaction added."); // Confirm addition
    }

    private static void listTransactionsCLI(FinanceTracker tracker) {
        System.out.println("--- Transactions ---"); // Header
        tracker.list().forEach(System.out::println); // Print each transaction
    }

    private static void filterByCategoryCLI(FinanceTracker tracker) {
        Category c = chooseCategory(); // Ask for category
        var results = tracker.filter(t -> t.category() == c); // Filter transactions

        System.out.println("Filtered Results:"); // Header
        results.forEach(System.out::println); // Print filtered transactions
    }

    private static Category chooseCategory() {
        System.out.println("Categories:"); // List categories
        for (int i = 0; i < Category.values().length; i++) {
            System.out.println(i + 1 + ". " + Category.values()[i]); // Print each category
        }

        int choice = readInt("Choose category: ") - 1; // Read choice and adjust index
        if (choice < 0 || choice >= Category.values().length) return Category.OTHER; // Default to OTHER if invalid
        return Category.values()[choice]; // Return selected category
    }

    private static int readInt(String msg) {
        System.out.print(msg); // Prompt
        return Integer.parseInt(scanner.nextLine()); // Read and convert to int
    }

    private static double readDouble(String msg) {
        System.out.print(msg); // Prompt
        return Double.parseDouble(scanner.nextLine()); // Read and convert to double
    }
}
