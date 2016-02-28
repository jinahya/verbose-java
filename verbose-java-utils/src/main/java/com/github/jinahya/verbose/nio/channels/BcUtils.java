/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.verbose.nio.channels;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import static java.lang.reflect.Proxy.newProxyInstance;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Utilities for byte channels.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class BcUtils {

    /**
     * Creates a new proxy instance which intercepts
     * {@link WritableByteChannel#write(java.nio.ByteBuffer)} method and mimics
     * non-blocking writing by temporarily adjusting the
     * {@link ByteBuffer#remaining()} of the buffer.
     *
     * @param <T> channel type parameter
     * @param type channel type
     * @param channel the channel
     * @return a new proxy instance
     */
    public static <T extends WritableByteChannel> T nonBlocking(
            final Class<T> type, final T channel) {
        final Method method; // <1>
        try {
            method = WritableByteChannel.class.getMethod(
                    "write", ByteBuffer.class);
        } catch (final NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
        final InvocationHandler handler = (p, m, a) -> {
            if (m.equals(method)) { // <1>
                final ByteBuffer src = (ByteBuffer) a[0]; // <2>
                final int limit = src.limit(); // <3>
                try {
                    src.limit(src.position() // <4>
                              + current().nextInt(src.remaining() + 1));
                    return channel.write(src); // <5>
                } finally {
                    src.limit(limit); // <6>
                }
            }
            return m.invoke(channel, a); // <7>
        };
        final Object proxy = newProxyInstance( // <1>
                type.getClassLoader(), new Class<?>[]{type}, handler);
        return type.cast(proxy); // <2>
    }

    private static <T extends WritableByteChannel> T nonBlockingHelper(
            final Class<T> type, final WritableByteChannel channel) {
        return BcUtils.nonBlocking(type, type.cast(channel)); // ClassCastException
    }

    /**
     * Creates a new proxy instance which intercepts
     * {@link WritableByteChannel#write(java.nio.ByteBuffer)} method and mimics
     * non-blocking writing by temporarily adjusting the
     * {@link ByteBuffer#remaining()} of the buffer.
     *
     * @param <T> channel type parameter
     * @param channel the channel
     * @return a new proxy instance
     */
    @SuppressWarnings("unchecked")
    public static <T extends WritableByteChannel> T nonBlocking(
            final T channel) {
        return (T) nonBlockingHelper(channel.getClass(), channel);
    }

    /**
     * Creates a new proxy instance which intercepts the invocation of
     * {@link ReadableByteChannel#read(java.nio.ByteBuffer)} and mimics
     * non-blocking reading by temporarily adjusting the
     * {@link ByteBuffer#remaining()} of the buffer.
     *
     * @param <T> channel type parameter
     * @param type channel type
     * @param channel the channel
     * @return a new proxy
     */
    public static <T extends ReadableByteChannel> T nonBlocking(
            final Class<T> type, final T channel) {
        final Method method; // <1>
        try {
            method = ReadableByteChannel.class.getMethod(
                    "read", ByteBuffer.class);
        } catch (final NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
        final InvocationHandler handler = (p, m, a) -> { // <1>
            if (method.equals(m)) { // <2>
                final ByteBuffer dst = (ByteBuffer) a[0]; // <3>
                final int limit = dst.limit(); // <4>
                try {
                    dst.limit(dst.position() // <5>
                              + current().nextInt(dst.remaining() + 1));
                    return channel.read(dst); // <6>
                } finally {
                    dst.limit(limit); // <7>
                }
            }
            return m.invoke(channel, a); // <8>
        };
        final Object proxy = newProxyInstance( // <1>
                type.getClassLoader(), new Class<?>[]{type}, handler);
        return type.cast(proxy); // <2>
    }

    private static <T extends ReadableByteChannel> T nonBlockingHelper(
            final Class<T> type, final ReadableByteChannel channel) {
        return nonBlocking(type, type.cast(channel));
    }

    /**
     * Creates a new proxy which intercepts the invocation of
     * {@link ReadableByteChannel#read(java.nio.ByteBuffer)} and mimics
     * non-blocking channel by temporarily adjusting the
     * {@link ByteBuffer#remaining()} of the buffer.
     *
     * @param <T> channel type parameter
     * @param channel the channel
     * @return a new proxy
     */
    @SuppressWarnings("unchecked")
    public static <T extends ReadableByteChannel> T nonBlocking(
            final T channel) {
        return (T) nonBlockingHelper(channel.getClass(), channel);
    }

    private BcUtils() {
        super();
    }
}
