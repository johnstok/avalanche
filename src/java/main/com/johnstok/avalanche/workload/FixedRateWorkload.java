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

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import com.johnstok.avalanche.Generator;
import com.johnstok.avalanche.GeneratorRunnable;
import com.johnstok.avalanche.NoOpCallback;
import com.johnstok.avalanche.Workload;

public class FixedRateWorkload implements Workload {

    final int _conns;
    final long _period;
    final TimeUnit _unit;


    public FixedRateWorkload(final int i,
                             final long period,
                             final TimeUnit unit) {
        _conns = i;
        _period = period;
        _unit = unit;
    }


    /** {@inheritDoc} */
    @Override
    public Future<Void> execute(final Generator<Void> command) {

        // This executor runs exactly n times.
        final ScheduledExecutorService workloadGenerator =
            new ScheduledThreadPoolExecutor(1) {
                AtomicLong l = new AtomicLong();
                /** {@inheritDoc} */
                @Override
                protected void afterExecute(final Runnable r,
                                            final Throwable t) {
                    final long submitted = l.incrementAndGet();
                    if (submitted>=_conns) {
                        shutdown();
                    }
                }
            };

        // FIXME: This future can return before all requests have completed - use a CDL.
        final ScheduledFuture<?> f =
            workloadGenerator.scheduleAtFixedRate(
                new GeneratorRunnable<>(
                    command, new NoOpCallback()), 0, _period, _unit);

        return
            new Future<Void> () {

                @Override
                public boolean cancel(final boolean mayInterruptIfRunning) {
                    return f.cancel(mayInterruptIfRunning);
                }

                @Override
                public boolean isCancelled() {
                    return f.isCancelled();
                }

                @Override
                public boolean isDone() {
                    return f.isDone();
                }

                @Override
                public Void get() throws InterruptedException,
                                         ExecutionException {
                    try {
                        f.get();
                    } catch (CancellationException e) {
                        /* No Op */
                    }
                    return null;
                }

                @Override
                public Void get(final long timeout,
                                final TimeUnit unit) throws InterruptedException,
                                                            ExecutionException,
                                                            TimeoutException {
                    try {
                        f.get(timeout, unit);
                    } catch (CancellationException e) {
                        /* No Op */
                    }
                    return null;
                }

            };
    }
}