package com.arcbees.checkstyle.checks.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public enum Static {
    STATIC,
    NOT_STATIC,
    ANY,
    NOT_APPLICABLE;

    public static Static fromModifiers(DetailAST modifiersAst) {
        assert modifiersAst.getType() == TokenTypes.MODIFIERS;

        return modifiersAst.findFirstToken(TokenTypes.LITERAL_STATIC) != null ? STATIC : NOT_STATIC;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public boolean matches(Static other) {
        return this == other
                || (this == ANY && other != NOT_APPLICABLE)
                || (other == ANY && this != NOT_APPLICABLE);
    }
}
