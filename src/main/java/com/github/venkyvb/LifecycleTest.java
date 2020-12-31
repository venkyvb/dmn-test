package com.github.venkyvb;

import java.time.*;
import java.util.*;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.HitPolicy;

public class LifecycleTest {

  public static void execute() {

    DmnEngine engine = DmnModelHandler.getDmnEngine();

    List<DecisionTableInput> decisionTableInputs = new LinkedList<>();
    decisionTableInputs.add(new DecisionTableInput("dim_type", "dim_type", "string", "Type"));
    decisionTableInputs.add(new DecisionTableInput("dim_family", "dim_family", "string", "Family"));
    decisionTableInputs.add(new DecisionTableInput("dim_term", "dim_term", "integer", "Term"));
    decisionTableInputs.add(
        new DecisionTableInput("dim_validity", "dim_validity", "date", "Validity"));

    List<DecisionTableOutput> decisionTableOutputs = new LinkedList<>();
    decisionTableOutputs.add(new DecisionTableOutput("Discount", "discount", "double"));

    // Create and execute an initial set of Rules for a given RuleSet
    List<RuleEntry> rules = new LinkedList<>();

    List<String> inputValuesLnprf1 = new LinkedList<>();
    inputValuesLnprf1.add("\"LNPRF\"");
    inputValuesLnprf1.add("\"LITS\",\"LILS\"");
    inputValuesLnprf1.add("");
    inputValuesLnprf1.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 1, 1, 00, 00, 00), LocalDateTime.of(2021, 12, 31, 00, 00, 00)));
    rules.add(new RuleEntry(inputValuesLnprf1, Arrays.asList("40")));

    String ruleSetId = "lifeCycleTest1";

    DecisionTableMetadata metadata =
        new DecisionTableMetadata(
            ruleSetId,
            decisionTableInputs,
            decisionTableOutputs,
            HitPolicy.UNIQUE,
            Optional.empty());

    String dmnModel = DmnModelHandler.transform(metadata, rules);

    List<Map<String, Object>> result =
        DmnModelHandler.evaluateRulesWithEngineInstance(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_type", "LNPRF")
                    .putValue("dim_family", "LITS")
                    .putValue("dim_term", 12)
                    .putValue("dim_validity", "2020-10-01T00:00:00"),
                engine)
            .getResultList();
    DmnModelHandler.printResult("PositiveLifecycleTest1", result);

    // Update the rules for the rule set by splitting the validity of an existing rule
    // Run evaluation for the updated ruleset
    List<RuleEntry> rulesUpdated = new LinkedList<>();

    List<String> inputValuesLnprfU1 = new LinkedList<>();
    inputValuesLnprfU1.add("\"LNPRF\"");
    inputValuesLnprfU1.add("\"LITS\",\"LILS\"");
    inputValuesLnprfU1.add("");
    inputValuesLnprfU1.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 1, 1, 00, 00, 00), LocalDateTime.of(2020, 10, 1, 00, 00, 00)));
    rulesUpdated.add(new RuleEntry(inputValuesLnprfU1, Arrays.asList("40")));

    List<String> inputValuesLnprfU2 = new LinkedList<>();
    inputValuesLnprfU2.add("\"LNPRF\"");
    inputValuesLnprfU2.add("\"LITS\",\"LILS\"");
    inputValuesLnprfU2.add("");
    inputValuesLnprfU2.add(
        DmnModelHandler.getValidityDateInputEntry(
            LocalDateTime.of(2020, 10, 1, 00, 00, 00), LocalDateTime.of(2021, 12, 31, 00, 00, 00)));
    rulesUpdated.add(new RuleEntry(inputValuesLnprfU2, Arrays.asList("50")));

    metadata =
        new DecisionTableMetadata(
            ruleSetId,
            decisionTableInputs,
            decisionTableOutputs,
            HitPolicy.UNIQUE,
            Optional.empty());

    dmnModel = DmnModelHandler.transform(metadata, rulesUpdated);

    result =
        DmnModelHandler.evaluateRulesWithEngineInstance(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_type", "LNPRF")
                    .putValue("dim_family", "LITS")
                    .putValue("dim_term", 12)
                    .putValue("dim_validity", "2020-10-01T00:00:00"),
                engine)
            .getResultList();
    DmnModelHandler.printResult("PositiveLifecycleTest2", result);
  }
}
