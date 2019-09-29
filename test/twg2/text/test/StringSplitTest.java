package twg2.text.test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import twg2.junitassist.checks.CheckTask;
import twg2.text.stringUtils.StringSplit;

/**
 * @author TeamworkGuy2
 * @since 2015-5-11
 */
public class StringSplitTest {


	static class StrOffLen {
		String pattern;
		String str;
		int off;
		int len;


		public static final StrOffLen of(String str, String pattern) {
			return of(str, 0, str.length(), pattern);
		}


		public static final StrOffLen of(String str, int off, int len, String pattern) {
			StrOffLen s = new StrOffLen();
			s.pattern = pattern;
			s.str = str;
			s.off = off;
			s.len = len;
			return s;
		}

	}




	static class CharsOffLen {
		String pattern;
		char[] chars;
		int off;
		int len;


		public static final CharsOffLen of(String str, String pattern) {
			return of(str.toCharArray(), 0, str.length(), pattern);
		}


		public static final CharsOffLen of(char[] chs, int off, int len, String pattern) {
			CharsOffLen s = new CharsOffLen();
			s.pattern = pattern;
			s.chars = chs;
			s.off = off;
			s.len = len;
			return s;
		}

	}



	@Test
	public void stringSplitTest() {
		String[] matches = new String[] {
				"a string without any matching values except at the end //",
				"something//split into pieces//3rd part//so",
				"//beginning split",
				"////split////",
				"",
				"//a//b//c//d//e//f//g",
				"1 2",
				"//",
				"5//",
		};

		for(String match : matches) {
			Assert.assertArrayEquals(match.split("//", 5), StringSplit.split(match, "//", 5));
		}
	}


	@Test
	public void stringStringSplitDst() {
		String str = "something//split into pieces//3rd part//so";
		List<String> strSplit3 = Arrays.asList("something", "split into pieces", "3rd part//so");

		// test String pattern, dst List<String>
		List<String> dstList = new ArrayList<>();
		StringSplit.split(str, "//", dstList);
		Assert.assertEquals(Arrays.asList(str.split("//")), dstList);

		// test String pattern, dst List<String>, limit
		dstList.clear();
		StringSplit.split(str, "//", 3, dstList);
		Assert.assertEquals(strSplit3, dstList);

		// test String pattern, dst String[], large enough
		String[] dstAry = new String[4];
		StringSplit.split(str, "//", dstAry);
		Assert.assertArrayEquals(str.split("//"), dstAry);

		// test String pattern, dst String[], not large enough
		dstAry = new String[3];
		StringSplit.split(str, "//", dstAry);
		Assert.assertArrayEquals(strSplit3.toArray(new String[3]), dstAry);
	}


	@Test
	public void stringCharSplitDst() {
		String str = "something#split into pieces#3rd part#so";
		List<String> strSplit3 = Arrays.asList("something", "split into pieces", "3rd part#so");

		// test char pattern
		List<String> dstList = StringSplit.split(null, '#');
		Assert.assertEquals(0, dstList.size());

		dstList = StringSplit.split(str, '#');
		Assert.assertEquals(Arrays.asList(str.split("#")), dstList);

		// test char pattern, dst List<String>
		dstList = new ArrayList<>();
		StringSplit.split(str, '#', dstList);
		Assert.assertEquals(Arrays.asList(str.split("#")), dstList);

		// test char pattern, dst List<String>, limit
		dstList = new ArrayList<>();
		StringSplit.split(str, '#', 3, dstList);
		Assert.assertEquals(strSplit3, dstList);

		// test char pattern, dst String[], large enough
		String[] dstAry = new String[4];
		StringSplit.split(str, '#', dstAry);
		Assert.assertArrayEquals(str.split("#"), dstAry);

		// test char pattern, dst String[], not large enough
		dstAry = new String[3];
		StringSplit.split(str, '#', dstAry);
		Assert.assertArrayEquals(strSplit3.toArray(new String[3]), dstAry);
	}


