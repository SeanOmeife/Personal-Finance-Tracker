import java.time.LocalDate;

/*
 * A record for lightweight immutable transaction data.
 * Demonstrates RECORDS and use of Java Core API (LocalDate)
 */

public record TransactionRecord(
        String id,
        String description,
        Money money,
        Category category,
        LocalDate date
) { }
