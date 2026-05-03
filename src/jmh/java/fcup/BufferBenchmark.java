package fcup;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class BufferBenchmark {

    private Buffer buffer;

    @Setup(Level.Invocation)
    public void setUp() {
        buffer = new Buffer("The quick brown fox jumps over the lazy dog");
    }

    @Benchmark
    public void insertCharacters() {
        for (int i = 0; i < 100; i++) {
            buffer.insertChar('x');
        }
    }

    @Benchmark
    public void insertAndDeleteCharacters() {
        for (int i = 0; i < 50; i++) {
            buffer.insertChar('a');
        }
        for (int i = 0; i < 50; i++) {
            buffer.deleteChar();
        }
    }

    @Benchmark
    public void insertNewlines() {
        buffer.setCursor(new Cursor(0, 5));
        buffer.insertChar('\n');
        buffer.setCursor(new Cursor(1, 5));
        buffer.insertChar('\n');
        buffer.setCursor(new Cursor(2, 5));
        buffer.insertChar('\n');
    }

    @Benchmark
    public void moveNextThroughBuffer() {
        for (int i = 0; i < 43; i++) {
            buffer.moveNext();
        }
    }

    @Benchmark
    public void movePrevThroughBuffer() {
        buffer.setCursor(new Cursor(0, 43));
        for (int i = 0; i < 43; i++) {
            buffer.movePrev();
        }
    }

    @Benchmark
    public void insertString() {
        buffer.insertStr("Hello, World!");
    }

    @Benchmark
    public int getNumLines() {
        return buffer.getNumLines();
    }

    @Benchmark
    public StringBuilder getNthLine() {
        return buffer.getNthLine(0);
    }
}
