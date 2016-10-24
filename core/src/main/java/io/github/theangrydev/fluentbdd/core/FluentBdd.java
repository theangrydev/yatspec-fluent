/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of fluent-bdd.
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
package io.github.theangrydev.fluentbdd.core;

import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Use this as a JUnit {@link Rule} alongside {@link WithFluentBdd}.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
public class FluentBdd<TestResult> extends TestWatcher implements FluentBddCommands<TestResult> {

    private final Verification<TestResult> verification = new Verification<>();

    private TestResult testResult;

    @Override
    protected void succeeded(Description description) {
        verification.checkThenHasBeenUsed();
    }

    @Override
    public void and(Given given) {
        given(given);
    }

    /**
     * Prime the given immediately.
     *
     * @param given The first given in the acceptance test, which should be built up inside the brackets
     */
    @Override
    public void given(Given given) {
        verification.recordGiven(given);
        given.prime();
    }

    @Override
    public <T extends When<TestResult>> void when(T when) {
        testResult = verification.recordWhen(when);
    }

    @Override
    public void given(When<TestResult> when) {
        given((Given) when::execute);
    }

    @Override
    public void and(When<TestResult> when) {
        given(when);
    }

    @Override
    public <Then> Then then(ThenAssertion<Then, TestResult> thenAssertion) {
        verification.recordThen(thenAssertion);
        return thenAssertion.then(testResult);
    }

    @Override
    public <Then> Then and(ThenAssertion<Then, TestResult> thenAssertion) {
        return then(thenAssertion);
    }

    @Override
    public void and(ThenVerification<TestResult> thenVerification) {
        then(thenVerification);
    }

    @Override
    public void then(ThenVerification<TestResult> thenVerification) {
        verification.recordThen(thenVerification);
        thenVerification.verify(testResult);
    }
}
