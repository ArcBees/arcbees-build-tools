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

public enum Final {
    FINAL,
    NOT_FINAL,
    ANY,
    NOT_SUPPORTED;

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
                || (this == ANY && other != NOT_SUPPORTED)
                || (other == ANY && this != NOT_SUPPORTED);
    }
}
