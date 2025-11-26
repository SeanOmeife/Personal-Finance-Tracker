public final class ExpenseTransaction extends BaseTransaction{
    public ExpenseTransaction(Money amount,Category category) {
        this("AUTO", "Expense", amount, category, java.time.LocalDate.now()); // demonstrates this()
    }

    public ExpenseTransaction(String id, String description, Money amount, Category category, java.time.LocalDate date){
        super(id, description, amount, category, date); // super
        if (amount.amount().signum() > 0){
            // want expenses as positive numbers for storage, but we could enforce sign here.
        }
    }

    @Override
    public String summary() {
        return "EXPENSE: " + description() + " | " + category() + " | " + date();
    }
}
