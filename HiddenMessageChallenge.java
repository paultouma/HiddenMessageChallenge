import java.util.HashSet;
import java.util.Set;

/**
 * This program computes the complete set of possible substrings from an original string
 * after having sequentially removed a "hidden message" throughout that original string.
 * For example, with an original string = "*-_-***" and "hidden message" = "*-*". The resulting
 * set is "_-**" and "-_**".
 *
 * Compile, run specifications and other information provided in README.md
 * included in the zip file.
 * 
 * @author paultouma
 * 
 */
public class HiddenMessageChallenge {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberOfArgs = args.length;
        
		// check the command line arguments
		if (numberOfArgs == 3) {

			String original = args[0];
			String firstHidden = args[1];
			String secondHidden = args[2];

			// check length
			if (original.length() < 100) {
				System.out.println(removeSecondMessageFromSet(
						removeMessageFromOriginal(original, firstHidden),
						secondHidden).size());
			} else {
				System.out
						.println("Original message needs to be less than 100 Morse Code characters in length");
			}

		} else if (numberOfArgs == 1 && args[0].equals("test")) {
			runAllTests();
		} else {
			System.out.println("Usage: ");
			System.out
					.println("   HiddenMessageChallenge <original_message> <first_hidden_message> <second_hidden_message>");
			System.out.println("   HiddenMessageChallenge test");
		}

	}

	/**
	 * This method removes a hidden message from a original message and returns
	 * the set of all strings after having removed the hidden message from the
	 * original message. This method wraps a call to
	 * recursiveRemoveTokenFromString which does the primary computing.
	 * 
	 * @param originalMessage
	 *            the original message
	 * @param hiddenMessage
	 *            the hidden message to be removed from the original message
	 * @return set of all possible valid strings after having removed the hidden
	 *         message from the original message
	 */
	private static Set<String> removeMessageFromOriginal(
			String originalMessage, String hiddenMessage) {
		if (originalMessage.length() > 0 && hiddenMessage.length() > 0) {
			return recursiveRemoveMessageFromOriginal(originalMessage,
					hiddenMessage);
		} else
			return new HashSet<String>();
	}

	/**
	 * This method recursively removes a hidden message from an original
	 * message, by at each step of the way either appending or not appending the
	 * current token to a subset of strings that is returned by a lower call to
	 * the recursive function
	 * 
	 * @param originalMessage
	 *            the current original message
	 * @param hiddenMessage
	 *            the current hidden message to be removed from the original
	 *            message
	 * @return set of all possible valid strings after having removed the hidden
	 *         message from the original message
	 */
	private static Set<String> recursiveRemoveMessageFromOriginal(
			String originalMessage, String hiddenMessage) {

		Set<String> returnSet = new HashSet<String>();

		int wordLength = originalMessage.length();
		char currentChar = originalMessage.charAt(0);

		// check equality between original and hidden message's first letter
		if (currentChar == hiddenMessage.charAt(0)) {

			// BASE case: if the hidden message is finished being removed, return
			if (hiddenMessage.length() == 1) {
				if (wordLength > 1) {
					returnSet.add(originalMessage.substring(1));
				} else {
					returnSet.add("");
				}
			}

			// otherwise continue by moving along the hidden message's remaining
			// characters
			else {
				if (wordLength > 1) {
					returnSet.addAll(recursiveRemoveMessageFromOriginal(
							originalMessage.substring(1),
							hiddenMessage.substring(1)));
				}
			}

		}

		// also add subset of possible words that involve removing the hidden
		// message to the remaining of the original message
		if (wordLength > 1) {
			returnSet.addAll(mapPrefixToSet(
					currentChar,
					recursiveRemoveMessageFromOriginal(
							originalMessage.substring(1), hiddenMessage)));
		}

		return returnSet;
	}

	/**
	 * This method maps a character prefix and prepends that character to each
	 * word in a set
	 * 
	 * @param prefix
	 *            - character to prepend
	 * @param set
	 *            - the set that to have all words prepended by prefix
	 * @return set of strings with character prepended to all elements
	 */
	private static Set<String> mapPrefixToSet(char prefix, Set<String> set) {

		Set<String> returnSet = new HashSet<String>();

		for (String s : set) {
			returnSet.add(prefix + s);
		}

		return returnSet;
	}

	/**
	 * This method removes the a second hidden message from a set consisting of
	 * strings resulting from removing a first hidden message from an original
	 * message
	 * 
	 * @param set
	 *            - set of strings that have already had their first hidden
	 *            message removed from the original message
	 * @param secondHiddenMessage
	 *            - the second hidden message to be removed
	 * @return the resulting set of valid words that have removed the second
	 *         message as well
	 */
	private static Set<String> removeSecondMessageFromSet(Set<String> set,
			String secondHiddenMessage) {
		Set<String> setToReturn = new HashSet<String>();

		// iterate the set of words
		for (String s : set) {
			// remove the second hidden message from each of the strings, and
			// add the result to a set
			setToReturn
					.addAll(removeMessageFromOriginal(s, secondHiddenMessage));
		}

		return setToReturn;
	}

	/**
	 * Unit Tests that I used along the way.
	 */
	private static void runAllTests() {

		// set used for testing purposes
		Set<String> testSet = new HashSet<String>();

		testSet.add("_-**");
		testSet.add("-_**");

		System.out.println("removeMessageFromOriginal Unit Tests");
		printTestResult(
				removeMessageFromOriginal("*-_-***", "*-*").equals(testSet),
				"AB rem R", testSet);

		printTestResult(removeMessageFromOriginal("", "*-*").equals(testSet),
				"Empty Original", testSet);

		printTestResult(removeMessageFromOriginal("*-_-***", "")
				.equals(testSet), "Emtpy Message", testSet);

		long testStartTime = System.currentTimeMillis();
		printTestResult(
				removeMessageFromOriginal(
						"****_*_*-**_*-**_---___*--_---_*-*_*-**_-**",
						"****_*_*-**_*--*").size() == 1311,
				"Hello World rem Help", testSet);
		long testEndTime = System.currentTimeMillis();

		printTestResult(testEndTime - testStartTime <= 10 * 1000,
				"Hello World rem Help time test", testSet);

		System.out.println("removeSecondMessageFromSet Unit Tests");

		testSet.add("_-**");
		testSet.add("_*-*");
		testSet.add("-_**");
		testSet.add("*_-*");
		testSet.add("*-_*");

		printTestResult(
				removeSecondMessageFromSet(
						removeMessageFromOriginal("*-_-***_-*-*_-**", "***_-"),
						"--**_-*").equals(testSet), "ABCD rem ST rem ZN",
				testSet);

		System.out
				.println("Testing \"The Star Wars Saga\" rem \"Yoda\" rem \"Leia\" (Caution: Takes around 35 seconds)");

		testStartTime = System.currentTimeMillis();
		printTestResult(
				removeSecondMessageFromSet(
						removeMessageFromOriginal(
								"-_****_*___***_-_*-_*-*___*--_*-_*-*_***___***_*-_--*_*-",
								"-*--_---_-**_*-"), "*-**_*_**_*-").size() == 11474,
				"The Star Wars Saga rem Yoda rem Leia", testSet);
		testEndTime = System.currentTimeMillis();

		printTestResult(testEndTime - testStartTime <= 60 * 1000,
				"The Star Wars Saga rem Yoda rem Leia time test", testSet);

	}

	/**
	 * Prints test results
	 * 
	 * @param result
	 *            - boolean value of test result
	 * @param description
	 *            - description of test
	 * @param set
	 *            - the set used in the test to be cleared (for later use in
	 *            other tests)
	 */
	private static void printTestResult(boolean result, String description,
			Set<String> set) {
		if (result) {
			System.out.println("Test Passed: " + description);
		} else {
			System.out.println("Test Failed: " + description);
		}
		set.clear();
	}

}
