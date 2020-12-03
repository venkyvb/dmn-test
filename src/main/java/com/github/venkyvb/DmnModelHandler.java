package com.github.venkyvb;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationListener;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
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

public class DmnModelHandler {

  private static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

  public static String transform(DecisionTableMetadata metadata, List<RuleEntry> rules) {
    DmnModelInstance modelInstance = getDmnModelInstance(metadata.getRuleSetId());
    Definitions definitions = modelInstance.getDefinitions();

    Decision decision = modelInstance.newInstance(Decision.class);
    decision.setId(getDecisionId(metadata.getRuleSetId()));
    decision.setName(getDecisionId(metadata.getRuleSetId()));
    definitions.addChildElement(decision);

    DecisionTable decisionTable = createDecisionTable(modelInstance, metadata);
    decision.addChildElement(decisionTable);

    for (RuleEntry ruleEntry : rules) {
      Rule rule = addRule(modelInstance, ruleEntry);
      decisionTable.addChildElement(rule);
    }

    Dmn.validateModel(modelInstance);

    return Dmn.convertToString(modelInstance);
  }

  public static DmnDecisionTableResult evaluateRules(
      String ruleSetId, String dmnModel, VariableMap variableMap) {

    DmnModelInstance modelInstance =
        Dmn.readModelFromStream(new ByteArrayInputStream(dmnModel.getBytes()));

    DmnEngineConfiguration configuration =
        DmnEngineConfiguration.createDefaultDmnEngineConfiguration();

    DmnDecisionTableEvaluationListener listener = new DecisionTableEvaluationListener();

    configuration.getCustomPostDecisionTableEvaluationListeners().add(listener);

    DmnEngine dmnEngine = configuration.buildEngine();

    DmnDecision decision = dmnEngine.parseDecision(getDecisionId(ruleSetId), modelInstance);

    DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(decision, variableMap);

    return result;
  }

  public static DmnModelInstance getDmnModelInstance(String ruleSetId) {
    DmnModelInstance modelInstance = Dmn.createEmptyModel();
    Definitions definitions = modelInstance.newInstance(Definitions.class);
    definitions.setNamespace("http://camunda.org/schema/1.0/dmn");
    definitions.setName(getDefinitionId(ruleSetId));
    definitions.setId(getDefinitionId(ruleSetId));
    modelInstance.setDefinitions(definitions);

    return modelInstance;
  }

  public static String getDefinitionId(String ruleSetId) {
    return String.join("_", "definition", ruleSetId);
  }

  public static String getDecisionId(String ruleSetId) {
    return String.join("_", "decision", ruleSetId);
  }

  public static DecisionTable createDecisionTable(
      DmnModelInstance modelInstance, DecisionTableMetadata metadata) {

    DecisionTable decisionTable = modelInstance.newInstance(DecisionTable.class);
    decisionTable.setHitPolicy(metadata.getHitPolicy());
    if (metadata.getAggregator().isPresent()) {
      decisionTable.setAggregation(metadata.getAggregator().get());
    }

    for (DecisionTableInput entry : metadata.getInputs()) {
      Input input = modelInstance.newInstance(Input.class);
      input.setLabel(entry.getInputLabel());
      InputExpression inputExpression = modelInstance.newInstance(InputExpression.class);
      inputExpression.setTypeRef(entry.getInputType());
      Text text = modelInstance.newInstance(Text.class);
      text.setTextContent(entry.getInputExpression());
      inputExpression.setText(text);
      input.addChildElement(inputExpression);
      decisionTable.addChildElement(input);
    }

    for (DecisionTableOutput entry : metadata.getOutputs()) {
      Output output = modelInstance.newInstance(Output.class);
      output.setLabel(entry.getOutputLabel());
      output.setName(entry.getOutputName());
      output.setTypeRef(entry.getOutputType());
      decisionTable.addChildElement(output);
    }

    return decisionTable;
  }

  public static Rule addRule(DmnModelInstance modelInstance, RuleEntry ruleEntry) {

    Rule rule = modelInstance.newInstance(Rule.class);

    for (String entry : ruleEntry.getInputEntries()) {
      Text text = modelInstance.newInstance(Text.class);
      text.setTextContent(entry);
      InputEntry inputEntry = modelInstance.newInstance(InputEntry.class);
      inputEntry.addChildElement(text);
      rule.addChildElement(inputEntry);
    }

    OutputEntry outputEntry = modelInstance.newInstance(OutputEntry.class);
    Text text = modelInstance.newInstance(Text.class);
    text.setTextContent(ruleEntry.getOutputEntry());
    outputEntry.addChildElement(text);
    rule.addChildElement(outputEntry);

    return rule;
  }

  public static String getValidityDateInputEntry(LocalDateTime from, LocalDateTime to) {

    String fromDate = from.format(formatter);
    String toDate = to.format(formatter);

    // From date inclusive, to date exclusive
    return "[date and time(\"" + fromDate + "\")..date and time(\"" + toDate + "\"))";
  }

  // For testing only
  public static void printResult(String testName, List<Map<String, Object>> result) {
    System.out.println(testName);
    for (Map<String, Object> entry : result) {
      System.out.println(entry);
    }
  }
}
