package org.openhds.webui;

import org.openhds.web.ui.PagingState;

import junit.framework.TestCase;

public class PagingStateTest extends TestCase {
	
	private static final int INDEX_OF_FIRST_RECORD_ON_LAST_PAGE = 12;
	private static final int INDEX_OF_FIRST_ITEM = 0;
	private static final int ITEM_TOTAL_COUNT = 15;
	private static final int ITEMS_PER_PAGE = 4;
	private PagingState pager;
	
	@Override
	public void setUp() {
		pager = new PagingState();
		pager.setTotalCount(ITEM_TOTAL_COUNT);	
		pager.setPageIncrement(ITEMS_PER_PAGE);
	}

	public void testInvalidTotalCount() {
		pager.setTotalCount(-1);
		
		try {
			pager.getPageIndex();
			fail("Pager should fail because of total count was invalid");
		} catch(IllegalStateException e) {
			
		}
	}
	
	public void testPagerDefaultsToZeroIndex() {
		assertEquals(INDEX_OF_FIRST_ITEM, pager.getPageIndex());
	}
	
	public void testLastLogicalIndexOnFirstPage() {
		assertEquals(ITEMS_PER_PAGE, pager.getLastLogicalIndexOnCurrentPage());
	}

	
	public void testNextPaging() {
		assertFalse(pager.getOnLastPage());
		
		pager.nextPage();
		
		assertEquals(ITEMS_PER_PAGE, pager.getPageIndex());
		assertEquals(ITEMS_PER_PAGE * 2, pager.getLastLogicalIndexOnCurrentPage());
	
		pager.nextPage();
		pager.nextPage();
		
		assertTrue(pager.getOnLastPage());
	}
	
	public void testPreviousPagingDoesNotGoPastFirstPage() {
		pager.previousPage();
		
		assertEquals(INDEX_OF_FIRST_ITEM, pager.getPageIndex());
	}
	
	public void testNextPagingingDoesNotGoFurtherThanLastPage() {
		pager.nextPage();
		pager.nextPage();
		pager.nextPage();
		pager.nextPage();
		pager.nextPage();
		
		assertEquals(INDEX_OF_FIRST_RECORD_ON_LAST_PAGE, pager.getPageIndex());
		assertEquals(ITEM_TOTAL_COUNT, pager.getLastLogicalIndexOnCurrentPage());
		assertTrue(pager.getOnLastPage());
	}
	
	public void testMoveToTheFinalPage() {
		movePagerToFinalPage();
		
		assertEquals(ITEM_TOTAL_COUNT - ITEMS_PER_PAGE + 1, pager.getPageIndex()); // off by 1 because the indexing starts at 0
		assertEquals(ITEM_TOTAL_COUNT, pager.getLastLogicalIndexOnCurrentPage());
		assertTrue(pager.getOnLastPage());
	}

	private void movePagerToFinalPage() {
		pager.setPageIndex((int)pager.getTotalCount());
	}
	
	public void testMoveToTheFirstPageFromLastPage() {
		movePagerToFinalPage();
		pager.setPageIndex(INDEX_OF_FIRST_ITEM);
		
		assertEquals(INDEX_OF_FIRST_ITEM, pager.getPageIndex());
		assertEquals(ITEMS_PER_PAGE, pager.getLastLogicalIndexOnCurrentPage());
		assertFalse(pager.getOnLastPage());
	}
}
