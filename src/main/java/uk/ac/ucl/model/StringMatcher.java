package uk.ac.ucl.model;

import jakarta.annotation.Nonnull;

/**
 * An interface with default instances used in {@link SearchBuilder} to define how strict searching for terms should be
 */
@FunctionalInterface
public interface StringMatcher {
    boolean isMatch(@Nonnull String source, @Nonnull String target);

    // Strips both strings, and looks case-insensitively if the target string contains the source string
    StringMatcher DEFAULT = (String source, String target) -> {
        final String source2 = source.strip().toLowerCase();
        final String target2 = target.strip().toLowerCase();
        if (target2.isEmpty() || source2.isEmpty()) {
            return false;
        }
        return target2.contains(source2);
    };
    StringMatcher EXACT = String::equals;

}