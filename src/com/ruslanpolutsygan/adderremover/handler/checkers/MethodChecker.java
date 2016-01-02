package com.ruslanpolutsygan.adderremover.handler.checkers;

import com.jetbrains.php.lang.psi.elements.Field;

public abstract class MethodChecker {
    public abstract boolean hasMethod(Field field);
}
