package com.johndaligault.adderremover.handler.generators;

import com.jetbrains.php.lang.psi.elements.Field;
import com.johndaligault.adderremover.Util;
import com.johndaligault.adderremover.ft.AdderRemoverTemplateDescriptorFactory;

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
