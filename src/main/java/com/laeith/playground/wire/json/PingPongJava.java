package com.laeith.playground.wire.json;

import com.dslplatform.json.CompiledJson;

import java.io.Serializable;
import java.util.List;

@CompiledJson
public class PingPongJava implements Serializable {
  private long id;
  private int version;
  private String message;
  private boolean isImportant;
  private List<String> names;
  private List<Integer> ints;
  private List<Double> doubles;
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public int getVersion() {
    return version;
  }
  
  public void setVersion(int version) {
    this.version = version;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
  
  public boolean isImportant() {
    return isImportant;
  }
  
  public void setImportant(boolean important) {
    isImportant = important;
  }
  
  public List<String> getNames() {
    return names;
  }
  
  public void setNames(List<String> names) {
    this.names = names;
  }
  
  public List<Integer> getInts() {
    return ints;
  }
  
  public void setInts(List<Integer> ints) {
    this.ints = ints;
  }
  
  public List<Double> getDoubles() {
    return doubles;
  }
  
  public void setDoubles(List<Double> doubles) {
    this.doubles = doubles;
  }
  
  public void clear() {
    id = 0;
    version = 0;
    message = null;
    isImportant = false;
    names = null;
    ints = null;
    doubles = null;
  }
}
