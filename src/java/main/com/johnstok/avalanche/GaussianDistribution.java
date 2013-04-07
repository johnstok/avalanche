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
 * A Gaussian distribution.
 *
 * http://en.wikipedia.org/wiki/Normal_distribution
 *
 * @author Keith Webster Johnston.
 */
public class GaussianDistribution
    implements
        Distribution {

    private final Random r = new Random(System.nanoTime());

    private final int _mean;
    private final int _deviation;


    /**
     * Constructor.
     *
     * <p>Note that if values less than 0 will be coerced to 0.
     *
     * @param mean      The mean value in the distribution.
     * @param deviation The standard deviation of the distribution.
     */
    public GaussianDistribution(final int mean, final int deviation) {
        _mean  = mean;          // FIXME: Validate >=0
        _deviation = deviation; // FIXME: Validate >=0
    }


    /** {@inheritDoc} */
    public int next() {
        int next = _mean+(int)(_deviation*r.nextGaussian());
        return next>=0 ? next : 0;
    }
}
