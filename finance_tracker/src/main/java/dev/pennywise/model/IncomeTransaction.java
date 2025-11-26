/*
 * Concrete subclass demonstrating:
 * - calling super(...) (super() in constructor)
 * - overriding summary() and using super.field(super.date etc.) conceptially
 */

 public final class IncomeTransaction extends BaseTransaction{
    
    public IncomeTransaction(Money amount, Category category){
        super(amount, category); // calls BaseTransaction(Money, Category)
    }

    public IncomeTransaction(String id, String description, Money amount, Category category, java.time.LocalDate date){
        super(id, description, amount, category, date); // super(...) usage
    }

    @Override
    public String summary() {
        // use of super.date to illustrate 'super.' (accessing inherited members)
        return "INCOME: " + description + " | " + category() + " | " + date();
    }
 }