	@Test
	public void stringSplitNthChildTest() {
		Assert.assertEquals(" def", StringSplit.findNthMatch("abc, def, ghi", ",", 1, 3));

		CheckTask.assertException(() -> StringSplit.findNthMatch("abc, def, ghi", ",", 1, 2));
		CheckTask.assertException(() -> StringSplit.findNthMatch("abc, def, ghi", ",", 1, 4));
	}


	@Test
	public void countStringMatchesTest() {
		Assert.assertEquals(2, StringSplit.countMatches("abc, def, ghi", ","));
		Assert.assertEquals(3, StringSplit.countMatches("aaa", "a"));
		Assert.assertEquals(3, StringSplit.countMatches("aaaaaa", "aa"));
		Assert.assertEquals(0, StringSplit.countMatches("", "-"));
		Assert.assertEquals(0, StringSplit.countMatches("123", "-"));
		Assert.assertEquals(1, StringSplit.countMatches("-", "-"));
	}


	@Test
	public void countCharMatchesTest() {
		CharsOffLen[] datas = new CharsOffLen[] {
			CharsOffLen.of("abc, def, ghi", ","),
			CharsOffLen.of("aaa", "a"),
			CharsOffLen.of("aaaaaa", "aa"),
			CharsOffLen.of("", "-"),
			CharsOffLen.of("123", "-"),
			CharsOffLen.of("-", "-"),
		};
		Integer[] expected = new Integer[] {
			2, 3, 3, 0, 0, 1
		};

		CheckTask.assertTests(datas, expected, (s) -> StringSplit.countMatches(s.chars, s.off, s.len, s.pattern.toCharArray(), 0, s.pattern.length()));
		CheckTask.assertTests(datas, expected, (s) -> StringSplit.countMatches(chars("~~~", s.chars), 3 + s.off, s.len, s.pattern.toCharArray(), 0, s.pattern.length()));
	}


	@Test
	public void firstMatchTest() {
		Assert.assertEquals("abc", StringSplit.firstMatch("abc-def-ghi", "-"));
		Assert.assertEquals("abc", StringSplit.firstMatch("abc--2--3", "-"));
		Assert.assertEquals("", StringSplit.firstMatch("", "-"));
		Assert.assertEquals("", StringSplit.firstMatch("-a-", "-"));
		Assert.assertEquals("Aa", StringSplit.firstMatch("Aa--Bb--Cc", "--"));

		Assert.assertEquals("abc", StringSplit.firstMatch("abc-def-ghi", '-'));
		Assert.assertEquals("abc", StringSplit.firstMatch("abc--2--3", '-'));
		Assert.assertEquals("", StringSplit.firstMatch("", '-'));
		Assert.assertEquals("", StringSplit.firstMatch("-a-", '-'));
	}


	@Test
	public void postFirstMatchTest() {
		Assert.assertEquals("def-ghi", StringSplit.postFirstMatch("abc-def-ghi", "-"));
		Assert.assertEquals("-2--3", StringSplit.postFirstMatch("abc--2--3", "-"));
		Assert.assertEquals("", StringSplit.postFirstMatch("", "-"));
		Assert.assertEquals("a-", StringSplit.postFirstMatch("-a-", "-"));
		Assert.assertEquals("Bb--Cc", StringSplit.postFirstMatch("Aa--Bb--Cc", "--"));

		Assert.assertEquals("def-ghi", StringSplit.postFirstMatch("abc-def-ghi", '-'));
		Assert.assertEquals("-2--3", StringSplit.postFirstMatch("abc--2--3", '-'));
		Assert.assertEquals("", StringSplit.postFirstMatch("", '-'));
		Assert.assertEquals("a-", StringSplit.postFirstMatch("-a-", '-'));
	}


