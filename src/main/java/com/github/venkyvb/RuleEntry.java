package com.github.venkyvb;

import java.util.*;

public class RuleEntry {
  private List<String> inputEntries;
  // For now just a single result value
  private String outputEntry;

  public RuleEntry(List<String> inputEntries, String outputEntry) {
    this.inputEntries = inputEntries;
    this.outputEntry = outputEntry;
  }

  public List<String> getInputEntries() {
    return this.inputEntries;
  }

  public String getOutputEntry() {
    return this.outputEntry;
  }
}
