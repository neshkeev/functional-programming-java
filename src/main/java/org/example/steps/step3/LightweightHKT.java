package org.example.steps.step3;

import org.example.kind.App;
import org.example.kind.OptionalK;

public class LightweightHKT {
    public static void main(String[] args) {
        final int originalValue = 42;

        App<OptionalK.mu, Integer> appOpt = OptionalK.some(originalValue);
        OptionalK<Integer> optK = OptionalK.narrow(appOpt);

        final int value = optK.getDelegate().caseOf(
                () -> 0,
                e -> e
        );

        assert value == originalValue : "Original value changed";
    }
}
