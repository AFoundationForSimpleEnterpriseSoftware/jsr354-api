/*
 *  Copyright (c) 2012, 2013, Credit Suisse (Anatole Tresch), Werner Keil.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javax.money.function;

import java.util.Arrays;

import javax.money.MonetaryAmount;
import javax.money.MonetaryFunction;

/**
 * This class allows to calculate the minimum of some {@link MonetaryAmount}
 * instances.
 * 
 * @author Anatole Tresch
 */
final class Minimum implements
		MonetaryFunction<Iterable<? extends MonetaryAmount>, MonetaryAmount> {

	/**
	 * Private constructor, there is only one instance of this class, accessible
	 * calling {@link #of()}.
	 */
	Minimum() {
	}

	/**
	 * Evaluates the minimum of the given amounts.
	 * 
	 * @param amounts
	 *            The amounts, at least one instance, not null, all of the same
	 *            currency.
	 * @return the minimum.
	 */
	public MonetaryAmount apply(Iterable<? extends MonetaryAmount> amounts) {
		if (amounts == null) {
			throw new IllegalArgumentException("amounts required.");
		}
		MonetaryAmount result = null;
		for (MonetaryAmount amount : amounts) {
			if (result == null) {
				result = amount;
			} else if (result.isGreaterThan(amount)) {
				result = amount;
			}
		}
		if (result == null) {
			throw new IllegalArgumentException("amounts is empty.");
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Minimum [Iterable<MonetaryAmount> -> MonetaryAmount]";
	}

}
