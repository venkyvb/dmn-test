package com.github.venkyvb;

import java.util.*;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.BuiltinAggregator;
import org.camunda.bpm.model.dmn.HitPolicy;

public class BundleTest {

  public static void executeTestOption1() {
    String ruleSetId = "testRuleSet3";
    String dmnModel = setupRuleSetOption1(ruleSetId);

    System.out.println(dmnModel);

    List<Map<String, Object>> result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables().putValue("dim_type", Arrays.asList("RC", "LL")))
            .getResultList();

    DmnModelHandler.printResult("sampleBundleTest", result);
  }

  public static String setupRuleSetOption1(String ruleSetId) {
    List<DecisionTableInput> decisionTableInputs = new LinkedList<>();
    decisionTableInputs.add(new DecisionTableInput("dim_type", "dim_type", "string", "Type"));

    List<DecisionTableOutput> decisionTableOutputs = new LinkedList<>();
    decisionTableOutputs.add(new DecisionTableOutput("dim_result", "dim_result", "string"));

    List<RuleEntry> rules = new LinkedList<>();
    List<String> inputValuesB1 = new LinkedList<>();
    inputValuesB1.add("contains(cellInput, \"RC\")");
    rules.add(new RuleEntry(inputValuesB1, Arrays.asList("\"RC\"")));

    List<String> inputValuesB2 = new LinkedList<>();
    inputValuesB2.add("contains(cellInput, \"JS\")");
    rules.add(new RuleEntry(inputValuesB2, Arrays.asList("\"JS\"")));

    List<String> inputValuesB3 = new LinkedList<>();
    inputValuesB3.add(
        "(contains(cellInput, \"RC\") or contains(cellInput, \"JS\")) and"
            + " (contains(cellInput, \"LL\") or contains(cellInput, \"LLP\"))");
    rules.add(new RuleEntry(inputValuesB3, Arrays.asList("\"RC OR JS AND LL OR LLP\"")));

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

  public static void executeTestOption2() {
    String ruleSetId = "testRuleSet3";
    String dmnModel = setupRuleSetOption2(ruleSetId);

    System.out.println(dmnModel);

    List<Map<String, Object>> result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_rr", "AP")
                    .putValue("dim_ts1", "RC")
                    .putValue("dim_ts1_vol", 1)
                    .putValue("dim_ts2", "JS")
                    .putValue("dim_ts2_vol", 7)
                    .putValue("dim_ls1", "LL")
                    .putValue("dim_ls1_vol", 10)
                    .putValue("dim_level", "TA0")
                    .putValue("dim_otype", "NB"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTest1_RuleFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_rr", "AP")
                    .putValue("dim_ts1", "RC")
                    .putValue("dim_ts1_vol", 1)
                    .putValue("dim_ts2", "JS")
                    .putValue("dim_ts2_vol", 2)
                    .putValue("dim_ls1", "LL")
                    .putValue("dim_ls1_vol", 10)
                    .putValue("dim_level", "TA0")
                    .putValue("dim_otype", "NB"))
            .getResultList();

    DmnModelHandler.printResult("NegativeTest1_RuleNotFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_rr", "AP")
                    .putValue("dim_ts1", "RC")
                    .putValue("dim_ts1_vol", 1)
                    .putValue("dim_ts2", "JS")
                    .putValue("dim_ts2_vol", 7)
                    .putValue("dim_ls1", "LL")
                    .putValue("dim_ls1_vol", 10)
                    .putValue("dim_level", "TA1")
                    .putValue("dim_otype", "NB"))
            .getResultList();

    DmnModelHandler.printResult("NegativeTest2_RuleNotFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_rr", "AP")
                    .putValue("dim_ts1", "RC")
                    .putValue("dim_ts1_vol", 1)
                    .putValue("dim_ts2", "JS")
                    .putValue("dim_ts2_vol", 7)
                    .putValue("dim_ls1", "LL")
                    .putValue("dim_ls1_vol", 10)
                    .putValue("dim_level", "TA2")
                    .putValue("dim_otype", "NB"))
            .getResultList();

    DmnModelHandler.printResult("NegativeTest3_RuleNotFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_rr", "AP")
                    .putValue("dim_ts1", "RC")
                    .putValue("dim_ts1_vol", 1)
                    .putValue("dim_ts2", "JS")
                    .putValue("dim_ts2_vol", 7)
                    .putValue("dim_ls1", "LL")
                    .putValue("dim_ls1_vol", 50)
                    .putValue("dim_level", "TA2")
                    .putValue("dim_otype", "NB"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTest2_RuleFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_rr", "AP")
                    .putValue("dim_ts1", "RC")
                    .putValue("dim_ts1_vol", 2)
                    .putValue("dim_ts2", "JS")
                    .putValue("dim_ts2_vol", 2)
                    .putValue("dim_ls1", "LL")
                    .putValue("dim_ls1_vol", 10)
                    .putValue("dim_level", "TA0")
                    .putValue("dim_otype", "NB"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTest3_RuleFound", result);

    result =
        DmnModelHandler.evaluateRules(
                ruleSetId,
                dmnModel,
                Variables.createVariables()
                    .putValue("dim_rr", "AP")
                    .putValue("dim_ts1", "RC")
                    .putValue("dim_ts1_vol", 2)
                    .putValue("dim_ts2", "JS")
                    .putValue("dim_ts2_vol", 2)
                    .putValue("dim_ls1", "LLP")
                    .putValue("dim_ls1_vol", 10)
                    .putValue("dim_level", "TA0")
                    .putValue("dim_otype", "NB"))
            .getResultList();

    DmnModelHandler.printResult("PositiveTest4_RuleFound", result);
  }

  public static String setupRuleSetOption2(String ruleSetId) {
    List<DecisionTableInput> decisionTableInputs = new LinkedList<>();
    decisionTableInputs.add(new DecisionTableInput("dim_rr", "dim_rr", "string", "RR"));
    decisionTableInputs.add(new DecisionTableInput("dim_ts1", "dim_ts1", "string", "TS1"));
    decisionTableInputs.add(
        new DecisionTableInput("dim_ts1_vol", "dim_ts1_vol", "integer", "TS1 Vol"));
    decisionTableInputs.add(new DecisionTableInput("dim_ts2", "dim_ts2", "string", "TS2"));
    decisionTableInputs.add(
        new DecisionTableInput("dim_ts2_vol", "dim_ts2_vol", "integer", "TS2 Vol"));
    decisionTableInputs.add(new DecisionTableInput("dim_ls1", "dim_ls1", "string", "LS1"));
    decisionTableInputs.add(new DecisionTableInput("dim_level", "dim_level", "string", "Level"));
    decisionTableInputs.add(
        new DecisionTableInput("dim_ls1_vol", "dim_ls1_vol", "integer", "LS1 Vol"));
    decisionTableInputs.add(new DecisionTableInput("dim_otype", "dim_otype", "string", "Otype"));

    List<DecisionTableOutput> decisionTableOutputs = new LinkedList<>();
    decisionTableOutputs.add(new DecisionTableOutput("dim_bundle", "dim_bundle", "string"));

    List<RuleEntry> rules = new LinkedList<>();
    List<String> inputValues1 = new LinkedList<>();
    inputValues1.add("\"AP\"");
    inputValues1.add("\"RC\"");
    inputValues1.add(">1");
    inputValues1.add("");
    inputValues1.add("");
    inputValues1.add("\"LL\",\"LLP\"");
    inputValues1.add("\"TA0\"");
    inputValues1.add(">=10");
    inputValues1.add("\"PIL\",\"NB\"");
    rules.add(new RuleEntry(inputValues1, Arrays.asList("\"Bundle1\"")));

    List<String> inputValues2 = new LinkedList<>();
    inputValues2.add("\"AP\"");
    inputValues2.add("");
    inputValues2.add("");
    inputValues2.add("\"JS\"");
    inputValues2.add(">6");
    inputValues2.add("\"LL\",\"LLP\"");
    inputValues2.add("\"TA0\"");
    inputValues2.add(">=10");
    inputValues2.add("\"PIL\",\"NB\"");
    rules.add(new RuleEntry(inputValues2, Arrays.asList("\"Bundle1\"")));

    List<String> inputValues3 = new LinkedList<>();
    inputValues3.add("\"AP\"");
    inputValues3.add("\"RC\"");
    inputValues3.add(">1");
    inputValues3.add("");
    inputValues3.add("");
    inputValues3.add("\"LL\",\"LLP\"");
    inputValues3.add("\"TA1\",\"TA2\"");
    inputValues3.add(">=50");
    inputValues3.add("\"PIL\",\"NB\"");
    rules.add(new RuleEntry(inputValues3, Arrays.asList("\"Bundle1\"")));

    List<String> inputValues4 = new LinkedList<>();
    inputValues4.add("\"AP\"");
    inputValues4.add("");
    inputValues4.add("");
    inputValues4.add("\"JS\"");
    inputValues4.add(">6");
    inputValues4.add("\"LL\",\"LLP\"");
    inputValues4.add("\"TA1\",\"TA2\"");
    inputValues4.add(">=50");
    inputValues4.add("\"PIL\",\"NB\"");
    rules.add(new RuleEntry(inputValues4, Arrays.asList("\"Bundle1\"")));

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
}
