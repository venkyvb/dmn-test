package com.github.venkyvb;

public class DecisionTableOutput {

  private String outputLabel;
  private String outputName;
  private String outputType;

  public DecisionTableOutput(String outputLabel, String outputName, String outputType) {
    this.outputLabel = outputLabel;
    this.outputName = outputName;
    this.outputType = outputType;
  }

  public String getOutputLabel() {
    return this.outputLabel;
  }

  public String getOutputName() {
    return this.outputName;
  }

  public String getOutputType() {
    return this.outputType;
  }
}
