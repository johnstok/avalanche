/*-----------------------------------------------------------------------------
 * Copyright © 2013 Keith Webster Johnston.
 * All rights reserved.
 *
 * This file is part of avalanche.
 *
 * avalanche is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * avalanche is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with avalanche. If not, see <http://www.gnu.org/licenses/>.
 *---------------------------------------------------------------------------*/
package com.johnstok.avalanche;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * A future whose result is known at creation time.
 *
 * @author Keith Webster Johnston.
 */
public class DoneFuture<V>
    implements
        Future<V> {

    private final Throwable _exception;
    private final V         _result;


    /**
     * Constructor.
     *
     * <p>This will create a future that is cancelled.
     */
    public DoneFuture() {
        _exception = null;
        _result = null;
    }


    /**
     * Constructor.
     *
     * <p>This will create a future whose result is an exception.
     *
     * @param exception The result.
     */
    public DoneFuture(final Throwable exception) {
        _exception = exception;
        _result = null;
    }


    /**
     * Constructor.
     *
     * @param result The result.
     */
    public DoneFuture(final V result) {
        _exception = null;
        _result = result;
    }


    /** {@inheritDoc} */
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return isCancelled();
    }


    /** {@inheritDoc} */
    public boolean isCancelled() {
        return null==_exception && null==_result;
    }


    /** {@inheritDoc} */
    public boolean isDone() {
        return true;
    }


    /** {@inheritDoc} */
    public V get()throws ExecutionException {
        if (null!=_exception) {
            throw new ExecutionException(_exception);
        } else if (null!=_result) {
            return _result;
        } else {
            throw new CancellationException();
        }
    }


    /** {@inheritDoc} */
    public V get(final long timeout,
                 final TimeUnit unit) throws ExecutionException {
        return get();
    }
}
