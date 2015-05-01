package com.arcbees.checkstyle.checks.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public enum Scope {
    PUBLIC(TokenTypes.LITERAL_PUBLIC),
    PROTECTED(TokenTypes.LITERAL_PROTECTED),
    PACKAGE_PRIVATE(-1),
    PRIVATE(TokenTypes.LITERAL_PRIVATE),
    ANY(-1),
    NOT_APPLICABLE(-1);

    private final int tokenType;

    Scope(int tokenType) {
        this.tokenType = tokenType;
    }

    public static Scope fromModifiers(DetailAST modifiersAst) {
        assert modifiersAst.getType() == TokenTypes.MODIFIERS;

        for (Scope scope : Scope.values()) {
            if (modifiersAst.findFirstToken(scope.tokenType) != null) {
                return scope;
            }
        }

        return PACKAGE_PRIVATE;
    }

    public boolean matches(Scope other) {
        return this == other
                || (this == ANY && other != NOT_APPLICABLE)
                || (other == ANY && this != NOT_APPLICABLE);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
