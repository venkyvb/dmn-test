package com.github.venkyvb;

import java.util.*;

import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.HitPolicy;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        testRuleSet1();
    }


    private static String setUpRuleSet1(String ruleSetId) {

        // 
        List<DecisionTableInput> decisionTableInputs = new LinkedList<>();
        decisionTableInputs.add(new DecisionTableInput("urn_type", "urn_type", "string", "Type"));
        decisionTableInputs.add(new DecisionTableInput("urn_family", "urn_family", "string", "Family"));
        decisionTableInputs.add(new DecisionTableInput("urn_term", "urn_term", "integer", "Term")); 
        
        List<DecisionTableOutput> decisionTableOutputs = new LinkedList<>();
        decisionTableOutputs.add(new DecisionTableOutput("Discount", "discount", "double"));


        List<RuleEntry> rules = new LinkedList<>();

        List<String> inputValuesLnprf = new LinkedList<>();
        inputValuesLnprf.add("\"LNPRF\"");
        inputValuesLnprf.add("\"LITS\",\"LILS\"");
        inputValuesLnprf.add("");
        rules.add(new RuleEntry(inputValuesLnprf, "50"));

        List<String> inputValuesLiss1 = new LinkedList<>();
        inputValuesLiss1.add("");
        inputValuesLiss1.add("\"LISS\"");
        inputValuesLiss1.add(">35");
        rules.add(new RuleEntry(inputValuesLiss1, "7.5"));

        List<String> inputValuesLiss2 = new LinkedList<>();
        inputValuesLiss2.add("");
        inputValuesLiss2.add("\"LISS\"");
        inputValuesLiss2.add("(23..35]");
        rules.add(new RuleEntry(inputValuesLiss2, "5"));      
        
        List<String> inputValuesLiss3 = new LinkedList<>();
        inputValuesLiss3.add("");
        inputValuesLiss3.add("\"LISS\"");
        inputValuesLiss3.add("<=23");
        rules.add(new RuleEntry(inputValuesLiss3, "3"));    

        List<String> inputValuesLiss4 = new LinkedList<>();
        inputValuesLiss4.add("");
        inputValuesLiss4.add("\"LISS\"");
        inputValuesLiss4.add("<=18");
        rules.add(new RuleEntry(inputValuesLiss4, "2"));

        DecisionTableMetadata metadata = new DecisionTableMetadata(ruleSetId, decisionTableInputs, decisionTableOutputs, HitPolicy.UNIQUE);
             
        String dmnModel = DmnModelHandler.transform(metadata, rules);

        return dmnModel;

    }

    private static void testRuleSet1() {

        String ruleSetId = "testRuleSet1";
        String dmnModel = setUpRuleSet1(ruleSetId);

//        System.out.println(dmnModel);

        System.out
                .println(
                        DmnModelHandler
                                .evaluateRules(
                                        ruleSetId, dmnModel,
                                        Variables.createVariables().putValue("urn_type", "LNPRF")
                                                .putValue("urn_family", "LITS").putValue("urn_term", 12))
                                .toString());

        System.out.println(DmnModelHandler
                        .evaluateRules(ruleSetId, dmnModel,
                                        Variables.createVariables().putValue("urn_type", "LNPRF")
                                                        .putValue("urn_family", "LILS").putValue("urn_term", 12))
                        .toString());      
                        
        System.out.println(DmnModelHandler
                        .evaluateRules(ruleSetId, dmnModel,
                                        Variables.createVariables().putValue("urn_type", "LNPRF")
                                                        .putValue("urn_family", "LISS").putValue("urn_term", 12))
                        .toString());                        

        System.out
                .println(
                        DmnModelHandler
                                .evaluateRules(
                                        ruleSetId, dmnModel,
                                        Variables.createVariables().putValue("urn_type", "LISS")
                                                .putValue("urn_family", "LISS").putValue("urn_term", 12))
                                .toString());   
                                
        System.out
                .println(
                        DmnModelHandler
                                .evaluateRules(
                                        ruleSetId, dmnModel,
                                        Variables.createVariables().putValue("urn_type", "LISS")
                                                .putValue("urn_family", "ABC").putValue("urn_term", 12))
                                .toString());  
                                
        System.out
                .println(
                        DmnModelHandler
                                .evaluateRules(
                                        ruleSetId, dmnModel,
                                        Variables.createVariables().putValue("urn_type", "LISS")
                                                .putValue("urn_family", "LISS").putValue("urn_term", 26))
                                .toString());        
                                
        System.out
                .println(
                        DmnModelHandler
                                .evaluateRules(
                                        ruleSetId, dmnModel,
                                        Variables.createVariables().putValue("urn_type", "LISS")
                                                .putValue("urn_family", "LISS").putValue("urn_term", 36))
                                .toString());   
                                
        System.out
                .println(
                        DmnModelHandler
                                .evaluateRules(
                                        ruleSetId, dmnModel,
                                        Variables.createVariables().putValue("urn_type", "ABC")
                                                .putValue("urn_family", "LISS").putValue("urn_term", 36))
                                .toString());                                
    }
}
