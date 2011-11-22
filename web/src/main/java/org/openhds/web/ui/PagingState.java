package org.openhds.web.ui;


/**
 * Supports paging results by keeping track of the page index and increment.
 * Used with paging support from the data access layer, this enables efficient
 * database access even for large data sets.
 * 
 * @author Brent Atkinson, Dave Roberge
 */
public class PagingState {
	// number of records per page
	private int pageIncrement = 10;
	
	// the current page the pager is on
	// NOTE: the index is NOT necessarily incremented in a sequence
	// i.e. 0, 1, 2, 3, etc.
	// the index is incremented by the pageIncrement
	// i.e. if pageIncrement is 5, then pageIndex is 0, 5, 10, 15, etc.
	// it is really a marker for the current item the pager is on
	private int pageIndex = 0;
	
	// total number of records to be paged
	// NOTE: this should be set prior to calling any paging methods
	private long totalCount = -1;

	/**
	 * 
	 * @return the page increment
	 */
	public int getPageIncrement() {
		return pageIncrement;
	}

	/**
	 * Set the page increment
	 * @param pageIncrement
	 */
	public void setPageIncrement(int pageIncrement) {
		this.pageIncrement = pageIncrement;
	}

	/**
	 * Get the total number of records to be paged
	 * @return total number of records
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * Set the total records for this pager
	 * @param l
	 */
	public void setTotalCount(long l) {
		this.totalCount = l;
	}

	/**
	 * Get the current page index
	 * @return
	 * @throws IllegalStateException if total count is not set, or is set to a negative number
	 */
	public int getPageIndex() {
		// the pager must have the total record count set before paging can occur
		if (totalCount < 0) {
			throw new IllegalStateException(
					"item count is required to page results");
		}
		
		// make sure the page index does not exceed the total count
		if (pageIndex >= totalCount) {
			if (totalCount == 0) {
				// on records to page
				pageIndex = 0;
			} else {
				// in the case where the page index does exceed the total count
				// revert the page index to the last possible index
				long lastPossibleIndex = totalCount - 1;
				double partialPages = lastPossibleIndex / pageIncrement;
				int wholePages = (int) Math.floor(partialPages);
				pageIndex = wholePages * pageIncrement;
			}
		}
		
		return pageIndex;
	}

	/**
	 * Set the page index
	 * @param firstItem
	 */
	public void setPageIndex(int firstItem) {
		this.pageIndex = firstItem;
	}

	/**
	 * Retrieve the last item for the current page
	 * @return
	 */
	public long getLastLogicalIndexOnCurrentPage() {
		getPageIndex();
		return pageIndex + pageIncrement > totalCount ? totalCount : pageIndex
				+ pageIncrement;
	}

	/**
	 * Go to the next page in the data set
	 * Will increment by the value specified in pageIncrement
	 */
	public void nextPage() {
		getPageIndex();
		if (pageIndex + pageIncrement < totalCount) {
			pageIndex += pageIncrement;
		}
	}

	/**
	 * Go back a page in the data set
	 * Will decrement by teh value specified in pageIncrement
	 */
	public void previousPage() {
		getPageIndex();
		pageIndex -= pageIncrement;
		if (pageIndex < 0) {
			pageIndex = 0;
		}
	}
	
	/**
	 * 
	 * @return true if the pager is on the last page in dataset, otherwise false
	 */
	public boolean getOnLastPage() {
		return pageIndex + pageIncrement >= totalCount;
	}
}
