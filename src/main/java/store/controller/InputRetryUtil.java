package store.controller;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import store.view.ConsoleInput;
import store.view.ConsoleOutput;

public class InputRetryUtil {
    final private ConsoleInput consoleInput;
    final private ConsoleOutput consoleOutput;

    // constructor for dependency injection

    public InputRetryUtil(ConsoleInput consoleInput, ConsoleOutput consoleOutput) {
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
    }

    // public methods

    // private methods

    private <T> T readValidatedInput(Supplier<String> inputSupplier, Function<String, T> parser,
                                     Consumer<T> validator) {
        while (true) {
            try {
                String userInput = inputSupplier.get();
                T parsedInput = parser.apply(userInput);
                validator.accept(parsedInput);
                return parsedInput;
            } catch (IllegalArgumentException error) {
                consoleOutput.printException(error);
            }
        }
    }
}
