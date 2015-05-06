/*
 * Copyright 2015 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.checkstyle.checks;

import java.util.Arrays;
import java.util.List;

import com.arcbees.checkstyle.checks.representation.Declaration;

import static com.arcbees.checkstyle.checks.modifiers.DeclarationType.CLASS;
import static com.arcbees.checkstyle.checks.modifiers.DeclarationType.CONSTRUCTOR;
import static com.arcbees.checkstyle.checks.modifiers.DeclarationType.ENUM;
import static com.arcbees.checkstyle.checks.modifiers.DeclarationType.FIELD;
import static com.arcbees.checkstyle.checks.modifiers.DeclarationType.INITIALIZER;
import static com.arcbees.checkstyle.checks.modifiers.DeclarationType.INTERFACE;
import static com.arcbees.checkstyle.checks.modifiers.DeclarationType.METHOD;
import static com.arcbees.checkstyle.checks.modifiers.Final.FINAL;
import static com.arcbees.checkstyle.checks.modifiers.Final.NOT_FINAL;
import static com.arcbees.checkstyle.checks.modifiers.Static.NOT_STATIC;
import static com.arcbees.checkstyle.checks.modifiers.Static.STATIC;
import static com.arcbees.checkstyle.checks.modifiers.Visibility.PACKAGE_PRIVATE;
import static com.arcbees.checkstyle.checks.modifiers.Visibility.PRIVATE;
import static com.arcbees.checkstyle.checks.modifiers.Visibility.PROTECTED;
import static com.arcbees.checkstyle.checks.modifiers.Visibility.PUBLIC;
import static com.arcbees.checkstyle.checks.representation.Declaration.newDeclaration;

public final class Defaults {
    private static final boolean ONE_ERROR_PER_FILE = false;

    private static final List<Declaration> DECLARATION_ORDER = Arrays.asList(
            newDeclaration(INTERFACE).build(),

            newDeclaration(ENUM).build(),

            newDeclaration(CLASS).withStatic(STATIC).build(),
            newDeclaration(CLASS).withStatic(NOT_STATIC).build(),

            newDeclaration(FIELD).withStatic(STATIC).withFinal(FINAL).build(),
            newDeclaration(FIELD).withStatic(STATIC).withFinal(NOT_FINAL).build(),

            newDeclaration(INITIALIZER).withStatic(STATIC).build(),

            newDeclaration(FIELD).withStatic(NOT_STATIC).withFinal(FINAL).build(),
            newDeclaration(FIELD).withStatic(NOT_STATIC).withFinal(NOT_FINAL).build(),

            newDeclaration(INITIALIZER).withStatic(NOT_STATIC).build(),

            newDeclaration(CONSTRUCTOR).withVisibility(PUBLIC).build(),
            newDeclaration(CONSTRUCTOR).withVisibility(PROTECTED).build(),
            newDeclaration(CONSTRUCTOR).withVisibility(PACKAGE_PRIVATE).build(),
            newDeclaration(CONSTRUCTOR).withVisibility(PRIVATE).build(),

            newDeclaration(METHOD).withStatic(STATIC).build(),

            newDeclaration(METHOD).withStatic(NOT_STATIC).build()
    );

    private Defaults() {
    }

    public static boolean getDefaultOneErrorPerFile() {
        return ONE_ERROR_PER_FILE;
    }

    public static List<Declaration> getDefaultDeclarationOrder() {
        return DECLARATION_ORDER;
    }
}