	@Test
	public void firstMatchPartsTest() {
		Assert.assertEquals(entry("abc", "def-ghi"), StringSplit.firstMatchParts("abc-def-ghi", "-"));
		Assert.assertEquals(entry("abc", "-2--3"), StringSplit.firstMatchParts("abc--2--3", "-"));
		Assert.assertEquals(entry("", ""), StringSplit.firstMatchParts("", "-"));
		Assert.assertEquals(entry("", "a-"), StringSplit.firstMatchParts("-a-", "-"));
		Assert.assertEquals(entry("abc", ""), StringSplit.firstMatchParts("abc", "-"));
		Assert.assertEquals(entry("Aa", "Bb--Cc"), StringSplit.firstMatchParts("Aa--Bb--Cc", "--"));

		Assert.assertEquals(entry("abc", "def-ghi"), StringSplit.firstMatchParts("abc-def-ghi", '-'));
		Assert.assertEquals(entry("abc", "-2--3"), StringSplit.firstMatchParts("abc--2--3", '-'));
		Assert.assertEquals(entry("", ""), StringSplit.firstMatchParts("", '-'));
		Assert.assertEquals(entry("", "a-"), StringSplit.firstMatchParts("-a-", '-'));
		Assert.assertEquals(entry("abc", ""), StringSplit.firstMatchParts("abc", '-'));
	}


	@Test
	public void lastMatchTest() {
		Assert.assertEquals("ghi", StringSplit.lastMatch("abc-def-ghi", "-"));
		Assert.assertEquals("3", StringSplit.lastMatch("abc--2--3", "-"));
		Assert.assertEquals("", StringSplit.lastMatch("", "-"));
		Assert.assertEquals("", StringSplit.lastMatch("a-", "-"));
		Assert.assertEquals("Cc", StringSplit.lastMatch("Aa--Bb--Cc", "--"));

		Assert.assertEquals("ghi", StringSplit.lastMatch("abc-def-ghi", '-'));
		Assert.assertEquals("3", StringSplit.lastMatch("abc--2--3", '-'));
		Assert.assertEquals("", StringSplit.lastMatch("", '-'));
		Assert.assertEquals("", StringSplit.lastMatch("a-", '-'));
	}


	@Test
	public void preLastMatchTest() {
		Assert.assertEquals("abc-def", StringSplit.preLastMatch("abc-def-ghi", "-"));
		Assert.assertEquals("abc--2-", StringSplit.preLastMatch("abc--2--3", "-"));
		Assert.assertEquals("", StringSplit.preLastMatch("", "-"));
		Assert.assertEquals("a", StringSplit.preLastMatch("a-", "-"));
		Assert.assertEquals("Aa--Bb", StringSplit.preLastMatch("Aa--Bb--Cc", "--"));

		Assert.assertEquals("abc-def", StringSplit.preLastMatch("abc-def-ghi", '-'));
		Assert.assertEquals("abc--2-", StringSplit.preLastMatch("abc--2--3", '-'));
		Assert.assertEquals("", StringSplit.preLastMatch("", '-'));
		Assert.assertEquals("a", StringSplit.preLastMatch("a-", '-'));
	}


	@Test
	public void lastMatchPartsTest() {
		Assert.assertEquals(entry("abc-def", "ghi"), StringSplit.lastMatchParts("abc-def-ghi", "-"));
		Assert.assertEquals(entry("abc--def", "ghi"), StringSplit.lastMatchParts("abc--def--ghi", "--"));
		Assert.assertEquals(entry("", ""), StringSplit.lastMatchParts("", "-"));
		Assert.assertEquals(entry("a", ""), StringSplit.lastMatchParts("a-", "-"));
		Assert.assertEquals(entry("abc", ""), StringSplit.lastMatchParts("abc", "-"));
	}


