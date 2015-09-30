package command;

import java.util.List;

import constants.CmdParameters;
import tds.Task;
import tds.TaskTree;

public class CmdDelete extends Command {

	/*
	 * Constants
	 */
	// Variable constants
	private static final int INPUT_NO_DELETE = 0;
	private static final int INPUT_DEFAULT_VALUE = -1;
	
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name";
	private static final String MSG_TASKNOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_TASKDELETED = "Deleted : %1$s";
	private static final String MSG_NOTASKDELETED = "No task deleted";

	// Error codes
	/*
	 * private enum ERROR { OK, TASKUNSPECIFIED }
	 */

	/*
	 * Variables for internal use
	 */
	private Task deleteTask;
	private String taskName;

	public CmdDelete() {

	}

	public CmdDelete(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public String execute() {

		taskName = getParameterValue(CmdParameters.PARAM_NAME_TASK_NAME);

		if (taskName == null || taskName.equals("")) {
			return MSG_TASKUNSPECIFIED;
		}

		List<Task> deleteTaskList = searchTask(taskName);
		
		return deleteTask(deleteTaskList);
		
	}

	@Override
	public String undo() {
		Command add = new CmdAdd();
		add.setTask(deleteTask);
		return add.execute();
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public String[] getRequiredFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_NAME };
	}

	@Override
	public String[] getOptionalFields() {
		return new String[] { CmdParameters.PARAM_NAME_TASK_STARTTIME, CmdParameters.PARAM_NAME_TASK_ENDTIME};
	}
	
	private List<Task> searchTask(String taskName){
		return TaskTree.searchName(taskName);
	}
	
	private String deleteTask(List<Task> deleteTaskList){
		
		//Case 1: List.size is empty
		if(deleteTaskList.isEmpty()){
			return String.format(MSG_TASKNOTFOUND, taskName);
		}
		
		//Case 2: List.size > 2
		if(deleteTaskList.size() > 1){
			int input = getUserInput(deleteTaskList);
			if(input == INPUT_NO_DELETE){
				return MSG_NOTASKDELETED;
			}else{
				deleteTask = deleteTaskList.get(input);
				TaskTree.remove(deleteTask);
				return String.format(MSG_TASKDELETED, taskName);
			}
		}
		
		//Case 3: List.size == 1
		deleteTask = deleteTaskList.get(0);
		TaskTree.remove(deleteTask);
		return String.format(MSG_TASKDELETED, taskName);
	}

	private int getUserInput(List<Task> deleteTaskList){
		
		String output = displayDeleteList(deleteTaskList);
		int input = INPUT_DEFAULT_VALUE; 
		/*
		 * To be coded when UI is available
		 * 
		print(output);
		*0 to exit
		while(not within 0 <= userInput <= deleteTaskList.size()){
			print("Invalid number.");
		}
		
		input = userInput;
		 */
		
		
		return input;
	}
	
	private String displayDeleteList(List<Task> deleteTaskList){
		
		String output = "";
		
		output = output + deleteTaskList.size() +
				" instances of \"" + taskName + "\" found:" + System.lineSeparator() ;
		for(int i=0; i<deleteTaskList.size(); i++){
			output = output + (i+1) + ". " + deleteTaskList.get(i).getName() + System.lineSeparator();
		}
		output = output + "\"delete 0\" to exit" + System.lineSeparator();
		output = output + System.lineSeparator();
		
		return output;
		
	}
	
}
