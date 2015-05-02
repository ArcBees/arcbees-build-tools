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

package com.arcbees.checkstyle.checks.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.arcbees.checkstyle.checks.utils.DeclarationType.CLASS;
import static com.arcbees.checkstyle.checks.utils.DeclarationType.CONSTRUCTOR;
import static com.arcbees.checkstyle.checks.utils.DeclarationType.ENUM;
import static com.arcbees.checkstyle.checks.utils.DeclarationType.FIELD;
import static com.arcbees.checkstyle.checks.utils.DeclarationType.INTERFACE;
import static com.arcbees.checkstyle.checks.utils.DeclarationType.METHOD;
import static com.arcbees.checkstyle.checks.utils.Final.FINAL;
import static com.arcbees.checkstyle.checks.utils.Final.NOT_FINAL;
import static com.arcbees.checkstyle.checks.utils.Scope.PACKAGE_PRIVATE;
import static com.arcbees.checkstyle.checks.utils.Scope.PRIVATE;
import static com.arcbees.checkstyle.checks.utils.Scope.PROTECTED;
import static com.arcbees.checkstyle.checks.utils.Scope.PUBLIC;
import static com.arcbees.checkstyle.checks.utils.Static.NOT_STATIC;
import static com.arcbees.checkstyle.checks.utils.Static.STATIC;

public class Defaults {
    private static final boolean ONE_ERROR_PER_FILE = false;

    private static List<Declaration> DECLARATION_ORDER;

    public static boolean getDefaultOneErrorPerFile() {
        return ONE_ERROR_PER_FILE;
    }

    public static List<Declaration> getDefaultDeclarationOrder() {
        if (DECLARATION_ORDER == null) {
            DECLARATION_ORDER = new ArrayList<>(buildDefaultDeclarationOrder());
        }

        return DECLARATION_ORDER;
    }

    private static Set<Declaration> buildDefaultDeclarationOrder() {
        Set<Declaration> order = new LinkedHashSet<>();

        // Static inner classes
        order.add(new Declaration(PUBLIC, STATIC, FINAL, CLASS));
        order.add(new Declaration(PUBLIC, STATIC, NOT_FINAL, CLASS));
        order.add(new Declaration(PROTECTED, STATIC, FINAL, CLASS));
        order.add(new Declaration(PROTECTED, STATIC, NOT_FINAL, CLASS));
        order.add(new Declaration(PACKAGE_PRIVATE, STATIC, FINAL, CLASS));
        order.add(new Declaration(PACKAGE_PRIVATE, STATIC, NOT_FINAL, CLASS));
        order.add(new Declaration(PRIVATE, STATIC, FINAL, CLASS));
        order.add(new Declaration(PRIVATE, STATIC, NOT_FINAL, CLASS));

        // Interfaces
        order.add(new Declaration(PUBLIC, INTERFACE));
        order.add(new Declaration(PROTECTED, INTERFACE));
        order.add(new Declaration(PACKAGE_PRIVATE, INTERFACE));
        order.add(new Declaration(PRIVATE, INTERFACE));

        // Enums
        order.add(new Declaration(PUBLIC, ENUM));
        order.add(new Declaration(PROTECTED, ENUM));
        order.add(new Declaration(PACKAGE_PRIVATE, ENUM));
        order.add(new Declaration(PRIVATE, ENUM));

        // Member inner classes
        order.add(new Declaration(PUBLIC, NOT_STATIC, FINAL, CLASS));
        order.add(new Declaration(PUBLIC, NOT_STATIC, NOT_FINAL, CLASS));
        order.add(new Declaration(PROTECTED, NOT_STATIC, FINAL, CLASS));
        order.add(new Declaration(PROTECTED, NOT_STATIC, NOT_FINAL, CLASS));
        order.add(new Declaration(PACKAGE_PRIVATE, NOT_STATIC, FINAL, CLASS));
        order.add(new Declaration(PACKAGE_PRIVATE, NOT_STATIC, NOT_FINAL, CLASS));
        order.add(new Declaration(PRIVATE, NOT_STATIC, FINAL, CLASS));
        order.add(new Declaration(PRIVATE, NOT_STATIC, NOT_FINAL, CLASS));

        // Class Variables (static fields)
        order.add(new Declaration(PUBLIC, STATIC, FINAL, FIELD));
        order.add(new Declaration(PUBLIC, STATIC, NOT_FINAL, FIELD));
        order.add(new Declaration(PROTECTED, STATIC, FINAL, FIELD));
        order.add(new Declaration(PROTECTED, STATIC, NOT_FINAL, FIELD));
        order.add(new Declaration(PACKAGE_PRIVATE, STATIC, FINAL, FIELD));
        order.add(new Declaration(PACKAGE_PRIVATE, STATIC, NOT_FINAL, FIELD));
        order.add(new Declaration(PRIVATE, STATIC, FINAL, FIELD));
        order.add(new Declaration(PRIVATE, STATIC, NOT_FINAL, FIELD));

        // Fields
        order.add(new Declaration(PUBLIC, NOT_STATIC, FINAL, FIELD));
        order.add(new Declaration(PUBLIC, NOT_STATIC, NOT_FINAL, FIELD));
        order.add(new Declaration(PROTECTED, NOT_STATIC, FINAL, FIELD));
        order.add(new Declaration(PROTECTED, NOT_STATIC, NOT_FINAL, FIELD));
        order.add(new Declaration(PACKAGE_PRIVATE, NOT_STATIC, FINAL, FIELD));
        order.add(new Declaration(PACKAGE_PRIVATE, NOT_STATIC, NOT_FINAL, FIELD));
        order.add(new Declaration(PRIVATE, NOT_STATIC, FINAL, FIELD));
        order.add(new Declaration(PRIVATE, NOT_STATIC, NOT_FINAL, FIELD));

        // Constructors
        order.add(new Declaration(PUBLIC, CONSTRUCTOR));
        order.add(new Declaration(PROTECTED, CONSTRUCTOR));
        order.add(new Declaration(PACKAGE_PRIVATE, CONSTRUCTOR));
        order.add(new Declaration(PRIVATE, CONSTRUCTOR));

        // Static Methods
        order.add(new Declaration(PUBLIC, STATIC, METHOD));
        order.add(new Declaration(PROTECTED, STATIC, METHOD));
        order.add(new Declaration(PACKAGE_PRIVATE, STATIC, METHOD));
        order.add(new Declaration(PRIVATE, STATIC, METHOD));

        // Methods
        order.add(new Declaration(PUBLIC, NOT_STATIC, FINAL, METHOD));
        order.add(new Declaration(PUBLIC, NOT_STATIC, NOT_FINAL, METHOD));
        order.add(new Declaration(PROTECTED, NOT_STATIC, FINAL, METHOD));
        order.add(new Declaration(PROTECTED, NOT_STATIC, NOT_FINAL, METHOD));
        order.add(new Declaration(PACKAGE_PRIVATE, NOT_STATIC, FINAL, METHOD));
        order.add(new Declaration(PACKAGE_PRIVATE, NOT_STATIC, NOT_FINAL, METHOD));
        order.add(new Declaration(PRIVATE, NOT_STATIC, FINAL, METHOD));
        order.add(new Declaration(PRIVATE, NOT_STATIC, NOT_FINAL, METHOD));

        return order;
    }
}
