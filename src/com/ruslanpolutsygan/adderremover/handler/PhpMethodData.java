package com.ruslanpolutsygan.adderremover.handler;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.Method;

public class PhpMethodData {
    private Method method;
    private PhpDocComment phpDoc;

    public PhpMethodData(Method method, PhpDocComment phpDoc) {
        this.method = method;
        this.phpDoc = phpDoc;
    }

    public Method getMethod() {
        return method;
    }

    public PhpDocComment getPhpDoc() {
        return phpDoc;
    }
}