	@Test
	public void substringCharTest() {
		Assert.assertEquals("", StringSplit.substring("-[][]", '[', ']')); // empty result string
		Assert.assertEquals("a", StringSplit.substring("[a]-[b]", '[', ']')); // multi-simple
		Assert.assertEquals("a", StringSplit.substring("a]-", '[', ']')); // no start
		Assert.assertEquals("b", StringSplit.substring("a[b", '[', ']')); // no end
		Assert.assertEquals("ab", StringSplit.substring("ab", '[', ']')); // no start AND no end
		Assert.assertEquals("", StringSplit.substring("", '[', ']')); // empty string
		Assert.assertEquals("-a", StringSplit.substring("-a]-b]-", '[', ']')); // no start multi
		Assert.assertEquals("b[c", StringSplit.substring("a[b[c", '[', ']')); // no end multi
	}


	@Test
	public void substringNullCharTest() {
		Assert.assertEquals("", StringSplit.substringNull("-[][]", '[', ']')); // empty result string
		Assert.assertEquals("a", StringSplit.substringNull("[a]-[b]", '[', ']')); // multi-simple
		Assert.assertEquals(null, StringSplit.substringNull("a]-", '[', ']')); // no start
		Assert.assertEquals(null, StringSplit.substringNull("a[b", '[', ']')); // no end
		Assert.assertEquals(null, StringSplit.substringNull("ab", '[', ']')); // no start AND no end
		Assert.assertEquals(null, StringSplit.substringNull("", '[', ']')); // empty string
		Assert.assertEquals(null, StringSplit.substringNull("-a]-b]-", '[', ']')); // no start multi
		Assert.assertEquals(null, StringSplit.substringNull("a[b[c", '[', ']')); // no end multi
	}


	@Test
	public void substringThrowsCharTest() {
		Assert.assertEquals("", StringSplit.substringThrows("-[][]", '[', ']')); // empty result string
		Assert.assertEquals("a", StringSplit.substringThrows("[a]-[b]", '[', ']')); // multi-simple
		CheckTask.assertException(() -> StringSplit.substringThrows("a]-", '[', ']')); // no start
		CheckTask.assertException(() -> StringSplit.substringThrows("a[b", '[', ']')); // no end
		CheckTask.assertException(() -> StringSplit.substringThrows("ab", '[', ']')); // no start AND no end
		CheckTask.assertException(() -> StringSplit.substringThrows("", '[', ']')); // empty string
		CheckTask.assertException(() -> StringSplit.substringThrows("-a]-b]-", '[', ']')); // no start multi
		CheckTask.assertException(() -> StringSplit.substringThrows("a[b[c", '[', ']')); // no end multi
	}


	@Test
	public void lastSubstringCharTest() {
		Assert.assertEquals("", StringSplit.lastSubstring("-[][]", '[', ']')); // empty result string
		Assert.assertEquals("b", StringSplit.lastSubstring("[a]-[b]", '[', ']')); // multi-simple
		Assert.assertEquals("a", StringSplit.lastSubstring("a]-", '[', ']')); // no start
		Assert.assertEquals("b", StringSplit.lastSubstring("a[b", '[', ']')); // no end
		Assert.assertEquals("ab", StringSplit.lastSubstring("ab", '[', ']')); // no start AND no end
		Assert.assertEquals("", StringSplit.lastSubstring("", '[', ']')); // empty string
		Assert.assertEquals("-a", StringSplit.lastSubstring("-a]-b]-", '[', ']')); // no start multi
		Assert.assertEquals("c", StringSplit.lastSubstring("a[b[c", '[', ']')); // no end multi
	}


	@Test
	public void lastSubstringNullCharTest() {
		Assert.assertEquals("", StringSplit.lastSubstringNull("-[][]", '[', ']')); // empty result string
		Assert.assertEquals("b", StringSplit.lastSubstringNull("[a]-[b]", '[', ']')); // multi-simple
		Assert.assertEquals(null, StringSplit.lastSubstringNull("a]-", '[', ']')); // no start
		Assert.assertEquals(null, StringSplit.lastSubstringNull("a[b", '[', ']')); // no end
		Assert.assertEquals(null, StringSplit.lastSubstringNull("ab", '[', ']')); // no start AND no end
		Assert.assertEquals(null, StringSplit.lastSubstringNull("", '[', ']')); // empty string
		Assert.assertEquals(null, StringSplit.lastSubstringNull("-a]-b]-", '[', ']')); // no start multi
		Assert.assertEquals(null, StringSplit.lastSubstringNull("a[b[c", '[', ']')); // no end multi
	}


