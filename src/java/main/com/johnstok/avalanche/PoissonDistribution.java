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

import java.util.Random;


/**
 * A Poisson distribution.
 *
 * <p>http://en.wikipedia.org/wiki/Poisson_distribution
 *
 * @author Keith Webster Johnston.
 */
public class PoissonDistribution
    implements
        Distribution {

    private final Random r = new Random(System.nanoTime());
    private final double _L;


    /**
     * Constructor.
     *
     * @param lambda The expected value of the distribution.
     */
    public PoissonDistribution(final double lambda) {
        _L = Math.exp(-lambda);
    }


    /** {@inheritDoc} */
    public int next() {
        double p = 1.0;
        int k = 0;

        do {
          k++;
          p *= r.nextDouble();
        } while (p > _L);

        return k - 1; // k is guaranteed to be >= 0.
    }
}
