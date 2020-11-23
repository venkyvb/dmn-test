package com.github.venkyvb;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.dmn.HitPolicy;
import org.camunda.bpm.model.dmn.instance.Decision;
import org.camunda.bpm.model.dmn.instance.DecisionTable;
import org.camunda.bpm.model.dmn.instance.Definitions;
import org.camunda.bpm.model.dmn.instance.Input;
import org.camunda.bpm.model.dmn.instance.InputEntry;
import org.camunda.bpm.model.dmn.instance.InputExpression;
import org.camunda.bpm.model.dmn.instance.Output;
import org.camunda.bpm.model.dmn.instance.OutputEntry;
import org.camunda.bpm.model.dmn.instance.Rule;
import org.camunda.bpm.model.dmn.instance.Text;

// Brute-force sample code :)
public class TestHandler {
    
    public static void transformTest() {
        DmnModelInstance modelInstance = Dmn.createEmptyModel();
        Definitions definitions = modelInstance.newInstance(Definitions.class);
        definitions.setNamespace("http://camunda.org/schema/1.0/dmn");
        definitions.setName("definitions");
        definitions.setId("definitions");
        modelInstance.setDefinitions(definitions);

        Decision decision = modelInstance.newInstance(Decision.class);
        decision.setId("testGenerated");
        decision.setName("generationtest");
        definitions.addChildElement(decision);

        DecisionTable decisionTable = modelInstance.newInstance(DecisionTable.class);
        decisionTable.setId("decisionTable");
        decisionTable.setHitPolicy(HitPolicy.UNIQUE);
        decision.addChildElement(decisionTable);

        Input dtypeInput = modelInstance.newInstance(Input.class);
        dtypeInput.setLabel("Type");
        InputExpression inputExpressionDtype = modelInstance.newInstance(InputExpression.class);
        inputExpressionDtype.setTypeRef("string");
        Text textDtype = modelInstance.newInstance(Text.class);
        textDtype.setTextContent("urn_type");
        inputExpressionDtype.setText(textDtype);
        dtypeInput.addChildElement(inputExpressionDtype);
        decisionTable.addChildElement(dtypeInput);

        Input familyInput = modelInstance.newInstance(Input.class);
        familyInput.setLabel("Family");
        InputExpression inputExpressionFamily = modelInstance.newInstance(InputExpression.class);
        inputExpressionFamily.setTypeRef("string");
        Text textFamily = modelInstance.newInstance(Text.class);
        textFamily.setTextContent("urn_family");
        inputExpressionFamily.setText(textFamily);
        familyInput.addChildElement(inputExpressionFamily);
        decisionTable.addChildElement(familyInput);

        Input dTermInput = modelInstance.newInstance(Input.class);
        dTermInput.setLabel("Term");
        InputExpression inputExpressionDterm = modelInstance.newInstance(InputExpression.class);
        inputExpressionDterm.setTypeRef("integer");
        Text textDterm = modelInstance.newInstance(Text.class);
        textDterm.setTextContent("urn_term");
        inputExpressionDterm.setText(textDterm);
        dTermInput.addChildElement(inputExpressionDterm);
        decisionTable.addChildElement(dTermInput);

        Output output = modelInstance.newInstance(Output.class);
        output.setLabel("Discount");
        output.setName("discount");
        output.setTypeRef("double");
        decisionTable.addChildElement(output);

        // LNPRF rule
        Rule ruleLnprf = modelInstance.newInstance(Rule.class);
        Text textLnprf = modelInstance.newInstance(Text.class);
        textLnprf.setTextContent("\"LNPRF\"");
        InputEntry inputEntryLnprfDtype = modelInstance.newInstance(InputEntry.class);
        inputEntryLnprfDtype.addChildElement(textLnprf);
        ruleLnprf.addChildElement(inputEntryLnprfDtype);

        Text textLnprfFamily = modelInstance.newInstance(Text.class);
        textLnprfFamily.setTextContent("");
        InputEntry inputEntryLnprfFamily = modelInstance.newInstance(InputEntry.class);
        inputEntryLnprfFamily.addChildElement(textLnprfFamily);
        ruleLnprf.addChildElement(inputEntryLnprfFamily);

        Text textLnprfDterm = modelInstance.newInstance(Text.class);
        textLnprfDterm.setTextContent("");
        InputEntry inputEntryLnprfDterm = modelInstance.newInstance(InputEntry.class);
        inputEntryLnprfDterm.addChildElement(textLnprfDterm);
        ruleLnprf.addChildElement(inputEntryLnprfDterm);

        OutputEntry outputEntry = modelInstance.newInstance(OutputEntry.class);
        Text textLnprfDiscount = modelInstance.newInstance(Text.class);
        textLnprfDiscount.setTextContent("50");
        outputEntry.addChildElement(textLnprfDiscount);
        ruleLnprf.addChildElement(outputEntry);

        decisionTable.addChildElement(ruleLnprf);

        // Rule LISS1
        Rule ruleLiss1 = modelInstance.newInstance(Rule.class);
        Text textLissDtype = modelInstance.newInstance(Text.class);
        textLissDtype.setTextContent("");
        InputEntry inputEntryLissDtype = modelInstance.newInstance(InputEntry.class);
        inputEntryLissDtype.addChildElement(textLissDtype);
        ruleLiss1.addChildElement(inputEntryLissDtype);

        Text ruleLissFamily = modelInstance.newInstance(Text.class);
        ruleLissFamily.setTextContent("\"LISS\"");
        InputEntry inputEntryLissFamily = modelInstance.newInstance(InputEntry.class);
        inputEntryLissFamily.addChildElement(ruleLissFamily);
        ruleLiss1.addChildElement(inputEntryLissFamily);

        Text ruleLissDterm = modelInstance.newInstance(Text.class);
        ruleLissDterm.setTextContent(">35");
        InputEntry inputEntryLissDterm1 = modelInstance.newInstance(InputEntry.class);
        inputEntryLissDterm1.addChildElement(ruleLissDterm);
        ruleLiss1.addChildElement(inputEntryLissDterm1);

        OutputEntry outputEntryLissDterm1 = modelInstance.newInstance(OutputEntry.class);
        Text textOutputLissDterm1 = modelInstance.newInstance(Text.class);
        textOutputLissDterm1.setTextContent("7.5");
        outputEntryLissDterm1.addChildElement(textOutputLissDterm1);
        ruleLiss1.addChildElement(outputEntryLissDterm1);

        decisionTable.addChildElement(ruleLiss1);

        // Rule LISS2
        Rule ruleLiss2 = modelInstance.newInstance(Rule.class);
        Text textLissDtype2 = modelInstance.newInstance(Text.class);
        textLissDtype2.setTextContent("");
        InputEntry inputEntryLissDtype2 = modelInstance.newInstance(InputEntry.class);
        inputEntryLissDtype2.addChildElement(textLissDtype2);
        ruleLiss2.addChildElement(inputEntryLissDtype2);

        Text ruleLissFamily2 = modelInstance.newInstance(Text.class);
        ruleLissFamily2.setTextContent("\"LISS\"");
        InputEntry inputEntryLissFamily2 = modelInstance.newInstance(InputEntry.class);
        inputEntryLissFamily2.addChildElement(ruleLissFamily2);
        ruleLiss2.addChildElement(inputEntryLissFamily2);

        Text ruleLissDterm2 = modelInstance.newInstance(Text.class);
        ruleLissDterm2.setTextContent("(23..35]");
        InputEntry inputEntryLissDterm2 = modelInstance.newInstance(InputEntry.class);
        inputEntryLissDterm2.addChildElement(ruleLissDterm2);
        ruleLiss2.addChildElement(inputEntryLissDterm2);

        OutputEntry outputEntryLissDterm2 = modelInstance.newInstance(OutputEntry.class);
        Text textOutputLissDterm2 = modelInstance.newInstance(Text.class);
        textOutputLissDterm2.setTextContent("5");
        outputEntryLissDterm2.addChildElement(textOutputLissDterm2);
        ruleLiss2.addChildElement(outputEntryLissDterm2);

        decisionTable.addChildElement(ruleLiss2);

        // Rule LISS3
        Rule ruleLiss3 = modelInstance.newInstance(Rule.class);
        Text textLissDtype3 = modelInstance.newInstance(Text.class);
        textLissDtype3.setTextContent("");
        InputEntry inputEntryLissDtype3 = modelInstance.newInstance(InputEntry.class);
        inputEntryLissDtype3.addChildElement(textLissDtype3);
        ruleLiss3.addChildElement(inputEntryLissDtype3);

        Text ruleLissFamily3 = modelInstance.newInstance(Text.class);
        ruleLissFamily3.setTextContent("\"LISS\"");
        InputEntry inputEntryLissFamily3 = modelInstance.newInstance(InputEntry.class);
        inputEntryLissFamily3.addChildElement(ruleLissFamily3);
        ruleLiss3.addChildElement(inputEntryLissFamily3);

        Text ruleLissDterm3 = modelInstance.newInstance(Text.class);
        ruleLissDterm3.setTextContent("<=23");
        InputEntry inputEntryLissDterm3 = modelInstance.newInstance(InputEntry.class);
        inputEntryLissDterm3.addChildElement(ruleLissDterm3);
        ruleLiss3.addChildElement(inputEntryLissDterm3);

        OutputEntry outputEntryLissDterm3 = modelInstance.newInstance(OutputEntry.class);
        Text textOutputLissDterm3 = modelInstance.newInstance(Text.class);
        textOutputLissDterm3.setTextContent("3");
        outputEntryLissDterm3.addChildElement(textOutputLissDterm3);
        ruleLiss3.addChildElement(outputEntryLissDterm3);

        decisionTable.addChildElement(ruleLiss3);

        Dmn.validateModel(modelInstance);

        System.out.println(Dmn.convertToString(modelInstance));

        DmnEngine dmnEngine = DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();

        DmnDecision decision2 = dmnEngine.parseDecision("testGenerated", modelInstance);

        VariableMap variables1 = Variables.createVariables().putValue("urn_type", "LNPRF")
                .putValue("urn_family", "LITS").putValue("urn_term", 12);
        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(decision2, variables1);

        System.out.println(result.toString());

        VariableMap variables2 = Variables.createVariables().putValue("urn_type", "LISS")
                .putValue("urn_family", "LISS").putValue("urn_term", 12);
        DmnDecisionTableResult result2 = dmnEngine.evaluateDecisionTable(decision2, variables2);

        System.out.println(result2.toString());

        VariableMap variables3 = Variables.createVariables().putValue("urn_type", "LISS")
                .putValue("urn_family", "ABC").putValue("urn_term", 12);
        DmnDecisionTableResult result3 = dmnEngine.evaluateDecisionTable(decision2, variables3);

        System.out.println(result3.toString());

        VariableMap variables4 = Variables.createVariables().putValue("urn_type", "LISS")
                .putValue("urn_family", "LISS").putValue("urn_term", 26);
        DmnDecisionTableResult result4 = dmnEngine.evaluateDecisionTable(decision2, variables4);

        System.out.println(result4.toString());

        VariableMap variables5 = Variables.createVariables().putValue("urn_type", "LISS")
                .putValue("urn_family", "LISS").putValue("urn_term", 36);
        DmnDecisionTableResult result5 = dmnEngine.evaluateDecisionTable(decision2, variables5);

        System.out.println(result5.toString());

    }    
}
