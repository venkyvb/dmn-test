package com.github.venkyvb;

import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationListener;

public class DecisionTableEvaluationListener implements DmnDecisionTableEvaluationListener {

  @Override
  public void notify(DmnDecisionTableEvaluationEvent evaluationEvent) {
    System.out.println("EvaluationEvent getInputs -> " + evaluationEvent.getInputs());
    System.out.println("EvaluationEvent getMatchingRules -> " + evaluationEvent.getMatchingRules());
  }
}
