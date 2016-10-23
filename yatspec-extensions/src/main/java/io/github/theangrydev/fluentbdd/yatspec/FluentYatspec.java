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
package io.github.theangrydev.fluentbdd.yatspec;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import io.github.theangrydev.fluentbdd.core.FluentBdd;
import io.github.theangrydev.fluentbdd.core.When;

/**
 * Use this as the base class for your acceptance tests.
 *
 * @param <TestResult> The type of test result produced by the {@link When}
 */
public class FluentYatspec<TestResult> extends FluentBdd<TestResult> implements FluentYatspecCommands {

    private final TestState state = new TestState();

    @Override
    public TestState testState() {
        return state;
    }
}
