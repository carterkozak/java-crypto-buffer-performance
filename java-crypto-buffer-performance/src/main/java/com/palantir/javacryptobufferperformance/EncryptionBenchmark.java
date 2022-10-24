/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.javacryptobufferperformance;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Warmup(iterations = 4, time = 10)
@Measurement(iterations = 4, time = 4)
@Fork(value = 1)
public class EncryptionBenchmark {

    @Benchmark
    public final void encrypt(BenchmarkState state, Blackhole blackhole) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(state.cipher);
        encrypt(state.writeStrategy, state.data, cipher, state.key, state.parameterSpec, blackhole);
    }

    private void encrypt(
            WriteStrategy writeStrategy,
            byte[] bytes,
            Cipher cipher,
            Key key,
            AlgorithmParameterSpec spec,
            Blackhole blackhole) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            try (CipherOutputStream os = new CipherOutputStream(new BlackholeOutputStream(blackhole), cipher)) {
                writeStrategy.writeTo(bytes, os);
            }
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] _args) throws RunnerException {
        new Runner(new OptionsBuilder()
                        .include(EncryptionBenchmark.class.getSimpleName())
                        .build())
                .run();
    }

    @State(Scope.Benchmark)
    @SuppressWarnings({"DesignForExtension", "StringSplitter", "CheckStyle"})
    public static class BenchmarkState {
        private static final Random random = new Random(314);

        @Param({"AES/GCM/NoPadding", "AES/CTR/NoPadding"})
        public String cipher;

        @Param({
            // 1 MiB
            "1048576",
            // 10 MiB
            "10485760",
            // 100 MiB
            "104857600"
        })
        public int numBytes;

        @Param
        public WriteStrategy writeStrategy;

        public byte[] data;

        public SecretKey key;
        public byte[] iv;

        public AlgorithmParameterSpec parameterSpec;

        @Setup
        public void setup() throws Exception {
            data = new byte[numBytes];
            random.nextBytes(data);
            String[] cipherParts = cipher.split("/");
            String keyAlgorithm = cipherParts[0];
            KeyGenerator keyGen = KeyGenerator.getInstance(keyAlgorithm);
            int keySize = Math.min(Cipher.getMaxAllowedKeyLength(keyAlgorithm), 256);
            keyGen.init(keySize);
            key = keyGen.generateKey();
            iv = new byte[16];
            // Don't use insecure-random for ivs in production!
            random.nextBytes(iv);

            String cipherMode = cipherParts[1];
            switch (cipherMode) {
                case "GCM":
                    parameterSpec = new GCMParameterSpec(8 * 16, iv);
                    break;
                case "CTR":
                    parameterSpec = new IvParameterSpec(iv);
                    break;
                default:
                    throw new IllegalStateException("Unknown cipher mode: " + cipherMode);
            }
        }
    }
}
