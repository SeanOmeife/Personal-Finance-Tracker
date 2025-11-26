package main.java.dev.pennywise.report;
// Import class files


import java.math.BigDecimal;
import java.util.List;

/**
 * Immutable report type - demonstrates custom immutable type and defensive copying.
 */
public final class Report {
    private final int year;
    private final int month;
    private final List<TransactionRecord> items;
    private final BigDecimal total;
    private final String currency;

    public Report(int year, int month, List<TransactionRecord> items, BigDecimal total, String currency) {
        this.year = year;
        this.month = month;
        this.items = List.copyOf(items); // defensive copy
        this.total = total;
        this.currency = currency;
    }

    public int year() { return year; }
    public int month() { return month; }
    public List<TransactionRecord> items() { return List.copyOf(items); } // defensive copy on access
    public BigDecimal total() { return total; }
    public String currency() { return currency; }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Report for ").append(month).append("/").append(year).append("\n");
        sb.append("Total: ").append(total).append(" ").append(currency).append("\n");
        sb.append("Transactions: ").append(items.size()).append("\n");
        return sb.toString();
    }
}
