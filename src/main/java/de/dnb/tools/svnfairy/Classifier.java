package de.dnb.tools.svnfairy;

import java.util.Objects;

public final class Classifier {

    private final String classifier;

    private Classifier(String classifier) {
        this.classifier = classifier;
    }

    public static Classifier of(String classifier) {
        if (classifier == null) {
            return null;
        }
        return new Classifier(classifier);
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.classifier, b.classifier));
    }

    @Override
    public int hashCode() {
        return Objects.hash(classifier);
    }

    @Override
    public String toString() {
        return classifier;
    }

}
