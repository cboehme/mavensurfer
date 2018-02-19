/*
 * Copyright 2018 Christoph Böhme
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dnb.tools.svnfairy.browser.model;

import java.util.function.Consumer;
import java.util.function.Function;

public class Outcome<R, E> {

    private final R returnValue;
    private final E error;

    private Outcome(R returnValue, E error) {

        assert returnValue == null || error == null;

        this.returnValue = returnValue;
        this.error = error;
    }

    public static <E> Outcome<Void, E> success() {

        return success(null);
    }

    public static <R, E> Outcome<R, E> success(R returnValue) {

        return new Outcome<>(returnValue, null);
    }

    public static <R, E> Outcome<R, E> error(E error) {

        return new Outcome<>(null, error);
    }

    public boolean succeeded() {
        return error == null;
    }

    public boolean failed() {
        return !succeeded();
    }

    public R get() {

        if (failed()) {
            throw new IllegalStateException(
                    "Cannot get return value of failed outcome");
        }
        return returnValue;
    }

    public R orElse(R other) {

        if (failed()) {
            return other;
        }
        return returnValue;
    }

    public <X extends Throwable> Outcome<R, E> onErrorThrow(Function<E, X> exceptionProducer)
            throws X {

        if (failed()) {
            throw exceptionProducer.apply(error);
        }
        return this;
    }

    public Outcome<R, E> onSuccess(Consumer<R> handler) {

        if (succeeded()) {
            handler.accept(returnValue);
        }
        return this;
    }

    public <S, F> Outcome<S, F> mapOutcome(Function<R, S> returnValueMapper,
                                           Function<E, F> errorMapper) {

        if (succeeded()) {
            return Outcome.success(returnValueMapper.apply(returnValue));
        }
        return Outcome.error(errorMapper.apply(error));
    }

}
