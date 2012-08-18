package sm.nntp.client.internal;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	public static String[] splitIntoFirstLineAndRemainder(String response) {
		List<String> lines = splitIntoCrLfTerminatedLines(response);
		
		StringBuffer remainder = new StringBuffer();
		for(int i = 1; i < lines.size(); i++) {
			remainder.append(lines.get(i));
			remainder.append("\r\n");
		}
		
		String[] firstLineAndRemainder = new String[2];
		firstLineAndRemainder[0] = lines.get(0);
		firstLineAndRemainder[1] = remainder.toString();
		return firstLineAndRemainder;
	}

	public static List<String> splitIntoCrLfTerminatedLines(String response) {
		List<String> lines = new ArrayList<String>();
		
		int lastIndex = 0;
		while (true) {
			int nextEol = response.indexOf("\r\n", lastIndex);
			if (nextEol == -1)
			{
				if (lastIndex != response.length())
					lines.add(response.substring(lastIndex));
				break;
			}
			else {
				lines.add(response.substring(lastIndex, nextEol));
				lastIndex = nextEol + 2;
			}
		}
		
		return lines;
	}

	public static String[] splitLineIntoWordAndRemainder(String string) {
		StringBuffer word = new StringBuffer();
		StringBuffer remainder = new StringBuffer();
		
		boolean wordFinished = false;
		boolean separatorFinished = false;
		
		for(int i = 0; i < string.length(); i++)
		{
			char c = string.charAt(i);
			
			if (!wordFinished)
			{
				if (c == ' ' || c == '\t')
					wordFinished = true;
				else
					word.append(c);
			}
			else if (!separatorFinished) {
				if (c != ' ' && c != '\t')
				{
					remainder.append(c);
					separatorFinished = true;
				}
			}
			else {
				remainder.append(c);
			}
		}
		
		String[] arr = new String[2];
		arr[0] = word.toString();
		arr[1] = remainder.toString();
		return arr;
	}

	public static List<String> splitLineIntoWords(String line) {
		List<String> stringList = new ArrayList<String>();
		
		String remainder = line;
		while (true) {
			String[] headAndTail = splitLineIntoWordAndRemainder(remainder);
			if (headAndTail[0].length() == 0)
				break;
			
			stringList.add(headAndTail[0]);
			remainder = headAndTail[1];
		}
		
		return stringList;
	}

	public static String validateNewsgroupNameOrException(String name) {
		// TODO check for control chars, spaces or non-ascii chars
		return name;
	}
	
}
