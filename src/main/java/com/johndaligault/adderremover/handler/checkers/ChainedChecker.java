package com.johndaligault.adderremover.handler.checkers;

import com.jetbrains.php.lang.psi.elements.Field;

public class ChainedChecker extends MethodChecker {

    private final MethodChecker[] checkers;

    public ChainedChecker(MethodChecker[] checkers) {
        this.checkers = checkers;
    }

    @Override
    public boolean hasMethod(Field field) {
        for(MethodChecker checker : this.checkers) {
            if(checker.hasMethod(field)) {
                return true;
            }
        }

        return false;
    }
}
