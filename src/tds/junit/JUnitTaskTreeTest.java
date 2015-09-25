package tds.junit;
import tds.Task;
import tds.TaskTree;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class JUnitTaskTreeTest {
	private static final int FIRST_ELEMENT = 0;
	@SuppressWarnings("unused")
	private static final int LAST_ELEMENT = 4;
	private static final int NUM_OF_ITEMS = 5;
	private static final int FIVE_SECONDS = 5000;
	private String[] names = {
			"Buy mushroom", 
			"Buy milk", 
			"Buy hotdog", 
			"Do homework",
			"Learn swimming"
			};
	private long[] startTimes = {
			0L,
			1L,
			2L,
			3L,
			4L
			};
	private long[] endTimes = {
			0L,			// floating task
			1441119600000L,	// 01 Sep 2015 15:00:00
			1442734205000L, // 20 Sep 2015 07:30:05
			1443112320000L, // 24 Sep 2015 16:32:00
			1444059228000L	// 05 Oct 2015 15:33:48
			};
	private int[] flags = {
			Task.FLAG_DONE,
			Task.FLAG_NULL,
			Task.FLAG_NULL,
			Task.FLAG_NULL,
			Task.FLAG_NULL
			};
	private int[] priorities = {
			Task.PRIORITY_NORMAL,
			Task.PRIORITY_HIGH,
			Task.PRIORITY_NORMAL,
			Task.PRIORITY_VERY_HIGH,
			Task.PRIORITY_NORMAL
			};

	private void testAddElementsToTree(TaskTree taskTree, ArrayList<Task> checkList) {
		Task temp;
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]);
			taskTree.add(temp);
			checkList.add(temp);
		}
	}
	
	@Test
	public void testAddElementsToTree () {
		TaskTree taskTree = new TaskTree();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);
		testAddElementsToTree(taskTree, checkList);
		
		String checkString = "";
		for (Task task : checkList) {
			checkString += task + "\n";
		}
		String resultString = "" + taskTree;
		
		assertEquals(resultString, checkString);
		assertTrue(checkList.equals(new ArrayList<Task>(taskTree.getList())));
	}

	@Test
	public void testBuildTreeFromCollection () {
		Task temp;
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS); 
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			temp = new Task(names[i], startTimes[i], endTimes[i], flags[i], priorities[i]);
			checkList.add(temp);
		}
		TaskTree taskTree = new TaskTree(checkList);
		assertEquals(taskTree.getList(), checkList);
	}
	
	@Test
	public void testRemoveElementsFromTree () {
		TaskTree taskTree = new TaskTree();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);
		testAddElementsToTree(taskTree, checkList);
		
		int listSize = taskTree.size();
		ArrayList<Task> returnList;
		String searchTerm;
		
		// Get original list
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(taskTree.searchName(searchTerm));
		assertEquals(taskTree.size(), listSize);
		
		// Remove last element
		taskTree.remove(returnList.get(returnList.size() - 1));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(taskTree.searchName(searchTerm));
		assertEquals(taskTree.size(), listSize -= 1);
	
		// Remove second element
		taskTree.remove(returnList.get(FIRST_ELEMENT + 1));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(taskTree.searchName(searchTerm));
		assertEquals(taskTree.size(), listSize -= 1);
		
		// Remove last element
		taskTree.remove(returnList.get(FIRST_ELEMENT));
		searchTerm = "Buy";
		returnList = new ArrayList<Task>(taskTree.searchName(searchTerm));
		assertEquals(taskTree.size(), listSize -= 1);
		
		// Remove non-existing element
		assertFalse(taskTree.remove(new Task("Dummy")));
	}
	
	@Test
	public void testQueryTime() {
		TaskTree taskTree = new TaskTree();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);
		testAddElementsToTree(taskTree, checkList);
		
		long lowerBound;
		long upperBound;
		ArrayList<Task> resultList;
		
		// Setting up test cases
		// first item (inclusive) to more than last element (inclusive)
		lowerBound = endTimes[FIRST_ELEMENT];
		upperBound = endTimes[LAST_ELEMENT] + FIVE_SECONDS;
		resultList = new ArrayList<Task>(taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == NUM_OF_ITEMS);
		
		// first item (inclusive) to last element (inclusive)
		lowerBound = endTimes[FIRST_ELEMENT];
		upperBound = endTimes[LAST_ELEMENT];
		resultList = new ArrayList<Task>(taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == NUM_OF_ITEMS);
		
		
		// lowerBound > upperBound
		lowerBound = endTimes[LAST_ELEMENT];
		upperBound = endTimes[FIRST_ELEMENT];
		resultList = new ArrayList<Task>(taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == 0);
		
		// item less than first element (inclusive) to first item (inclusive)
		lowerBound = endTimes[LAST_ELEMENT] - FIVE_SECONDS;
		upperBound = endTimes[LAST_ELEMENT];
		resultList = new ArrayList<Task>(taskTree.queryEndTime(lowerBound, upperBound));
		assertTrue(resultList.size() == 1);
	}
	
	@Test
	public void testSearchFlagAndPriority() {
		TaskTree taskTree = new TaskTree();
		ArrayList<Task> checkList = new ArrayList<Task>(NUM_OF_ITEMS);
		testAddElementsToTree(taskTree, checkList);
		
		int searchInt;
		long lowerBound;
		long upperBound;
		ArrayList<Task> returnList;
		
		// Get done flag
		searchInt = Task.FLAG_DONE;
		returnList = new ArrayList<Task>(taskTree.searchFlag(searchInt));
		assertEquals(returnList.size(), 1);
		
		// Get done flag
		searchInt = Task.FLAG_NULL;
		returnList = new ArrayList<Task>(taskTree.searchFlag(searchInt));
		assertEquals(returnList.size(), 4);
		
		// Query done flag
		lowerBound = Task.FLAG_NULL;
		upperBound = Task.FLAG_DONE;
		returnList = new ArrayList<Task>(taskTree.queryFlag((int)lowerBound,(int)upperBound));
		assertEquals(returnList.size(), 5);
		
		// Query done flag
		searchInt = Task.PRIORITY_HIGH;
		returnList = new ArrayList<Task>(taskTree.queryPriority(searchInt,searchInt));
		assertEquals(returnList.size(), 1);
		
		// Query priority normal
		searchInt = Task.PRIORITY_NORMAL;
		returnList = new ArrayList<Task>(taskTree.searchPriority(searchInt));
		assertEquals(returnList.size(), 3);		
	}
	
	
	@SuppressWarnings("unused")
	private void printList(List<Task> returnList) {
		int index;
		if (returnList.isEmpty()) {
			System.out.println("No item in list");
		} else {
			for (int i = 0; i < returnList.size(); i++) {
				index = i + 1;
				System.out.println("" + index + ". " + returnList.get(i));
			}
		}
		System.out.println();
	}

}
