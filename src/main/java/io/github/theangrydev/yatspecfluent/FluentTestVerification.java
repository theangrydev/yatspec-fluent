/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of yatspec-fluent.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.theangrydev.yatspecfluent;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;

class FluentTestVerification<TestResult> extends TestWatcher {

    private enum Stage {
        GIVEN,
        WHEN,
        THEN
    }

    private Stage stage = Stage.GIVEN;

    private final List<Given> usedGivens = new ArrayList<>();
    private final List<ThenVerification<TestResult>> usedThenVerifications = new ArrayList<>();
    private final List<ThenAssertion<?, TestResult>> usedThenAssertions = new ArrayList<>();

    public void checkGivenIsAllowed(Given given) {
        if (stage != Stage.GIVEN) {
            throw new IllegalStateException("The 'given' steps must be specified before the 'when' and 'then' steps");
        }
        if (appearsToBeMutable(given.getClass()) && usedGivens.contains(given)) {
            throw new IllegalStateException(format("This '%s' instance has been used once already. To avoid accidentally sharing state, use a new instance.", given.getClass().getSimpleName()));
        }
    }

    public void recordGiven(Given given) {
        stage = Stage.GIVEN;
        usedGivens.add(given);
    }

    public void checkWhenIsAllowed() {
        if (stage != Stage.GIVEN) {
            throw new IllegalStateException("There should only be one 'when', after the 'given' and before the 'then'");
        }
    }

    public void recordWhen(When<TestResult> when, TestResult testResult) {
        if (testResult == null) {
            throw new IllegalStateException(format("'%s' test result was null", when));
        }
        stage = Stage.WHEN;
    }

    public void checkThenVerificationIsAllowed(ThenVerification<TestResult> thenVerification) {
        checkThenIsAllowed();
        if (appearsToBeMutable(thenVerification.getClass()) && usedThenVerifications.contains(thenVerification)) {
            throw new IllegalStateException(format("This '%s' instance has been used once already. To avoid accidentally sharing state, use a new instance.", thenVerification.getClass().getSimpleName()));
        }
    }

    public void recordThenVerification(ThenVerification<TestResult> thenVerification) {
        usedThenVerifications.add(thenVerification);
    }

    public <Then> void checkThenAssertionIsAllowed(ThenAssertion<Then, TestResult> thenAssertion) {
        checkThenIsAllowed();
        if (appearsToBeMutable(thenAssertion.getClass()) && usedThenAssertions.contains(thenAssertion)) {
            throw new IllegalStateException(format("This '%s' instance has been used once already. To avoid accidentally sharing state, use a new instance.", thenAssertion.getClass().getSimpleName()));
        }
        usedThenAssertions.add(thenAssertion);
    }

    @Override
    protected void succeeded(Description description) {
        if (stage != Stage.THEN) {
            throw new IllegalStateException("Each test needs at least a 'when' and a 'then'");
        }
    }

    private void checkThenIsAllowed() {
        if (stage.compareTo(Stage.WHEN) < 0) {
            throw new IllegalStateException("The 'then' steps should be after the 'when'");
        }
        stage = Stage.THEN;
    }

    private boolean appearsToBeMutable(Class<?> aClass) {
        return stream(aClass.getDeclaredFields())
                .mapToInt(Field::getModifiers)
                .anyMatch(modifiers -> !(isStatic(modifiers) && isFinal(modifiers)));
    }
}
