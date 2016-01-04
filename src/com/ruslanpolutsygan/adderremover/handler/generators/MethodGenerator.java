package com.ruslanpolutsygan.adderremover.handler.generators;

import com.jetbrains.php.lang.psi.elements.Field;
import com.ruslanpolutsygan.adderremover.handler.PhpMethodData;

public abstract class MethodGenerator {
    public abstract PhpMethodData[] generate(Field field);
}
