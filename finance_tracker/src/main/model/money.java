import java.math.BigDecimal;
import java.util.Objects;

/**
 * Immutable custom money type demonstrating:
 * - custom immutable type
 * - defensive copying (though BigDecimal is immutable)
 * - call-by-value concepts
 */

 public final class money {
    private final BigDecimal amount;
    private final String currency;

    public money(BigDecimal amount, String currency){
        this.amount = amount == null ? BigDecimal.ZERO :amount;
        this.currency = currency == null ? "GBP" : currency;
    }
    
    public money(String amount, String currency){
        this(new BigDecimal(amount), currency);
    }

    public BigDecimal amount() {
        return amount; // BigDecimal is immutable
    }

    public String currency() {
        return currency;
    }

    public money add(money other) {
        if (!Objects.equals(currency, other.currency())){
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new money(this.amount.add(other.amount()), currency);
    }

    @Override
    public String toString() {
        return amount.toString() + " " + currency;
    }

 }