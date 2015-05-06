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

import java.util.Collection;
import java.util.LinkedList;

import com.arcbees.checkstyle.checks.representation.Declaration;

public class Context {
    private final Context parent;
    private final LinkedList<Declaration> declarationOrder;

    private boolean failed;

    public Context(
            Collection<Declaration> declarationOrder,
            Context parent) {
        this.parent = parent;
        this.declarationOrder = new LinkedList<>(declarationOrder);

        if (parent != null) {
            failed = parent.isFailed();
        }
    }

    public boolean isDeclarationAllowed(Declaration declaration) {
        return declarationOrder.contains(declaration);
    }

    public void preventDeclarationsBefore(Declaration declaration) {
        assert isDeclarationAllowed(declaration);

        int index = declarationOrder.indexOf(declaration);

        declarationOrder.subList(0, index).clear();
    }

    public Context close() {
        declarationOrder.clear();
        return parent;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        // Once failed, cannot go back.
        this.failed = this.failed || failed;

        if (parent != null) {
            parent.setFailed(this.failed);
        }
    }
}
