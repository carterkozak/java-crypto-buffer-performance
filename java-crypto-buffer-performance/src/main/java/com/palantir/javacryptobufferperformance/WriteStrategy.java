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
import java.io.OutputStream;

/** Strategy used to write a byte-array to an {@link OutputStream}. */
public enum WriteStrategy {
    /** Equivalent to {@code output.write(bytes)}. */
    ENTIRE_BUFFER() {
        @Override
        void writeTo(byte[] bytes, OutputStream output) throws IOException {
            output.write(bytes);
        }
    },
    /** Writes {@code bytes} to {@code output} at most 16 KiB at a time. */
    CHUNKED() {
        private static final int BUFFER_SIZE = 16 * 1024;

        @Override
        void writeTo(byte[] bytes, OutputStream output) throws IOException {
            for (int i = 0; i < bytes.length; i += BUFFER_SIZE) {
                output.write(bytes, i, Math.min(BUFFER_SIZE, bytes.length - i));
            }
        }
    };

    abstract void writeTo(byte[] bytes, OutputStream output) throws IOException;
}
