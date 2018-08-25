/**
 * 
 */
package com.org.marketplace.util;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.org.marketplace.model.Bid;

/**
 * @author gauravkahadane
 *
 */
public final class BidUtils {
	public static Double getLowestBid(Set<Bid> bidSet) {
		if (bidSet != null) {
			TreeSet<Bid> sortedBid = new TreeSet<Bid>(/* new BidComparator() */);
			sortedBid.addAll(bidSet);
			return sortedBid.first().getBid();
		}
		return (double) 0;
	}

	static class BidComparator implements Comparator<Bid> {

		@Override
		public int compare(Bid o1, Bid o2) {
			if (o1 != null && o2 != null) {
				return o1.getBid().compareTo(o2.getBid());
			} else if (o1 == null) {
				return -1;
			} else if (o2 == null) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
