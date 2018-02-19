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

import java.util.function.Function;

public class ImportResult {

    private final Throwable cause;
    private final String message;

    public ImportResult(Throwable cause, String message) {
        this.cause = cause;
        this.message = message;
    }

    public static ImportResult noErrors() {

        return new ImportResult(null, "");
    }

    public boolean succeeded() {
        return cause == null;
    }

    public boolean failed() {
        return !succeeded();
    }

    public <T extends Throwable> ImportResult onErrorThrow(Function<ImportResult, T> exceptionProducer)
            throws T {

        if (failed()) {
            throw exceptionProducer.apply(this);
        }
        return this;
    }

    public String getMessage() {
        return message;
    }

}
