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

}
