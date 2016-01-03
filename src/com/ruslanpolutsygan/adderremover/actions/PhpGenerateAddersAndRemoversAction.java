package com.ruslanpolutsygan.adderremover.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.ruslanpolutsygan.adderremover.handler.ActionHandler;
import com.ruslanpolutsygan.adderremover.handler.checkers.AdderChecker;
import com.ruslanpolutsygan.adderremover.handler.checkers.ChainedChecker;
import com.ruslanpolutsygan.adderremover.handler.checkers.MethodChecker;
import com.ruslanpolutsygan.adderremover.handler.checkers.RemoverChecker;
import com.ruslanpolutsygan.adderremover.handler.generators.AdderGenerator;
import com.ruslanpolutsygan.adderremover.handler.generators.ChainedGenerator;
import com.ruslanpolutsygan.adderremover.handler.generators.MethodGenerator;
import com.ruslanpolutsygan.adderremover.handler.generators.RemoverGenerator;
import org.jetbrains.annotations.NotNull;


public class PhpGenerateAddersAndRemoversAction extends CodeInsightAction {

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        MethodGenerator generator = new ChainedGenerator(new MethodGenerator[] {
                new AdderGenerator(),
                new RemoverGenerator()
        });
        MethodChecker checker = new ChainedChecker(new MethodChecker[] {
                new AdderChecker(),
                new RemoverChecker()
        });

        return new ActionHandler(generator, checker);
    }
}
