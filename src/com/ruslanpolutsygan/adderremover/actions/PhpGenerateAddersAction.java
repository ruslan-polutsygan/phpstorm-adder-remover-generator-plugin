package com.ruslanpolutsygan.adderremover.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.ruslanpolutsygan.adderremover.handler.ActionHandler;
import com.ruslanpolutsygan.adderremover.handler.checkers.AdderChecker;
import com.ruslanpolutsygan.adderremover.handler.generators.AdderGenerator;
import org.jetbrains.annotations.NotNull;


public class PhpGenerateAddersAction extends CodeInsightAction {
    private final ActionHandler handler;

    public PhpGenerateAddersAction() {
        this.handler = new ActionHandler(
                new AdderGenerator(),
                new AdderChecker()
        );
    }

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return this.handler;
    }

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        return this.handler.isValidFor(editor, file);
    }
}
