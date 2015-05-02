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

import java.util.Objects;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Other modifiers: static, native, volatile, synchronized, transient, default
 *
 */
public class Declaration {
    private final Scope scope;
    private final Static isStatic;
    private final Final isFinal;
    private final DeclarationType type;

    public Declaration(
            Scope scope,
            DeclarationType type) {
        this(scope, Static.ANY, Final.ANY, type);
    }

    public Declaration(
            Scope scope,
            Static isStatic,
            DeclarationType type) {
        this(scope, isStatic, Final.ANY, type);
    }

    public Declaration(
            Scope scope,
            Static isStatic,
            Final isFinal,
            DeclarationType type) {
        this.scope = type.hasScopeRestriction() ? type.getScopeRestriction() : scope;
        this.isStatic = type.hasStaticRestriction() ? type.getStaticRestriction() : isStatic;
        this.isFinal = type.hasFinalRestriction() ? type.getFinalRestriction() : isFinal;
        this.type = type;
    }

    public static Declaration fromAst(DetailAST ast) {
        Scope scope = Scope.PACKAGE_PRIVATE;
        Static isStatic = Static.NOT_STATIC;
        Final isFinal = Final.NOT_FINAL;
        DeclarationType type = DeclarationType.fromAst(ast);

        DetailAST modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
        if (modifiers != null) {
            scope = Scope.fromModifiers(modifiers);
            isStatic = Static.fromModifiers(modifiers);
            isFinal = Final.fromModifiers(modifiers);
        }

        return new Declaration(scope, isStatic, isFinal, type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scope, isStatic, isFinal, type);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Declaration other = (Declaration) object;
        return scope.matches(other.scope)
                && isStatic.matches(other.isStatic)
                && isFinal.matches(other.isFinal)
                && type == other.type;
    }

    @Override
    public String toString() {
        return scope +
                (isStatic == Static.STATIC ? " " + isStatic : "") +
                (isFinal == Final.FINAL ? " " + isFinal : "") +
                " " + type;
    }
}
