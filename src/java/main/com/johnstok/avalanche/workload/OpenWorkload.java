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
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;
import com.johnstok.avalanche.Distribution;
import com.johnstok.avalanche.Generator;
import com.johnstok.avalanche.Workload;

/**
 *
 * A workload where execution of new commands is not blocked by currently
 * executing commands.
 *
 * @author Keith Webster Johnston.
 */
public class OpenWorkload
    implements
        Workload {

    private static final AtomicLong THREAD_ID = new AtomicLong();

    private final int _conns;
    private final Distribution _distribution;


    /**
     * Constructor.
     *
     * @param i            The number of times a command should be executed.
     * @param distribution The distribution of delays between each execution, in milliseconds.
     */
    public OpenWorkload(final int i, final Distribution distribution) {
        _conns = i;
        _distribution = distribution;
    }


    /** {@inheritDoc} */
    public Future<Void> execute(final Generator<Void> command) {
        CountDownLatch cdl = new CountDownLatch(_conns);
        CountDownCallback cdc = new CountDownCallback(cdl);
        Runnable r =
            new DistributedGenerator(_conns, _distribution, command, cdc);
        FutureTask<Void> ft = new FutureTask<Void>(r, null);
        Thread t = new Thread(ft, "Workload-" + THREAD_ID.incrementAndGet());
        t.start();

        return new LatchFuture(cdl);  // FIXME: This future can't be cancelled.
    }


    // TODO: Accept an Executor to allow alternative threading models for invocation.
    // TODO: Document behaviour if generate() throws an exception.
    // TODO: If we exit early signal to the Future that everything is done.
    private static class DistributedGenerator
        implements
            Runnable {

        private final int _conns;
        private final Distribution _distribution;
        private final Generator<Void> _command;
        private final CountDownCallback _cdc;


        public DistributedGenerator(final int conns,
                                    final Distribution distribution,
                                    final Generator<Void> command,
                                    final CountDownCallback cdc) {
            _conns = conns;
            _distribution = distribution;
            _command = command;
            _cdc = cdc;
        }


        /** {@inheritDoc} */
        public void run() {
            for (int i = 0; i < _conns; i++) {
                try {
                    int sleep = _distribution.next();
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    return;
                }
                _command.generate(_cdc);
            }
        }
    }
}