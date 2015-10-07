package command;

import java.util.List;

import constants.CmdParameters;
//import logic.TaskBuddy;
import tds.Task;
import tds.TaskTree;
import ui.UIHelper;

public class CmdUpdate extends Command {

	/*
	 * Constants
	 */
	// Variable constants
	private static final int INPUT_NO_UPDATE = 0;
	private static final int INPUT_DEFAULT_VALUE = -1;
	
	// Message constants
	private static final String MSG_TASKUNSPECIFIED = "Please specify a task name";
	private static final String MSG_TASKNOTFOUND = "Specified task \"%1$s\" not found";
	private static final String MSG_TASKUPDATED = "Updated : \"%1$s\" to \"%2$s\"";
	private static final String MSG_TASKNOTUPDATED = "Empty String. Task not updated";
	private static final String MSG_TASKNOCHANGE = "No changes was made";
	
	private static final String MSG_INVALID_INPUT = "Invalid input";
	
	// Error codes
	/*
	 * private enum ERROR { OK, TASKUNSPECIFIED }
	 */

	/*
	 * Variables for internal use
	 */
	private Task updateTask;
	private String taskName;
	
	//variables for undo
	private String prevTaskName;
	//To be included in later version
	private long prevTaskStartTime;
	private long prevTaskEndTime;

	public CmdUpdate() {

	}

	public CmdUpdate(String taskName) {
		this.taskName = taskName;
	}
	
	@Override
	public String execute() {
		
		taskName = getParameterValue(CmdParameters.PARAM_NAME_TASK_NAME);
		
		if (taskName == null || taskName.equals("")) {
			return MSG_TASKUNSPECIFIED;
		}
		
		List<Task> updateTaskList = searchTask(taskName);
		
		return updateTask(updateTaskList);
	}

	@Override
	public String undo() {
		
		Command update = new CmdUpdate();
		update.setTask(updateTask);
		update.setParameter(prevTaskName, null);
		

		return execute();
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
	
	private String updateTask(List<Task> updateTaskList){
		
		//Case 1: List.size is empty
		if(updateTaskList.isEmpty()){
			return String.format(MSG_TASKNOTFOUND, taskName);
		}
		
		//Case 2: List.size == 1
		if(updateTaskList.size() == 1){
			updateTask = updateTaskList.get(0);
			prevTaskName = updateTask.getName();
			String newTaskName = getNewTaskName(prevTaskName);
			TaskTree.updateName(updateTask, newTaskName);
			
			//check if invalid
			if(newTaskName.equals(prevTaskName)) {
				return MSG_TASKNOCHANGE;
			}
			if(newTaskName.equals("") || newTaskName == null){
				return MSG_TASKNOTUPDATED;
			}
			
			return String.format(MSG_TASKUPDATED, prevTaskName, newTaskName);
		}
		
		//Case 3: List.size > 1
		int input = getUserInput(updateTaskList);
		if(input == INPUT_NO_UPDATE){
			return MSG_TASKNOTUPDATED;
		}else{
			int index = input - 1;
			updateTask = updateTaskList.get(index);
			prevTaskName = updateTask.getName();
		}
		String newTaskName = getNewTaskName(prevTaskName);
		TaskTree.updateName(updateTask, newTaskName);
		
		//check if invalid
		if(newTaskName.equals(prevTaskName)) {
			return MSG_TASKNOCHANGE;
		}
		if(newTaskName.equals("") || newTaskName == null){
			return MSG_TASKNOTUPDATED;
		}
			
		return String.format(MSG_TASKUPDATED, prevTaskName, newTaskName);
	}
	
	//To be refactored
	private int getUserInput(List<Task> updateTaskList){
		
		String output = displayUpdateList(updateTaskList);
		int input = INPUT_DEFAULT_VALUE; 
		
		//TaskBuddy.printMessage(output);
		UIHelper.appendOutput(output);
		
		while(input <= -1 || input > updateTaskList.size()){
			input = processInput(UIHelper.getUserInput());
			if(input <= -1 || input > updateTaskList.size()){
				//TaskBuddy.printMessage(MSG_INVALID_INPUT);
				UIHelper.appendOutput(MSG_INVALID_INPUT);
			}
		}
		
		return input;
	}
	
	private String displayUpdateList(List<Task> updateTaskList){
		
		String output = "";
		
		output = output + updateTaskList.size() +
				" instances of \"" + taskName + "\" found:" + System.lineSeparator() ;
		for(int i=0; i<updateTaskList.size(); i++){
			output = output + (i+1) + ". " + updateTaskList.get(i).getName() + System.lineSeparator();
		}
		output = output + "\"0\" to exit" + System.lineSeparator();
		output = output + System.lineSeparator();
		
		return output;
		
	}
	
	//Method to be refactored if possible (Should not be in CmdSearch)
	private int processInput(String input){
		int inputNumber = INPUT_DEFAULT_VALUE;
			
		if(input == null || input.equals("")){
			return inputNumber;
		}
			
		try{
			inputNumber = Integer.parseInt(input);
		}catch(NumberFormatException e){/*Do nothing*/}
			
		return inputNumber;
	}
	
	private String getNewTaskName(String prevTaskName){
		
		UIHelper.setInput(prevTaskName);
		
		String input = UIHelper.getUserInput().trim();
		
		return input;
	}
	


}
