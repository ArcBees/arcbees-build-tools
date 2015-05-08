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

package com.arcbees.checkstyle.checks.modifiers;

import java.util.Collections;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import static java.util.Arrays.asList;

import static com.puppycrawl.tools.checkstyle.api.TokenTypes.CLASS_DEF;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.CTOR_DEF;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.ENUM_DEF;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.INSTANCE_INIT;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.INTERFACE_DEF;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.METHOD_DEF;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.STATIC_INIT;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.VARIABLE_DEF;

public enum DeclarationType {
    INTERFACE(INTERFACE_DEF, Visibility.ANY, Static.NOT_SUPPORTED, Final.NOT_SUPPORTED),
    CLASS(CLASS_DEF, Visibility.ANY, Static.ANY, Final.ANY),
    ENUM(ENUM_DEF, Visibility.ANY, Static.NOT_SUPPORTED, Final.NOT_SUPPORTED),
    FIELD(VARIABLE_DEF, Visibility.ANY, Static.ANY, Final.ANY),
    CONSTRUCTOR(CTOR_DEF, Visibility.ANY, Static.NOT_SUPPORTED, Final.NOT_SUPPORTED),
    METHOD(METHOD_DEF, Visibility.ANY, Static.ANY, Final.ANY),
    INITIALIZER(asList(INSTANCE_INIT, STATIC_INIT), Visibility.NOT_SUPPORTED, Static.ANY, Final.NOT_SUPPORTED);

    private final List<Integer> tokenTypes;
    private final Visibility visibilityRestriction;
    private final Static staticRestriction;
    private final Final finalRestriction;

    DeclarationType(
            int tokenType,
            Visibility visibilityRestriction,
            Static staticRestriction,
            Final isFinal) {
        this(asList(tokenType), visibilityRestriction, staticRestriction, isFinal);
    }

    DeclarationType(
            List<Integer> tokenTypes,
            Visibility visibilityRestriction,
            Static staticRestriction,
            Final finalRestriction) {
        this.tokenTypes = Collections.unmodifiableList(tokenTypes);
        this.visibilityRestriction = visibilityRestriction;
        this.staticRestriction = staticRestriction;
        this.finalRestriction = finalRestriction;
    }

    public static DeclarationType fromAst(DetailAST ast) {
        int type = ast.getType();
        for (DeclarationType declarationType : DeclarationType.values()) {
            if (declarationType.getTokenTypes().contains(type)) {
                return declarationType;
            }
        }

        // Should not happen unless someone overrides this class and uses invalid token types.
        throw new IllegalArgumentException("Unable to convert token type " + type + ".");
    }

    public List<Integer> getTokenTypes() {
        return tokenTypes;
    }

    public boolean hasScopeRestriction() {
        return visibilityRestriction != Visibility.ANY;
    }

    public boolean hasStaticRestriction() {
        return staticRestriction != Static.ANY;
    }

    public boolean hasFinalRestriction() {
        return finalRestriction != Final.ANY;
    }

    public Visibility getVisibilityRestriction() {
        return visibilityRestriction;
    }

    public Static getStaticRestriction() {
        return staticRestriction;
    }

    public Final getFinalRestriction() {
        return finalRestriction;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
