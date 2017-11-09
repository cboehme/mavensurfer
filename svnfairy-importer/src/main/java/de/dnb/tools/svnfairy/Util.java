package de.dnb.tools.svnfairy;

import java.util.function.BiFunction;

public final class Util {

    private Util() {
        throw new AssertionError("No instances allowed");
    }

    public static <T> boolean equals(T object1,
                                     Object object2,
                                     BiFunction<T, T, Boolean> equalsMethod) {

        if (object1 == object2) {
            return true;
        }
        if (object1 == null || !object1.getClass().isInstance(object2)) {
            return false;
        }
        return equalsMethod.apply(object1, (T) object2);
    }

}
