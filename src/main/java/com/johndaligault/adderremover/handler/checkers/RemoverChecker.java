package com.johndaligault.adderremover.handler.checkers;

import com.jetbrains.php.lang.psi.elements.Field;
import com.johndaligault.adderremover.Util;

public class RemoverChecker extends MethodChecker {
    @Override
    public boolean hasMethod(Field field) {
        return this.hasMethod(
                Util.createRemoverName(field.getName()), field.getContainingClass()
        );
    }
}
