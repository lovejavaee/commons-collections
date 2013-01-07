/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.commons.collections.iterators;

import java.util.Iterator;

import org.apache.commons.collections.ResettableIterator;

/**
 * Adapter to make an {@link Iterator Iterator} instance appear to be an
 * {@link Iterable Iterable} instance.  The iterable can be constructed in one
 * of two variants:  single use, multiple use.
 * 
 * <p>
 * In the single use iterable case, the iterable is only usable for one
 * iterative operation over the source iterator.  Subsequent iterative
 * operations use the same, exhausted source iterator.  To create a single use
 * iterable, construct a new {@link IteratorIterable} using a {@link Iterator}
 * that is NOT a {@link ResettableIterator} iterator:
 * <pre>
 *   Iterator<Integer> iterator = // some non-resettable iterator
 *   Iterable<Integer> iterable = new IteratorIterable<Integer>(iterator);
 * </pre>
 * </p>
 * 
 * <p>
 * In the multiple use iterable case, the iterable is usable for any number of
 * iterative operations over the source iterator.  Of special note, even though
 * the iterable supports multiple iterations, it does not support concurrent
 * iterations. To implicitly create a multiple use iterable, construct a new
 * {@link IteratorIterable} using a {@link ResettableIterator} iterator:
 * <pre>
 *   Integer[] array = {Integer.valueOf(1),Integer.valueOf(2),Integer.valueOf(3)};
 *   Iterator<Integer> iterator = IteratorUtils.arrayIterator(array); // a resettable iterator
 *   Iterable<Integer> iterable = new IteratorIterable<Integer>(iterator);
 * </pre>
 * </p>
 * 
 * <p>
 * A multiple use iterable can also be explicitly constructed using any
 * {@link Iterator} and specifying <code>true</code> for the
 * <code>multipleUse</code> flag:
 * <pre>
 *   Iterator<Integer> iterator = // some non-resettable iterator
 *   Iterable<Integer> iterable = new IteratorIterable<Integer>(iterator, true);
 * </pre>
 * </p>
 * 
 * @since 4.0
 * @version $Id$
 */
public class IteratorIterable<E> implements Iterable<E> {

    /**
     * Factory method to create an {@link Iterator Iterator} from another
     * iterator over objects of a different subtype.
     */
    private static <E> Iterator<E> createTypesafeIterator(
            final Iterator<? extends E> iterator) {
        return new Iterator<E>() {
            public boolean hasNext() {
                return iterator.hasNext();
            }

            public E next() {
                return iterator.next();
            }

            public void remove() {
                iterator.remove();
            }
        };
    }

    /** the iterator being adapted into an iterable. */
    private final Iterator<? extends E> iterator;
    
    /** the iterator parameterized as the {@link #iterator()} return type. */ 
    private final Iterator<E> typeSafeIterator;
    
    /**
     * Constructs a new <code>IteratorIterable</code> that will use the given
     * iterator.
     * 
     * @param iterator the iterator to use.
     */
    public IteratorIterable(final Iterator<? extends E> iterator) {
        this(iterator, false);
    }

    /**
     * Constructs a new <code>IteratorIterable</code> that will use the given
     * iterator.
     * 
     * @param iterator the iterator to use.
     * @param multipleUse <code>true</code> if the new iterable can be used in multiple iterations
     */
    public IteratorIterable(final Iterator<? extends E> iterator, final boolean multipleUse) {
        super();
        if (multipleUse && !(iterator instanceof ResettableIterator)) {
            this.iterator = new ListIteratorWrapper<E>(iterator); 
        } else {
            this.iterator = iterator;
        }
        this.typeSafeIterator = createTypesafeIterator(this.iterator);
    }

    /**
     * Gets the iterator wrapped by this iterable.
     * 
     * @return the iterator
     */
    public Iterator<E> iterator() {
        if (iterator instanceof ResettableIterator) {
            ((ResettableIterator<? extends E>)iterator).reset();
        }
        return typeSafeIterator;
    }
}
