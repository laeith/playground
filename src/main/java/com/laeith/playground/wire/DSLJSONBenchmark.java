package com.laeith.playground.wire;

import com.dslplatform.json.DslJson;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(3)
public class DSLJSONBenchmark {
  
  @State(Scope.Benchmark)
  public static class DSLJSONState {
    DslJson<PingPongJava> dslJson = new DslJson<>();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    byte[] serializedJson;
    
    {
      try {
        outputStream.reset();
        dslJson.serialize(generateMessage(), outputStream);
        serializedJson = outputStream.toByteArray();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  @Benchmark
  public byte[] measureSerialization(final DSLJSONState state) throws IOException {
    state.outputStream.reset();
    state.dslJson.serialize(generateMessage(), state.outputStream);
    return state.outputStream.toByteArray();
  }
  
  @Benchmark
  public PingPongJava measureDeserialization(final DSLJSONState state) throws IOException {
    return state.dslJson.deserialize(PingPongJava.class, state.serializedJson, state.serializedJson.length);
  }
  
  private static PingPongJava generateMessage() {
    PingPongJava pingPong = new PingPongJava();
    pingPong.id = 1;
    pingPong.version = 2;
    pingPong.message = "Random message - " + System.currentTimeMillis();
    pingPong.isImportant = true;
    pingPong.names = Data.NAMES;
    pingPong.ints = Data.INTS;
    pingPong.doubles = Data.DOUBLES;
    
    return pingPong;
  }
}
