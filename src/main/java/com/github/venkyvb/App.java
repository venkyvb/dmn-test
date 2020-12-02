package com.github.venkyvb;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.HitPolicy;

public final class App {
  private App() {}

  public static void main(String[] args) throws ParseException {
    // testRuleSet1();
    // BundleTest.executeTest();
    BundleTest.executeTestOption2();
  }

  private static String setUpRuleSet1(String ruleSetId) {

    //
    List<DecisionTableInput> decisionTableInputs = new LinkedList<>();
    decisionTableInputs.add(new DecisionTableInput("urn_type", "urn_type", "string", "Type"));
    decisionTableInputs.add(new DecisionTableInput("urn_family", "urn_family", "string", "Family"));
    decisionTableInputs.add(new DecisionTableInput("urn_term", "urn_term", "integer", "Term"));
    decisionTableInputs.add(
        new DecisionTableInput("urn_validity", "urn_validity", "date", "Validity"));

    List<DecisionTableOutput> decisionTableOutputs = new LinkedList<>();
    decisionTableOutputs.add(new DecisionTableOutput("Discount", "discount", "double"));

    List<RuleEntry> rules = new LinkedList<>();

    List<String> inputValuesLnprf1 = new LinkedList<>();
    inputValuesLnprf1.add("\"LNPRF\"");
    inputValuesLnprf1.add("\"LITS\",\"LILS\"");
    inputValuesLnprf1.add("");
    inputValuesLnprf1.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLnprf1, "50"));

    List<String> inputValuesLnprf2 = new LinkedList<>();
    inputValuesLnprf2.add("\"LNPRF\"");
    inputValuesLnprf2.add("\"LITS\",\"LILS\"");
    inputValuesLnprf2.add("");
    inputValuesLnprf2.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 1, 1, 00, 00, 00), LocalDateTime.of(2020, 10, 1, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLnprf2, "40"));

    List<String> inputValuesLiss1 = new LinkedList<>();
    inputValuesLiss1.add("");
    inputValuesLiss1.add("\"LISS\"");
    inputValuesLiss1.add(">35");
    inputValuesLiss1.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLiss1, "7.5"));

    List<String> inputValuesLiss2 = new LinkedList<>();
    inputValuesLiss2.add("");
    inputValuesLiss2.add("\"LISS\"");
    inputValuesLiss2.add("(23..35]");
    inputValuesLiss2.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLiss2, "5"));

    List<String> inputValuesLiss3 = new LinkedList<>();
    inputValuesLiss3.add("");
    inputValuesLiss3.add("\"LISS\"");
    inputValuesLiss3.add("<=23");
    inputValuesLiss3.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 6, 30, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLiss3, "3"));

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

  private static void testRuleSet1() {

    String ruleSetId = "testRuleSet1";
    String dmnModel = setUpRuleSet1(ruleSetId);

    System.out.println(dmnModel);

    System.out.println(
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("urn_type", "LNPRF")
                    .putValue("urn_family", "LITS")
                    .putValue("urn_term", 12)
                    .putValue("urn_validity", "2020-10-01T00:00:00"))
            .toString());

    System.out.println(
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("urn_type", "LNPRF")
                    .putValue("urn_family", "LILS")
                    .putValue("urn_term", 12)
                    .putValue("urn_validity", "2020-09-30T00:00:00"))
            .toString());

    System.out.println(
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("urn_type", "LNPRF")
                    .putValue("urn_family", "LISS")
                    .putValue("urn_term", 12)
                    .putValue("urn_validity", "2020-10-01T00:00:00"))
            .toString());

    System.out.println(
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("urn_type", "LISS")
                    .putValue("urn_family", "LISS")
                    .putValue("urn_term", 12)
                    .putValue("urn_validity", "2020-10-01T00:00:00"))
            .toString());

    System.out.println(
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("urn_type", "LISS")
                    .putValue("urn_family", "ABC")
                    .putValue("urn_term", 12)
                    .putValue("urn_validity", "2020-10-01T00:00:00"))
            .toString());

    System.out.println(
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("urn_type", "LISS")
                    .putValue("urn_family", "LISS")
                    .putValue("urn_term", 26)
                    .putValue("urn_validity", "2020-10-01T00:00:00"))
            .toString());

    System.out.println(
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("urn_type", "LISS")
                    .putValue("urn_family", "LISS")
                    .putValue("urn_term", 36)
                    .putValue("urn_validity", "2020-10-01T00:00:00"))
            .toString());

    System.out.println(
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("urn_type", "ABC")
                    .putValue("urn_family", "LISS")
                    .putValue("urn_term", 36)
                    .putValue("urn_validity", "2020-10-01T00:00:00"))
            .toString());
  }
}
