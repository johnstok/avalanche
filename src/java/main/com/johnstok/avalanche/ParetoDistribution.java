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
 * A Pareto distribution.
 *
 * <p>http://en.wikipedia.org/wiki/Pareto_distribution
 *
 * @author Keith Webster Johnston.
 */
public class ParetoDistribution
    implements
        Distribution {

    private final Random r = new Random(System.nanoTime());
    private final double _alpha;
    private final int _scale;


    /**
     * Constructor.
     *
     * @param alpha The shape of the distribution.
     * @param scale The minimum possible value of the distribution.
     */
    public ParetoDistribution(final double alpha, final int scale) {
        _alpha = alpha; // TODO: Validate?
        _scale = scale; // TODO: Validate >= 0
    }


    /** {@inheritDoc} */
    public int next() {
        return (int) (_scale * Math.pow(r.nextDouble(), -1.0/_alpha));
    }
}
