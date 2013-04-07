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


/**
 * A distribution that always returns the same value.
 *
 * @author Keith Webster Johnston.
 */
public class FixedDistribution
    implements
        Distribution {

    private final int _value;


    /**
     * Constructor.
     *
     * @param value The value of the distribution.
     */
    public FixedDistribution(final int value) {
        _value = value;  // FIXME: Validate >=0
    }


    /** {@inheritDoc} */
    public int next() {
        return _value;
    }
}
