package com.arcbees.checkstyle.checks.utils;

import java.util.Objects;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

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
