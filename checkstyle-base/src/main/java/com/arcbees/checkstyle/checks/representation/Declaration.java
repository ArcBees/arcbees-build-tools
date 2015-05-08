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

package com.arcbees.checkstyle.checks.representation;

import java.util.Objects;

import com.arcbees.checkstyle.checks.modifiers.DeclarationType;
import com.arcbees.checkstyle.checks.modifiers.Final;
import com.arcbees.checkstyle.checks.modifiers.Static;
import com.arcbees.checkstyle.checks.modifiers.Visibility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import static com.arcbees.checkstyle.checks.modifiers.Final.FINAL;
import static com.arcbees.checkstyle.checks.modifiers.Final.NOT_FINAL;
import static com.arcbees.checkstyle.checks.modifiers.Static.NOT_STATIC;
import static com.arcbees.checkstyle.checks.modifiers.Static.STATIC;
import static com.arcbees.checkstyle.checks.modifiers.Visibility.ANY;
import static com.arcbees.checkstyle.checks.modifiers.Visibility.NOT_SUPPORTED;
import static com.arcbees.checkstyle.checks.modifiers.Visibility.PACKAGE_PRIVATE;

/**
 * The following modifiers are not supported: native, volatile, synchronized, transient, default
 */
public class Declaration {
    private final Visibility visibility;
    private final Static isStatic;
    private final Final isFinal;
    private final DeclarationType type;

    Declaration(
            Visibility visibility,
            Static isStatic,
            Final isFinal,
            DeclarationType type) {
        this.visibility = type.hasScopeRestriction() ? type.getVisibilityRestriction() : visibility;
        this.isStatic = type.hasStaticRestriction() ? type.getStaticRestriction() : isStatic;
        this.isFinal = type.hasFinalRestriction() ? type.getFinalRestriction() : isFinal;
        this.type = type;
    }

    public static DeclarationBuilder newDeclaration(DeclarationType type) {
        return new DeclarationBuilder(type);
    }

    public static Declaration fromAst(DetailAST ast) {
        DeclarationBuilder builder = newDeclaration(DeclarationType.fromAst(ast));
        DetailAST modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);

        if (modifiers != null) {
            builder = builder
                    .withVisibility(Visibility.fromModifiers(modifiers))
                    .withStatic(Static.fromModifiers(modifiers))
                    .withFinal(Final.fromModifiers(modifiers));
        } else {
            builder = builder
                    .withVisibility(PACKAGE_PRIVATE)
                    .withStatic(NOT_STATIC)
                    .withFinal(NOT_FINAL);
        }

        return builder.build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(visibility, isStatic, isFinal, type);
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
        return visibility.matches(other.visibility)
                && isStatic.matches(other.isStatic)
                && isFinal.matches(other.isFinal)
                && type == other.type;
    }

    @Override
    public String toString() {
        return (visibility != NOT_SUPPORTED && visibility != ANY ? visibility + " " : "") +
                (isStatic == STATIC ? isStatic + " " : "") +
                (isFinal == FINAL ? isFinal + " " : "") +
                type;
    }
}