	@Test
	public void lastSubstringThrowsCharTest() {
		Assert.assertEquals("", StringSplit.lastSubstringThrows("-[][]", '[', ']')); // empty result string
		Assert.assertEquals("b", StringSplit.lastSubstringThrows("[a]-[b]", '[', ']')); // multi-simple
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("a]-", '[', ']')); // no start
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("a[b", '[', ']')); // no end
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("ab", '[', ']')); // no start AND no end
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("", '[', ']')); // empty string
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("-a]-b]-", '[', ']')); // no start multi
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("a[b[c", '[', ']')); // no end multi
	}


	@Test
	public void substringStrTest() {
		Assert.assertEquals("", StringSplit.substring("-<[]><[]>", "<[", "]>")); // empty result string
		Assert.assertEquals("a", StringSplit.substring("<[a]>-<[b]>", "<[", "]>")); // multi-simple
		Assert.assertEquals("a", StringSplit.substring("a]>-", "<[", "]>")); // no start
		Assert.assertEquals("b", StringSplit.substring("a<[b", "<[", "]>")); // no end
		Assert.assertEquals("ab", StringSplit.substring("ab", "<[", "]>")); // no start AND no end
		Assert.assertEquals("", StringSplit.substring("", "<[", "]>")); // empty string
		Assert.assertEquals("-a", StringSplit.substring("-a]>-b]>-", "<[", "]>")); // no start multi
		Assert.assertEquals("b<[c", StringSplit.substring("a<[b<[c", "<[", "]>")); // no end multi
	}


	@Test
	public void substringNullStrTest() {
		Assert.assertEquals("", StringSplit.substringNull("-<[]><[]>", "<[", "]>")); // empty result string
		Assert.assertEquals("a", StringSplit.substringNull("<[a]>-<[b]>", "<[", "]>")); // multi-simple
		Assert.assertEquals(null, StringSplit.substringNull("a]>-", "<[", "]>")); // no start
		Assert.assertEquals(null, StringSplit.substringNull("a<[b", "<[", "]>")); // no end
		Assert.assertEquals(null, StringSplit.substringNull("ab", "<[", "]>")); // no start AND no end
		Assert.assertEquals(null, StringSplit.substringNull("", "<[", "]>")); // empty string
		Assert.assertEquals(null, StringSplit.substringNull("-a]>-b]>-", "<[", "]>")); // no start multi
		Assert.assertEquals(null, StringSplit.substringNull("a<[b<[c", "<[", "]>")); // no end multi
	}


	@Test
	public void substringThrowsStrTest() {
		Assert.assertEquals("", StringSplit.substringThrows("-<[]><[]>", "<[", "]>")); // empty result string
		Assert.assertEquals("a", StringSplit.substringThrows("<[a]>-<[b]>", "<[", "]>")); // multi-simple
		CheckTask.assertException(() -> StringSplit.substringThrows("a]>-", "<[", "]>")); // no start
		CheckTask.assertException(() -> StringSplit.substringThrows("a<[b", "<[", "]>")); // no end
		CheckTask.assertException(() -> StringSplit.substringThrows("ab", "<[", "]>")); // no start AND no end
		CheckTask.assertException(() -> StringSplit.substringThrows("", "<[", "]>")); // empty string
		CheckTask.assertException(() -> StringSplit.substringThrows("-a]>-b]>-", "<[", "]>")); // no start multi
		CheckTask.assertException(() -> StringSplit.substringThrows("a<[b<[c", "<[", "]>")); // no end multi
	}


	@Test
	public void lastSubstringStrTest() {
		Assert.assertEquals("", StringSplit.lastSubstring("-<[]><[]>", "<[", "]>")); // empty result string
		Assert.assertEquals("b", StringSplit.lastSubstring("<[a]>-<[b]>", "<[", "]>")); // multi-simple
		Assert.assertEquals("a", StringSplit.lastSubstring("a]>-", "<[", "]>")); // no start
		Assert.assertEquals("b", StringSplit.lastSubstring("a<[b", "<[", "]>")); // no end
		Assert.assertEquals("ab", StringSplit.lastSubstring("ab", "<[", "]>")); // no start AND no end
		Assert.assertEquals("", StringSplit.lastSubstring("", "<[", "]>")); // empty string
		Assert.assertEquals("-a", StringSplit.lastSubstring("-a]>-b]>-", "<[", "]>")); // no start multi
		Assert.assertEquals("c", StringSplit.lastSubstring("a<[b<[c", "<[", "]>")); // no end multi
	}


	@Test
	public void lastSubstringNullStrTest() {
		Assert.assertEquals("", StringSplit.lastSubstring("-<[]><[]>", "<[", "]>")); // empty result string
		Assert.assertEquals("b", StringSplit.lastSubstring("<[a]>-<[b]>", "<[", "]>")); // multi-simple
		Assert.assertEquals(null, StringSplit.lastSubstringNull("a]>-", "<[", "]>")); // no start
		Assert.assertEquals(null, StringSplit.lastSubstringNull("a<[b", "<[", "]>")); // no end
		Assert.assertEquals(null, StringSplit.lastSubstringNull("ab", "<[", "]>")); // no start AND no end
		Assert.assertEquals(null, StringSplit.lastSubstringNull("", "<[", "]>")); // empty string
		Assert.assertEquals(null, StringSplit.lastSubstringNull("-a]>-b]>-", "<[", "]>")); // no start multi
		Assert.assertEquals(null, StringSplit.lastSubstringNull("a<[b<[c", "<[", "]>")); // no end multi
	}


	@Test
	public void lastSubstringThrowsStrTest() {
		Assert.assertEquals("", StringSplit.lastSubstringThrows("-<[]><[]>", "<[", "]>")); // empty result string
		Assert.assertEquals("b", StringSplit.lastSubstringThrows("<[a]>-<[b]>", "<[", "]>")); // multi-simple
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("a]>-", "<[", "]>")); // no start
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("a<[b", "<[", "]>")); // no end
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("ab", "<[", "]>")); // no start AND no end
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("", "<[", "]>")); // empty string
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("-a]>-b]>-", "<[", "]>")); // no start multi
		CheckTask.assertException(() -> StringSplit.lastSubstringThrows("a<[b<[c", "<[", "]>")); // no end multi
	}


	@Test
	public void splitAtBoundary() {
		Assert.assertEquals(Arrays.asList("the", "quick", "sly", "fox"), StringSplit.splitAtBoundary("the quick sly fox", ' ', 5));
		Assert.assertEquals(Arrays.asList("the", "quick", "sly fox"), StringSplit.splitAtBoundary("the quick sly fox", ' ', 8));
		Assert.assertEquals(Arrays.asList("a", "sto", "p b", "c", "def"), StringSplit.splitAtBoundary("a stop b c def", ' ', 3));
		Assert.assertEquals(Arrays.asList("a", "stop", "b c", "def"), StringSplit.splitAtBoundary("a stop b c def", ' ', 4));
	}


	private static <K, V> Map.Entry<K, V> entry(K key, V value) {
		return new AbstractMap.SimpleImmutableEntry<>(key, value);
	}


	private static final char[] chars(String str, char[] chs) {
		return str(str, chs).toCharArray();
	}


	private static final String str(String str, char[] chs) {
		return (str + new String(chs));
	}

}
