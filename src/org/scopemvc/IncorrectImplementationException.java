package org.scopemvc;

public class IncorrectImplementationException extends RuntimeException {
    public IncorrectImplementationException(String selector, String model, IllegalArgumentException exception) {
        super(String.format("Implementasjonsfeil i bruk av matrikkel-scope. Selector: %s. Model: %s", selector, model), exception);
    }
}
