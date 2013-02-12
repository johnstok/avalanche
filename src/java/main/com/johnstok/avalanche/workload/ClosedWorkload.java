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
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import com.johnstok.avalanche.Callback;
import com.johnstok.avalanche.Generator;
import com.johnstok.avalanche.Workload;

/**
 * A closed workload.
 *
 * @author Keith Webster Johnston.
 */
public class ClosedWorkload implements Workload {

    private final int conns;
    private final Semaphore _permits;
    private final CountDownLatch _stopLatch;


    public ClosedWorkload(final int i, final int concurrency) {
        conns = i;
        _permits = new Semaphore(concurrency); // FIXME: Test >= 1.
        _stopLatch = new CountDownLatch(i);
    }


    /** {@inheritDoc} */
    @Override
    public Future<Void> execute(final Generator<Void> command) {
        final CCallback cc = new CCallback();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<conns; i++) {
                    try {
                        _permits.acquire();
                        command.generate(cc);
                    } catch (final InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                        // FIXME: Breaking here means the future's latch will never count down to 0.
                    }
                }
            }
        };

        FutureTask<Void> workloadTask = new FutureTask<Void>(r, (Void) null);
        new Thread(workloadTask, "Closed Workload Thread").start();

        return new LatchFuture(_stopLatch); // FIXME: Can't be cancelled.
    }


    private static class LatchFuture implements Future<Void> {

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
        @Override
        public boolean cancel(final boolean mayInterruptIfRunning) {
            throw new UnsupportedOperationException("Method not implemented.");
        }


        /** {@inheritDoc} */
        @Override
        public boolean isCancelled() {
            return false;
        }


        /** {@inheritDoc} */
        @Override
        public boolean isDone() {
            return 0==_latch.getCount();
        }


        /** {@inheritDoc} */
        @Override
        public Void get() throws InterruptedException, ExecutionException {
            _latch.await();
            return null;
        }


        /** {@inheritDoc} */
        @Override
        public Void get(final long timeout, final TimeUnit unit)
            throws InterruptedException,
                ExecutionException,
                TimeoutException {
            if (_latch.await(timeout, unit)) { return null; }
            throw new TimeoutException();
        }
    }


    final class CCallback
        implements
            Callback {

        AtomicLong i = new AtomicLong();

        /** {@inheritDoc} */
        @Override
        public void onComplete() {
            _permits.release();
            _stopLatch.countDown();
            System.out.println(_stopLatch.getCount());
        }
    }
}