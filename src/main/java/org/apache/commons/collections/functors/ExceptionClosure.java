/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.functors;

import java.io.Serializable;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.FunctorException;

/**
 * Closure implementation that always throws an exception.
 *
 * @since 3.0
 * @version $Id$
 */
public final class ExceptionClosure<E> implements Closure<E>, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = 7179106032121985545L;

    /** Singleton predicate instance */
    public static final Closure<Object> INSTANCE = new ExceptionClosure<Object>();

    /**
     * Factory returning the singleton instance.
     * 
     * @param <E> the type that the closure acts on
     * @return the singleton instance
     * @since 3.1
     */
    @SuppressWarnings("unchecked")
    public static <E> Closure<E> exceptionClosure() {
        return (Closure<E>) INSTANCE;
    }

    /**
     * Restricted constructor.
     */
    private ExceptionClosure() {
        super();
    }

    /**
     * Always throw an exception.
     * 
     * @param input  the input object
     * @throws FunctorException always
     */
    public void execute(final E input) {
        throw new FunctorException("ExceptionClosure invoked");
    }

    private Object readResolve() {
        return INSTANCE;
    }

}
