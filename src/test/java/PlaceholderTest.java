import com.hypherionmc.simplerpc.api.variables.PlaceholderEngine;

public class PlaceholderTest {

    public static void main(String[] args) {
        PlaceholderEngine.INSTANCE.registerPlaceholder("world", "World", () -> "Oi");
        PlaceholderEngine.INSTANCE.registerPlaceholder("bye", "Good", () -> "Goodbye");

        String input = "So today we {{ world}}, {{bye}}";

        System.out.println(PlaceholderEngine.INSTANCE.resolvePlaceholders(input));
    }

}
