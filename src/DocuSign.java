import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DocuSign {
	
	//Two HashMaps to store hot weather and cold weather responses.
	//Key-value : Responses id number , and response description
	HashMap<Integer, String> hotResponses = null;
	HashMap<Integer, String> coldResponses = null;
	
	//Populate Map for hot responses
	void initiateHotResponses(){
		hotResponses = new HashMap<Integer, String>();
		hotResponses.put(1, "sandals");
		hotResponses.put(2, "sun visor");
		hotResponses.put(4, "t-shirt");
		hotResponses.put(6, "shorts");
		hotResponses.put(7, "leaving house");
		hotResponses.put(8, "Removing PJs");
	}
	
	//Populate Map for cold responses
	void initiateColdResponses(){
		coldResponses = new HashMap<Integer, String>();
		coldResponses.put(1, "boots");
		coldResponses.put(2, "hat");
		coldResponses.put(3, "socks");
		coldResponses.put(4, "shirt");
		coldResponses.put(5, "jacket");
		coldResponses.put(6, "pants");
		coldResponses.put(7, "leaving house");
		coldResponses.put(8, "Removing PJs");
	}
	
	/*Function to convert String input to List*/
	List<Integer> getCommandList(String inputCommands){
		inputCommands = inputCommands.replaceAll(" ", "");
		List<Integer> commandList = new ArrayList<Integer>();
		String[] commands = inputCommands.split(",");
		for (String command : commands) {
			int val = Integer.parseInt(command);
			commandList.add(val);
		}
		return commandList;
	}
	
	/*Function to start executing commands
	 * Parameters :  String input
	 * Returns String output which is command description if all the commands are valid, 
	 * or the return string will indicate Failure for invalid clothing choice.*/
	String executeCommands(String input){
		
		String weather = input.substring(0, input.indexOf(' '));
		String inputCommands = input.substring(input.indexOf(' ')+1, input.length());
		
		List<Integer> commandList = getCommandList(inputCommands);
		
		HashMap<Integer, String> currentWeatherResponses = null;
		boolean[] repeatRecord = new boolean[9];
		
		if(weather.equalsIgnoreCase("hot"))
			currentWeatherResponses = hotResponses;
		else if(weather.equalsIgnoreCase("cold"))
			currentWeatherResponses = coldResponses;
		else{
			return "fail";
		}
		
		if(commandList.get(0) != 8)
			return "fail";
		
		StringBuffer sb = new StringBuffer();
		
		//Uppend clothing in sequence, append "Fail" if invalid
		for(int i= 0; i<commandList.size(); i++ ){
			int command = commandList.get(i);
			if(repeatRecord[command]){
				sb.append("fail");
				break;
			}else{
				if(checkSpecialCommands(weather, command, repeatRecord)){
					repeatRecord[command] = true;
					sb.append(currentWeatherResponses.get(command));
					if(i != commandList.size()-1)
						sb.append(", ");
				}else{
					sb.append("fail");
					break;
				}
			}
		}
		
		return sb.toString();
	}
	
	//Check cases for special requirements for Hot or Cold weathers 
	boolean checkSpecialCommands(String weather, int currentCommand, boolean[] repeatRecord){
		if(currentCommand <1 || currentCommand > 8){
			System.out.println("Invalid Command: "+currentCommand);
			return false;
		}
			
		if((currentCommand == 3 || currentCommand == 5) && weather.equalsIgnoreCase("hot"))
			return false;
		
		if(currentCommand == 1 && !repeatRecord[3] && !repeatRecord[6])
			return false;
		
		if((currentCommand == 2 || currentCommand == 5 ) && !repeatRecord[4])
			return false;
		
		if(currentCommand == 7){
			int count = 0;
			for(boolean val : repeatRecord){
				if(val)
					count++;
			}
			
			if(weather.equalsIgnoreCase("hot") && count != 5)
				return false;
			else if(weather.equalsIgnoreCase("cold") && count !=7)
				return false;
		}
		
		return true;
	}
	
	/*Function to unit test
	 * Parameters - input for the test in String, expected output in String
	 * Returns boolean value - True: if output matches with expected, False: if output is different than expected.*/
	boolean testInputs(String input, String expectedOutput){
		System.out.println("Input:"+input);
		System.out.println("Expected:"+expectedOutput);
		//Function call to get output from logic
		String output = executeCommands(input);
		System.out.println("Actual:"+output);
		if(output.equalsIgnoreCase(expectedOutput))
			return true;
		
		return false;
	}

	public static void main(String[] args) {
		
		DocuSign ds = new DocuSign();
		ds.initiateHotResponses();
		ds.initiateColdResponses();
		
		/*Command line input code*/
		
//		Scanner sc = new Scanner(System.in);
//		String input = sc.nextLine();
//		
//		System.out.println(ds.executeCommands(input));
		
		String input1 = "HOT 8, 6, 4, 2, 1, 7";
		String expectedOutput1 = "Removing PJs, shorts, t-shirt, sun visor, sandals, leaving house";
		System.out.println("Test 1's Result: "+(ds.testInputs(input1, expectedOutput1) == true? "PASS" : "FAIL"));
		System.out.println();
		
		String input2 = "COLD 8, 6, 3, 4, 2, 5, 1, 7";
		String expectedOutput2 = "Removing PJs, pants, socks, shirt, hat, jacket, boots, leaving house";
		System.out.println("Test 2's Result: "+(ds.testInputs(input2, expectedOutput2) == true? "PASS" : "FAIL"));
		System.out.println();
		
		String input3 = "HOT 8, 6, 6";
		String expectedOutput3 = "Removing PJs, shorts, fail";
		System.out.println("Test 3's Result: "+(ds.testInputs(input3, expectedOutput3) == true? "PASS" : "FAIL"));
		System.out.println();
		
		String input4 = "HOT 8, 6, 3";
		String expectedOutput4 = "Removing PJs, shorts, fail";
		System.out.println("Test 4's Result: "+(ds.testInputs(input4, expectedOutput4) == true? "PASS" : "FAIL"));
		System.out.println();
		
		String input5 = "COLD 8, 6, 3, 4, 2, 5, 7";
		String expectedOutput5 = "Removing PJs, pants, socks, shirt, hat, jacket, fail";
		System.out.println("Test 5's Result: "+(ds.testInputs(input5, expectedOutput5) == true? "PASS" : "FAIL"));
		System.out.println();
		
		String input6 = "COLD 6";
		String expectedOutput6 = "fail";
		System.out.println("Test 6's Result: "+(ds.testInputs(input6, expectedOutput6) == true? "PASS" : "FAIL"));
		System.out.println();
		
//		sc.close();
	}

}
