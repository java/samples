package foo;

import org.apache.commons.lang3.StringUtils;

public class ReverseString{
	public static void main(String... args){
		String reversedString = StringUtils.reverse(args[0]);
		System.out.println(reversedString);
	}
}
