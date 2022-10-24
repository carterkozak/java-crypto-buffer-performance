java-crypto-buffer-performance
============
Analysis of java cryptography performance using large buffers.

**Initial Benchmark Results**
_Using an Intel Xeon W-2175 (x86_64) on linux kernel 5.15.0 and JDK 17.0.4.1_
```
Benchmark                             (cipher)  (numBytes)  (writeStrategy)   Mode  Cnt     Score     Error  Units
EncryptionBenchmark.encrypt  AES/GCM/NoPadding     1048576    ENTIRE_BUFFER  thrpt    4  2273.350 ± 579.343  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding     1048576          CHUNKED  thrpt    4  2471.884 ± 120.589  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding    10485760    ENTIRE_BUFFER  thrpt    4     6.506 ±   0.801  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding    10485760          CHUNKED  thrpt    4   249.512 ±  44.803  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding   104857600    ENTIRE_BUFFER  thrpt    4     0.658 ±   0.043  ops/s
EncryptionBenchmark.encrypt  AES/GCM/NoPadding   104857600          CHUNKED  thrpt    4    24.302 ±   3.950  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding     1048576    ENTIRE_BUFFER  thrpt    4  2828.721 ± 429.015  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding     1048576          CHUNKED  thrpt    4  3457.250 ±  99.273  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding    10485760    ENTIRE_BUFFER  thrpt    4    31.805 ±   6.138  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding    10485760          CHUNKED  thrpt    4   353.136 ±   3.790  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding   104857600    ENTIRE_BUFFER  thrpt    4     3.301 ±   0.130  ops/s
EncryptionBenchmark.encrypt  AES/CTR/NoPadding   104857600          CHUNKED  thrpt    4    35.113 ±   0.901  ops/s
```

Gradle Tasks
------------
`./gradlew tasks` - to get the list of gradle tasks


Start Developing
----------------
Run one of the following commands:

* Open directly with IntelliJ using Gradle integration
* `./gradlew eclipse` for Eclipse
