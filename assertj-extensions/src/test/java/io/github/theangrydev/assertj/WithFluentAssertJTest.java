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
package io.github.theangrydev.assertj;

import io.github.theangrydev.fluentbdd.assertj.WithFluentAssertJ;
import io.github.theangrydev.fluentbdd.core.FluentBdd;
import io.github.theangrydev.fluentbdd.core.Given;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class WithFluentAssertJTest implements WithFluentAssertJ<WithFluentAssertJTest> {

    private interface SomeDependency extends Given {}
    private final SomeDependency someDependency = mock(SomeDependency.class);

    @Rule
    public FluentBdd<WithFluentAssertJTest> fluentBdd = new FluentBdd<>(this);

    @Override
    public FluentBdd<WithFluentAssertJTest> fluentBdd() {
        return fluentBdd;
    }

    private int intResult;

    @Test
    public void whenCallingCanSetIntResult() {
        given(someDependency);
        whenCalling(() -> intResult = 42);
        then(theResult().intResult).isLessThan(43);
    }
}