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

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public enum DeclarationType {
    INTERFACE(TokenTypes.INTERFACE_DEF, Scope.ANY, Static.NOT_SUPPORTED, Final.NOT_SUPPORTED),
    CLASS(TokenTypes.CLASS_DEF, Scope.ANY, Static.ANY, Final.ANY),
    ENUM(TokenTypes.ENUM_DEF, Scope.ANY, Static.NOT_SUPPORTED, Final.NOT_SUPPORTED),
    FIELD(TokenTypes.VARIABLE_DEF, Scope.ANY, Static.ANY, Final.ANY),
    CONSTRUCTOR(TokenTypes.CTOR_DEF, Scope.ANY, Static.NOT_SUPPORTED, Final.NOT_SUPPORTED),
    METHOD(TokenTypes.METHOD_DEF, Scope.ANY, Static.ANY, Final.ANY),
    INITIALIZER(TokenTypes.INSTANCE_INIT, Scope.NOT_SUPPORTED, Static.NOT_STATIC, Final.NOT_SUPPORTED),
    STATIC_INITIALIZER(TokenTypes.STATIC_INIT, Scope.NOT_SUPPORTED, Static.STATIC, Final.NOT_SUPPORTED);

    private final int tokenType;
    private final Scope scopeRestriction;
    private final Static staticRestriction;
    private final Final finalRestriction;

    DeclarationType(
            int tokenType,
            Scope scopeRestriction,
            Static staticRestriction,
            Final isFinal) {
        this.tokenType = tokenType;
        this.scopeRestriction = scopeRestriction;
        this.staticRestriction = staticRestriction;
        this.finalRestriction = isFinal;
    }

    public static DeclarationType fromAst(DetailAST ast) {
        int type = ast.getType();
        for (DeclarationType declarationType : DeclarationType.values()) {
            if (type == declarationType.tokenType) {
                return declarationType;
            }
        }

        // Should not happen unless someone overrides this class and uses invalid token types.
        throw new IllegalArgumentException("Unable to convert token type " + type + ".");
    }

    public int getTokenType() {
        return tokenType;
    }

    public boolean hasScopeRestriction() {
        return scopeRestriction != Scope.ANY;
    }

    public boolean hasStaticRestriction() {
        return staticRestriction != Static.ANY;
    }

    public boolean hasFinalRestriction() {
        return finalRestriction != Final.ANY;
    }

    public Scope getScopeRestriction() {
        return scopeRestriction;
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
