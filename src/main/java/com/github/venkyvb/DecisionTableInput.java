package com.github.venkyvb;

public class DecisionTableInput {
    private String input;
    private String inputExpression;
    private String inputType;
    private String inputLabel;


    public DecisionTableInput(String input, String inputExpression, String inputType, String inputLabel) {
        this.input = input;
        this.inputExpression = inputExpression;
        this.inputType = inputType;
        this.inputLabel = inputLabel;
    }
    

    public String getInput() {
        return this.input;
    }

    public String getInputExpression() {
        return this.inputExpression;
    }

    public String getInputType() {
        return this.inputType;
    }

    public String getInputLabel() {
        return this.inputLabel;
    }

} 