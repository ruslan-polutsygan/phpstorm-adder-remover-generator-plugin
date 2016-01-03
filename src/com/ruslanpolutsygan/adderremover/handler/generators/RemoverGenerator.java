package com.ruslanpolutsygan.adderremover.handler.generators;

import com.jetbrains.php.lang.psi.elements.Field;
import com.ruslanpolutsygan.adderremover.Util;

public class RemoverGenerator extends TemplateBasedMethodGenerator {
    @Override
    protected String getMethodTemplate(Field field) {
        String template = "public function __METHOD_NAME__(__ARGUMENT__)\n{\n";
        if(this.isDoctrineCollectionField(field)) {
            template += "\t\t$this->__FIELD_NAME__->removeElement($var);\n";
        } else {
            template += "\t\t// @todo: add method body there\n";
        }
        template += "}\n";

        return template
                .replace("__METHOD_NAME__", Util.createRemoverName(field.getName()))
                .replace("__FIELD_NAME__", field.getName())
                .replace("__ARGUMENT__", TemplateBasedMethodGenerator.getMethodArgument(field))
                ;

    }

    @Override
    protected String getPhpDocTemplate(Field field) {
        String template = "/**\n @param __TYPE_HINT__ $var\n */";

        return template
                .replace("__TYPE_HINT__", TemplateBasedMethodGenerator.getTypeHint(field))
                ;
    }
}
