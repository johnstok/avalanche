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

import java.util.Random;


/**
 * A distribution in which each integer is equally likely to occur.
 *
 * @author Keith Webster Johnston.
 */
public class UniformDistribution
    implements
        Distribution {

    private final Random r = new Random(System.nanoTime());

    private final int _min;
    private final int _size;


    /**
     * Constructor.
     *
     * @param min  The minimum value in the distribution.
     * @param size The number of integers in the range.
     */
    public UniformDistribution(final int min, final int size) {
        _min  = min;  // FIXME: Validate >=0
        _size = size; // FIXME: Validate >=0
    }



    /** {@inheritDoc} */
    public int next() {
        return _min+r.nextInt(_size);
    }
}
