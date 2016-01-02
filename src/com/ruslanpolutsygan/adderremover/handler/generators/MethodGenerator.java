package com.ruslanpolutsygan.adderremover.handler.generators;

import com.jetbrains.php.lang.intentions.generators.PhpAccessorMethodData;
import com.jetbrains.php.lang.psi.elements.Field;

public abstract class MethodGenerator {
    public abstract PhpAccessorMethodData[] generate(Field field);
}
