package com.github.venkyvb;

import java.util.*;

import org.camunda.bpm.model.dmn.HitPolicy;

public class DecisionTableMetadata {
    
    private String ruleSetId;
    private List<DecisionTableInput> inputs;
    private List<DecisionTableOutput> outputs;
    private HitPolicy hitPolicy;


    public DecisionTableMetadata(String ruleSetId, List<DecisionTableInput> inputs, List<DecisionTableOutput> outputs, HitPolicy hitPolicy) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.hitPolicy = hitPolicy;
        this.ruleSetId = ruleSetId;
    }


    public List<DecisionTableInput> getInputs() {
        return this.inputs;
    }

    public List<DecisionTableOutput> getOutputs() {
        return this.outputs;
    }


    public HitPolicy getHitPolicy() {
        return this.hitPolicy;
    }

    public String getRuleSetId() {
        return this.ruleSetId;
    }

}

