package twg2.text.test;

import org.junit.Test;

import twg2.junitassist.checks.CheckTask;
import twg2.text.stringUtils.StringCheck;

/**
 * @author TeamworkGuy2
 * @since 2015-5-26
 */
public class StringCheckTest {

	@Test
	public void testStringCheck() {

		CheckTask.assertTests(new String[] { "A\nB", " ", "", null  }, new Boolean[] { false, false, true, true }, StringCheck::isNullOrEmpty);

		CheckTask.assertTests(new String[] { "A\nB", " ", "", null  }, new Boolean[] { false, true, true, true }, StringCheck::isNullOrWhitespace);

		CheckTask.assertTests(new String[] { "A\nB", " ", ""  }, new Boolean[] { false, true, true }, StringCheck::isWhitespace);

		CheckTask.assertTests(new String[] { "==A\nB", "-- \t ", "..   -="  }, new Boolean[] { false, true, true }, (str) -> StringCheck.isWhitespace(str.toCharArray(), 2, 3));

		CheckTask.assertTests(new String[] { "==A\nB", "-- \t ", "..   12"  }, new Boolean[] { false, true, true }, (str) -> StringCheck.isWhitespace(str.toCharArray(), 2, 3));

		CheckTask.assertTests(new String[] { "==A\nB", "-- \t ", "..   ab"  }, new Boolean[] { false, true, true }, (str) -> StringCheck.isWhitespace(new StringBuilder(str), 2, 3));

	}

}
