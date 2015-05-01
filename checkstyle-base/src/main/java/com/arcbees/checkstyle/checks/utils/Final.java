package com.arcbees.checkstyle.checks.utils;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public enum Final {
    FINAL,
    NOT_FINAL,
    ANY,
    NOT_APPLICABLE;

    public static Final fromModifiers(DetailAST modifiersAst) {
        assert modifiersAst.getType() == TokenTypes.MODIFIERS;

        return modifiersAst.findFirstToken(TokenTypes.FINAL) != null ? FINAL : NOT_FINAL;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public boolean matches(Final other) {
        return this == other
                || (this == ANY && other != NOT_APPLICABLE)
                || (other == ANY && this != NOT_APPLICABLE);
    }
}
