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

package com.arcbees.checkstyle.checks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.arcbees.checkstyle.checks.utils.Context;
import com.arcbees.checkstyle.checks.utils.Declaration;
import com.arcbees.checkstyle.checks.utils.DeclarationType;
import com.arcbees.checkstyle.checks.utils.Defaults;
import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class DeclarationOrderCheck extends Check {
    private static final String ORDER_MESSAGE_KEY = "definition.order.wrong";
    private static final String EXCEPTION_MESSAGE_KEY = "definition.order.exception";
    private static final int[] TOKEN_TYPES;

    private static boolean canExecute = true;

    static {
        DeclarationType[] declarationTypes = DeclarationType.values();

        TOKEN_TYPES = new int[declarationTypes.length + 1];
        TOKEN_TYPES[0] = TokenTypes.OBJBLOCK;

        for (int i = 0; i < declarationTypes.length; ++i) {
            TOKEN_TYPES[i + 1] = declarationTypes[i].getTokenType();
        }
    }

    private Boolean oneLogPerFile;
    // TODO: Allow configuration by XML
    private List<Declaration> declarationOrder;
    private Context context;

    @SuppressWarnings("unused" /* Accessed by reflection from checkstyle */)
    public void setOneLogPerFile(Boolean oneLogPerFile) {
        this.oneLogPerFile = oneLogPerFile;
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
    public void init() {
        initLogSetting();
        initDeclarationOrder();
    }

    @Override
    public void beginTree(DetailAST rootAST) {
        if (canExecute) {
            context = new Context(declarationOrder, null);
        }
    }

    @Override
    public void visitToken(DetailAST ast) {
        if (canExecute) {
            try {
                handleAst(ast);
            } catch (RuntimeException e) {
                log(ast, EXCEPTION_MESSAGE_KEY, e);
                e.printStackTrace();
                canExecute = false;
            }
        }
    }

    @Override
    public void leaveToken(DetailAST ast) {
        if (canExecute && ast.getType() == TokenTypes.OBJBLOCK) {
            context = context.close();
        }
    }

    private void initLogSetting() {
        if (oneLogPerFile == null) {
            oneLogPerFile = Defaults.getDefaultOneErrorPerFile();
        }
    }

    private void initDeclarationOrder() {
        if (declarationOrder == null) {
            declarationOrder = Defaults.getDefaultDeclarationOrder();
        } else {
            // Remove duplicates. Wildcards are duplicates of everything.
            // List is required to use indexOf(). Further optimization may help use a set directly.
            declarationOrder = new ArrayList<>(new HashSet<>(declarationOrder));
        }

        declarationOrder = Collections.unmodifiableList(declarationOrder);
    }

    private void handleAst(DetailAST ast) {
        if (isRootAst(ast)) {
            context = new Context(declarationOrder, context);
        } else if (canContinue() && isValidParent(ast)) {
            validateAst(ast);
        }
    }

    private boolean isValidParent(DetailAST ast) {
        DetailAST parent = ast.getParent();
        return parent != null && isRootAst(parent);
    }

    private boolean isRootAst(DetailAST ast) {
        return ast.getType() == TokenTypes.EOF || ast.getType() == TokenTypes.OBJBLOCK;
    }

    private boolean canContinue() {
        return !context.isFailed() || !oneLogPerFile;
    }

    private void validateAst(DetailAST ast) {
        Declaration declaration = Declaration.fromAst(ast);

        if (context.isDeclarationAllowed(declaration)) {
            context.preventDeclarationsBefore(declaration);
        } else {
            log(ast, ORDER_MESSAGE_KEY, declaration);
            context.setFailed(true);
        }
    }
}
