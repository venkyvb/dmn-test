package com.github.venkyvb;

import java.time.LocalDateTime;
import java.util.*;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.HitPolicy;

public class RulesetTest {

  private static String setUpRuleSet1(String ruleSetId) {

    //
    List<DecisionTableInput> decisionTableInputs = new LinkedList<>();
    decisionTableInputs.add(new DecisionTableInput("dim_type", "dim_type", "string", "Type"));
    decisionTableInputs.add(new DecisionTableInput("dim_family", "dim_family", "string", "Family"));
    decisionTableInputs.add(new DecisionTableInput("dim_term", "dim_term", "integer", "Term"));
    decisionTableInputs.add(
        new DecisionTableInput("dim_validity", "dim_validity", "date", "Validity"));

    List<DecisionTableOutput> decisionTableOutputs = new LinkedList<>();
    decisionTableOutputs.add(new DecisionTableOutput("Discount", "discount", "double"));
    decisionTableOutputs.add(new DecisionTableOutput("Description", "description", "string"));

    List<RuleEntry> rules = new LinkedList<>();

    List<String> inputValuesLnprf1 = new LinkedList<>();
    inputValuesLnprf1.add("\"LNPRF\"");
    inputValuesLnprf1.add("\"LITS\",\"LILS\"");
    inputValuesLnprf1.add("");
    inputValuesLnprf1.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLnprf1, Arrays.asList("50", "\"fifty_percent\"")));

    List<String> inputValuesLnprf2 = new LinkedList<>();
    inputValuesLnprf2.add("\"LNPRF\"");
    inputValuesLnprf2.add("\"LITS\",\"LILS\"");
    inputValuesLnprf2.add("");
    inputValuesLnprf2.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 1, 1, 00, 00, 00), LocalDateTime.of(2020, 10, 1, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLnprf2, Arrays.asList("40", "\"forty_percent\"")));

    List<String> inputValuesLiss1 = new LinkedList<>();
    inputValuesLiss1.add("");
    inputValuesLiss1.add("\"LISS\"");
    inputValuesLiss1.add(">35");
    inputValuesLiss1.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLiss1, Arrays.asList("7.5", "\"seven_and_half_percent\"")));

    List<String> inputValuesLiss2 = new LinkedList<>();
    inputValuesLiss2.add("");
    inputValuesLiss2.add("\"LISS\"");
    inputValuesLiss2.add("(23..35]");
    inputValuesLiss2.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLiss2, Arrays.asList("5", "\"five_percent\"")));

    List<String> inputValuesLiss3 = new LinkedList<>();
    inputValuesLiss3.add("");
    inputValuesLiss3.add("\"LISS\"");
    inputValuesLiss3.add("<=23");
    inputValuesLiss3.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLiss3, Arrays.asList("3", "\"three_percent\"")));

    DecisionTableMetadata metadata =
        new DecisionTableMetadata(
            ruleSetId,
            decisionTableInputs,
            decisionTableOutputs,
            HitPolicy.UNIQUE,
            Optional.empty());

    String dmnModel = DmnModelHandler.transform(metadata, rules);

    return dmnModel;
  }

  public static void testRuleSet1() {

    String ruleSetId = "testRuleSet1";
    String dmnModel = setUpRuleSet1(ruleSetId);

    System.out.println(dmnModel);

    List<Map<String, Object>> result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_type", "LNPRF")
                    .putValue("dim_family", "LITS")
                    .putValue("dim_term", 12)
                    .putValue("dim_validity", "2020-10-01T00:00:00"))
            .getResultList();
    DmnModelHandler.printResult("PositiveTestNprLitsf12_RuleFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_type", "LNPRF")
                    .putValue("dim_family", "LILS")
                    .putValue("dim_term", 12)
                    .putValue("dim_validity", "2020-09-30T00:00:00"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTestNprfLils12_RuleFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_type", "LNPRF")
                    .putValue("dim_family", "LISS")
                    .putValue("dim_term", 12)
                    .putValue("dim_validity", "2020-10-01T00:00:00"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTestNprfLiss12_RuleFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_type", "LISS")
                    .putValue("dim_family", "LISS")
                    .putValue("dim_term", 12)
                    .putValue("dim_validity", "2020-10-01T00:00:00"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTestLissLiss12_RuleFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_type", "LISS")
                    .putValue("dim_family", "ABC")
                    .putValue("dim_term", 12)
                    .putValue("dim_validity", "2020-10-01T00:00:00"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTestLissAbc12_RuleNotFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_type", "LISS")
                    .putValue("dim_family", "LISS")
                    .putValue("dim_term", 26)
                    .putValue("dim_validity", "2020-10-01T00:00:00"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTestLissLiss26_RuleFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_type", "LISS")
                    .putValue("dim_family", "LISS")
                    .putValue("dim_term", 36)
                    .putValue("dim_validity", "2020-10-01T00:00:00"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTestLissLiss36_RuleFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_type", "ABC")
                    .putValue("dim_family", "LISS")
                    .putValue("dim_term", 36)
                    .putValue("dim_validity", "2020-10-01T00:00:00"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTestAbcLiss36_RuleFound", result);
  }
}
