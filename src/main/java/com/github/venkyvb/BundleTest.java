package com.github.venkyvb;

import java.time.LocalDateTime;
import java.util.*;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.BuiltinAggregator;
import org.camunda.bpm.model.dmn.HitPolicy;

public class BundleTest {

  public static void executeTest() {
    String ruleSetId = "testRuleSet3";
    String dmnModel = setupRuleSet(ruleSetId);

    System.out.println(dmnModel);

    System.out.println(
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("urn_prod", "\"RC\", \"JS\"")
                    .putValue("urn_vol", 3)
                    .putValue("urn_vol", 10)
                    .putValue("urn_level", "")
                    .putValue("urn_validity", "2020-10-01T00:00:00"))
            .toString());
  }

  public static String setupRuleSet(String ruleSetId) {

    List<DecisionTableInput> decisionTableInputs = new LinkedList<>();
    decisionTableInputs.add(new DecisionTableInput("urn_prod", "urn_prod", "string", "Prod"));
    decisionTableInputs.add(new DecisionTableInput("urn_vol", "urn_vol", "integer", "Vol"));
    decisionTableInputs.add(new DecisionTableInput("urn_level", "urn_level", "string", "Level"));
    decisionTableInputs.add(
        new DecisionTableInput("urn_validity", "urn_validity", "date", "Validity"));

    List<DecisionTableOutput> decisionTableOutputs = new LinkedList<>();
    decisionTableOutputs.add(new DecisionTableOutput("Weight", "urn_weight", "integer"));

    List<RuleEntry> rules = new LinkedList<>();

    List<String> inputValuesB1 = new LinkedList<>();
    inputValuesB1.add("\"RC\"");
    inputValuesB1.add(">1");
    inputValuesB1.add("");
    inputValuesB1.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesB1, "1"));

    List<String> inputValuesB2 = new LinkedList<>();
    inputValuesB2.add("\"JS\"");
    inputValuesB2.add(">6");
    inputValuesB2.add("");
    inputValuesB2.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesB2, "1"));

    List<String> inputValuesB3 = new LinkedList<>();
    inputValuesB3.add("\"LL\",\"LLP\"");
    inputValuesB3.add(">10");
    inputValuesB3.add("L0");
    inputValuesB3.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesB3, "2"));

    List<String> inputValuesB4 = new LinkedList<>();
    inputValuesB4.add("\"LL\",\"LLP\"");
    inputValuesB4.add(">50");
    inputValuesB4.add("\"L1\",\"L2\"");
    inputValuesB4.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesB4, "2"));

    List<String> inputValuesB5 = new LinkedList<>();
    inputValuesB5.add("\"LL\",\"LLP\"");
    inputValuesB5.add(">100");
    inputValuesB5.add("\"L3\",\"L4\"");
    inputValuesB5.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesB5, "2"));

    DecisionTableMetadata metadata =
        new DecisionTableMetadata(
            ruleSetId,
            decisionTableInputs,
            decisionTableOutputs,
            HitPolicy.COLLECT,
            Optional.of(BuiltinAggregator.SUM));

    String dmnModel = DmnModelHandler.transform(metadata, rules);

    return dmnModel;
  }
}
