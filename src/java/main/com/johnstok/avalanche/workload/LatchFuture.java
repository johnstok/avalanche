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
package com.johnstok.avalanche.workload;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A future backed by a countdown latch.
 *
 * @author Keith Webster Johnston.
 */
class LatchFuture implements Future<Void> {

    private final CountDownLatch _latch;


    /**
     * Constructor.
     *
     * @param latch
     */
    public LatchFuture(final CountDownLatch latch) {
        _latch = latch; // FIXME: Check for null.
    }


    /** {@inheritDoc} */
    public boolean cancel(final boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    public boolean isCancelled() {
        return false;
    }


    /** {@inheritDoc} */
    public boolean isDone() {
        return 0==_latch.getCount();
    }


    /** {@inheritDoc} */
    public Void get() throws InterruptedException, ExecutionException {
        _latch.await();
        return null;
    }


    /** {@inheritDoc} */
    public Void get(final long timeout, final TimeUnit unit)
        throws InterruptedException,
            ExecutionException,
            TimeoutException {
        if (_latch.await(timeout, unit)) { return null; }
        throw new TimeoutException();
    }
}