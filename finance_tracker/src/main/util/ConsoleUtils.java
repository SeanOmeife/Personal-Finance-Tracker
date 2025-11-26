//  dont forget to Import class file

import java.util.Scanner;

/**
 * tiny console helper to centralize console IO
 */
public final class ConsoleUtils {
    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleUtils() {}

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    public static void println(Object o) {
        System.out.println(o);
    }
}
