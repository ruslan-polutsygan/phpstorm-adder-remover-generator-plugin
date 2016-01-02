package com.ruslanpolutsygan.adderremover.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.ruslanpolutsygan.adderremover.handler.ActionHandler;
import com.ruslanpolutsygan.adderremover.handler.checkers.AdderChecker;
import com.ruslanpolutsygan.adderremover.handler.generators.AdderGenerator;
import org.jetbrains.annotations.NotNull;


public class PhpGenerateAddersAction extends CodeInsightAction {

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return new ActionHandler(
                new AdderGenerator(),
                new AdderChecker()
        );
    }
}
