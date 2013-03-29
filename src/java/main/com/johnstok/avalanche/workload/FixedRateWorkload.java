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
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import com.johnstok.avalanche.Callback;
import com.johnstok.avalanche.Generator;
import com.johnstok.avalanche.GeneratorRunnable;
import com.johnstok.avalanche.Workload;

public class FixedRateWorkload implements Workload {

    private final int _conns;
    private final long _period;
    private final TimeUnit _unit;
    private final CountDownLatch _stopLatch;


    public FixedRateWorkload(final int i,
                             final long period,
                             final TimeUnit unit) {
        _conns = i;
        _period = period;
        _unit = unit;
        _stopLatch = new CountDownLatch(i);
    }


    /** {@inheritDoc} */
    public Future<Void> execute(final Generator<Void> command) {
        final CountDownCallback cdc = new CountDownCallback();

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

        workloadGenerator.scheduleAtFixedRate(
            new GeneratorRunnable<Void>(command, cdc), 0, _period, _unit);

        return new LatchFuture(_stopLatch); // FIXME: This future can't be cancelled.
    }


    final class CountDownCallback
        implements
            Callback {

        /** {@inheritDoc} */
        public void onComplete() {
            _stopLatch.countDown();
        }
    }
}