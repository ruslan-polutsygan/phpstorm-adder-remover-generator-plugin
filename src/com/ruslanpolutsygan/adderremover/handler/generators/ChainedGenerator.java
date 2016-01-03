package com.ruslanpolutsygan.adderremover.handler.generators;

import com.jetbrains.php.lang.intentions.generators.PhpAccessorMethodData;
import com.jetbrains.php.lang.psi.elements.Field;

import java.util.ArrayList;
import java.util.Collections;

public class ChainedGenerator extends MethodGenerator {

    private MethodGenerator[] generators;

    public ChainedGenerator(MethodGenerator[] generators) {
        this.generators = generators;
    }

    @Override
    public PhpAccessorMethodData[] generate(Field field) {
        ArrayList<PhpAccessorMethodData> methods = new ArrayList<>();

        for (MethodGenerator generator : this.generators) {
            Collections.addAll(methods, generator.generate(field));
        }

        return methods.toArray(new PhpAccessorMethodData[methods.size()]);
    }
}
