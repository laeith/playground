package com.laeith.playground.wire;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laeith.playground.utils.Data;
import com.laeith.playground.wire.json.PingPongJava;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(3)
public class JacksonJSONBenchmark {
  
  @State(Scope.Benchmark)
  public static class JacksonJSONState {
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] serializedJson;
    
    {
      try {
        serializedJson = objectMapper.writeValueAsBytes(generateMessage());
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
  }
  
  @Benchmark
  public byte[] measureSerialization(final JacksonJSONState state) throws JsonProcessingException {
    return state.objectMapper.writeValueAsBytes(generateMessage());
  }
  
  @Benchmark
  public PingPongJava measureDeserialization(final JacksonJSONState state) throws IOException {
    return state.objectMapper.readValue(state.serializedJson, PingPongJava.class);
  }
  
  private static PingPongJava generateMessage() {
    PingPongJava pingPong = new PingPongJava();
    pingPong.setId(1);
    pingPong.setVersion(2);
    pingPong.setMessage("Random message - " + System.currentTimeMillis());
    pingPong.setImportant(true);
    pingPong.setNames(Data.NAMES);
    pingPong.setInts(Data.INTS);
    pingPong.setDoubles(Data.DOUBLES);
    
    return pingPong;
  }
  
}
