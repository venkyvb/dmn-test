package com.github.venkyvb;

import java.util.*;

public class RuleEntry {
  private List<String> inputEntries;
  // For now just a single result value
  private List<String> outputEntries;

  public RuleEntry(List<String> inputEntries, List<String> outputEntries) {
    this.inputEntries = inputEntries;
    this.outputEntries = outputEntries;
  }

  public List<String> getInputEntries() {
    return this.inputEntries;
  }

  public List<String> getOutputEntries() {
    return this.outputEntries;
  }
}
