package com.arcbees.checkstyle.checks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.arcbees.checkstyle.checks.utils.Declaration;
import com.arcbees.checkstyle.checks.utils.DeclarationType;
import com.arcbees.checkstyle.checks.utils.Defaults;
import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class DeclarationOrderCheck extends Check {
    private static final String MESSAGE_KEY = "definition.order.general";
    private static final int[] TOKEN_TYPES;

    static {
        DeclarationType[] declarationTypes = DeclarationType.values();
        TOKEN_TYPES = new int[declarationTypes.length];

        for (int i = 0; i < declarationTypes.length; ++i) {
            TOKEN_TYPES[i] = declarationTypes[i].getTokenType();
        }
    }

    private Boolean oneLogPerFile;
    // TODO: Allow configuration by XML
    private List<Declaration> declarationOrder;
    private boolean errorsDetected;

    @SuppressWarnings("unused" /* Accessed by reflection from checkstyle */)
    public void setOneLogPerFile(Boolean oneLogPerFile) {
        this.oneLogPerFile = oneLogPerFile;
    }

    @Override
    public void init() {
        if (oneLogPerFile == null) {
            oneLogPerFile = Defaults.getDefaultOneErrorPerFile();
        }
        if (declarationOrder == null) {
            declarationOrder = Defaults.getDefaultDeclarationOrder();
        } else {
            // Remove duplicates. Wildcards are duplicates of everything.
            // List is required to use indexOf(). Further optimization may help use a set directly.
            declarationOrder = new ArrayList<>(new HashSet<>(declarationOrder));
        }
    }

    @Override
    public int[] getDefaultTokens() {
        return TOKEN_TYPES;
    }

    @Override
    public int[] getRequiredTokens() {
        return TOKEN_TYPES;
    }

    @Override
    public void beginTree(DetailAST rootAst) {
        errorsDetected = false;
    }

    @Override
    public void visitToken(DetailAST ast) {
        DetailAST parent = ast.getParent();
        if ((errorsDetected && oneLogPerFile) || parent == null || parent.getType() != TokenTypes.OBJBLOCK) {
            return;
        }

        // TODO: Optimize: Keep track of what declaration can be used rather than compare the siblings.

        Declaration declaration = Declaration.fromAst(ast);
        int orderIndex = declarationOrder.indexOf(declaration);

        for (DetailAST previousAst = ast.getPreviousSibling();
                previousAst != null;
                previousAst = previousAst.getPreviousSibling()) {
            Declaration previousDeclaration = Declaration.fromAst(previousAst);

            int previousOrderIndex = declarationOrder.indexOf(previousDeclaration);

            if (orderIndex < previousOrderIndex) {
                log(ast, MESSAGE_KEY, previousDeclaration, declaration);
                errorsDetected = true;
                break;
            }
        }
    }
}
