package com.arcbees.checkstyle.checks.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public enum DeclarationType {
    INTERFACE(TokenTypes.INTERFACE_DEF, Scope.ANY, Static.NOT_APPLICABLE, Final.NOT_APPLICABLE),
    CLASS(TokenTypes.CLASS_DEF, Scope.ANY, Static.ANY, Final.ANY),
    ENUM(TokenTypes.ENUM_DEF, Scope.ANY, Static.NOT_APPLICABLE, Final.NOT_APPLICABLE),
    FIELD(TokenTypes.VARIABLE_DEF, Scope.ANY, Static.ANY, Final.ANY),
    CONSTRUCTOR(TokenTypes.CTOR_DEF, Scope.ANY, Static.NOT_APPLICABLE, Final.NOT_APPLICABLE),
    METHOD(TokenTypes.METHOD_DEF, Scope.ANY, Static.ANY, Final.ANY),
    INITIALIZER(TokenTypes.INSTANCE_INIT, Scope.NOT_APPLICABLE, Static.NOT_STATIC, Final.NOT_APPLICABLE),
    STATIC_INITIALIZER(TokenTypes.INSTANCE_INIT, Scope.NOT_APPLICABLE, Static.STATIC, Final.NOT_APPLICABLE),
    NOTHING(TokenTypes.WILDCARD_TYPE, Scope.NOT_APPLICABLE, Static.NOT_APPLICABLE, Final.NOT_APPLICABLE);

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
        for (DeclarationType declarationType : DeclarationType.values()) {
            if (ast.getType() == declarationType.tokenType) {
                return declarationType;
            }
        }

        return NOTHING;
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
