package com.ruslanpolutsygan.adderremover.handler.generators;

import com.jetbrains.php.lang.psi.elements.Field;
import com.ruslanpolutsygan.adderremover.Util;
import com.ruslanpolutsygan.adderremover.ft.AdderRemoverTemplateDescriptorFactory;

public class RemoverGenerator extends TemplateBasedMethodGenerator {
    @Override
    protected String getMethodName(Field field) {
        return Util.createRemoverName(field.getName());
    }

    @Override
    protected String getMethodTemplateName() {
        return AdderRemoverTemplateDescriptorFactory.REMOVER_TEMPLATE_NAME;
    }
}
