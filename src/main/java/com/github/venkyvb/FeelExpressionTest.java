package com.github.venkyvb;

import java.util.*;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.BuiltinAggregator;
import org.camunda.bpm.model.dmn.HitPolicy;

public class FeelExpressionTest {

  public static void executeTest() {
    String ruleSetId = "testRuleSet3";
    String dmnModel = setupRuleSet(ruleSetId);

    System.out.println(dmnModel);

    List<Map<String, Object>> result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables().putValue("urn_type", Arrays.asList("RC", "LL")))
            .getResultList();

    for (Map<String, Object> entry : result) {
      System.out.println(entry);
    }
  }

  public static String setupRuleSet(String ruleSetId) {
    List<DecisionTableInput> decisionTableInputs = new LinkedList<>();
    decisionTableInputs.add(new DecisionTableInput("urn_type", "urn_type", "string", "Type"));

    List<DecisionTableOutput> decisionTableOutputs = new LinkedList<>();
    decisionTableOutputs.add(new DecisionTableOutput("urn_result", "urn_result", "string"));

    List<RuleEntry> rules = new LinkedList<>();
    List<String> inputValuesB1 = new LinkedList<>();
    inputValuesB1.add("contains(cellInput, \"RC\")");
    rules.add(new RuleEntry(inputValuesB1, "\"RC\""));

    List<String> inputValuesB2 = new LinkedList<>();
    inputValuesB2.add("contains(cellInput, \"JS\")");
    rules.add(new RuleEntry(inputValuesB2, "\"JS\""));

    List<String> inputValuesB3 = new LinkedList<>();
    inputValuesB3.add(
        "(contains(cellInput, \"RC\") or contains(cellInput, \"JS\")) and"
            + " (contains(cellInput, \"LL\") or contains(cellInput, \"LLP\"))");
    rules.add(new RuleEntry(inputValuesB3, "\"RC OR JS AND LL OR LLP\""));

    DecisionTableMetadata metadata =
        new DecisionTableMetadata(
            ruleSetId,
            decisionTableInputs,
            decisionTableOutputs,
            HitPolicy.COLLECT,
            Optional.of(BuiltinAggregator.COUNT));

    String dmnModel = DmnModelHandler.transform(metadata, rules);

    return dmnModel;
  }
}
