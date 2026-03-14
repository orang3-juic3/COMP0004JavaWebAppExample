package uk.ac.ucl.model;

import jakarta.annotation.Nonnull;

@FunctionalInterface
public interface StringMatcher {
    boolean isMatch(@Nonnull String source, @Nonnull String target);

    // Strips both strings, and looks case-insensitively if either string contains the other
    StringMatcher DEFAULT_MATCHER = (String source, String target) -> {
        final String source2 = source.strip().toLowerCase();
        final String target2 = target.strip().toLowerCase();
        if (target2.isEmpty() || source2.isEmpty()) {
            return false;
        }
        return target2.contains(source2) || source2.contains(target2);
    };
    StringMatcher EXACT_MATCHER = String::equals;

}