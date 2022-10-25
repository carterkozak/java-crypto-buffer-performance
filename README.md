Java Cryptography Buffer Performance
====================================
Analysis of java cryptography performance using large buffers.

Running Locally
---------------

You may run using your preferred IDE using the main method in [EncryptionBenchmark](./java-crypto-buffer-performance/src/main/java/com/palantir/javacryptobufferperformance/EncryptionBenchmark.java),
or by executing `./gradlew run`. Alternatively, you can build a zip archive using `./gradlew distZip` for execution in another environment.

Initial Benchmark Results
-------------------------

_Using an Intel Xeon W-2175 (x86_64) on linux kernel 5.15.0_

### JDK-17

```
# JMH version: 1.35
# VM version: JDK 17.0.5, OpenJDK 64-Bit Server VM, 17.0.5+8-LTS
# VM invoker: /jdk/amazon-corretto-17.0.5.8.1-linux-x64/bin/java
# VM options: -Dfile.encoding=UTF-8 -Duser.country=US -Duser.language=en -Duser.variant
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 4 iterations, 10 s each
# Measurement: 4 iterations, 4 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.palantir.javacryptobufferperformance.EncryptionBenchmark.encrypt
...
# Run complete. Total time: 00:11:26

Benchmark                             (cipher)  (numBytes)  (writeStrategy)   Mode  Cnt     Score     Error  Units
EncryptionBenchmark.encrypt  AES/GCM/NoPadding     1048576    ENTIRE_BUFFER  thrpt    4  2215.898 ± 185.661  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding     1048576          CHUNKED  thrpt    4  2516.770 ± 193.009  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding    10485760    ENTIRE_BUFFER  thrpt    4     6.427 ±   0.475  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding    10485760          CHUNKED  thrpt    4   246.956 ±  51.193  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding   104857600    ENTIRE_BUFFER  thrpt    4     0.620 ±   0.096  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding   104857600          CHUNKED  thrpt    4    24.633 ±   2.784  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding     1048576    ENTIRE_BUFFER  thrpt    4  2933.808 ±  17.538  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding     1048576          CHUNKED  thrpt    4  3277.374 ± 569.573  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding    10485760    ENTIRE_BUFFER  thrpt    4    31.775 ±   1.898  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding    10485760          CHUNKED  thrpt    4   332.873 ±  55.589  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding   104857600    ENTIRE_BUFFER  thrpt    4     3.174 ±   0.171  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding   104857600          CHUNKED  thrpt    4    33.909 ±   1.675  ops/s
```

### JDK-19
```
# JMH version: 1.35
# VM version: JDK 19.0.1, OpenJDK 64-Bit Server VM, 19.0.1+10-FR
# VM invoker: /jdk/amazon-corretto-19.0.1.10.1-linux-x64/bin/java
# VM options: -Dfile.encoding=UTF-8 -Duser.country=US -Duser.language=en -Duser.variant
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 4 iterations, 10 s each
# Measurement: 4 iterations, 4 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.palantir.javacryptobufferperformance.EncryptionBenchmark.encrypt

# Run complete. Total time: 00:11:20

Benchmark                             (cipher)  (numBytes)  (writeStrategy)   Mode  Cnt     Score     Error  Units
EncryptionBenchmark.encrypt  AES/GCM/NoPadding     1048576    ENTIRE_BUFFER  thrpt    4  2125.445 ± 418.329  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding     1048576          CHUNKED  thrpt    4  2457.693 ± 290.441  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding    10485760    ENTIRE_BUFFER  thrpt    4   225.984 ±   1.411  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding    10485760          CHUNKED  thrpt    4   257.000 ±   4.423  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding   104857600    ENTIRE_BUFFER  thrpt    4    22.618 ±   0.129  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding   104857600          CHUNKED  thrpt    4    25.258 ±   0.903  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding     1048576    ENTIRE_BUFFER  thrpt    4  2924.690 ±  69.371  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding     1048576          CHUNKED  thrpt    4  3223.378 ± 426.088  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding    10485760    ENTIRE_BUFFER  thrpt    4    32.698 ±   2.155  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding    10485760          CHUNKED  thrpt    4   342.303 ±  46.821  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding   104857600    ENTIRE_BUFFER  thrpt    4     3.212 ±   0.503  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding   104857600          CHUNKED  thrpt    4    34.635 ±   0.660  ops/s
```

Gradle Tasks
------------
`./gradlew tasks` - to get the list of gradle tasks


Start Developing
----------------
Run one of the following commands:

* Open directly with IntelliJ using Gradle integration
* `./gradlew eclipse` for Eclipse
