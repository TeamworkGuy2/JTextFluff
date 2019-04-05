package twg2.text.stringSearch;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.RandomAccess;

/** Utility methods for comparing portions of strings.<br>
 * Includes {@link #startsWithAny(String, String...)} and {@link #endsWithAny(String, String...)}
 * to find common prefixes and suffixes amongst a group of strings.<br>
 * And the complementary {@link #anyStartWith(List, CharSequence)} that searching for
 * any string in a list that starts with a specified sub-string.<br>
 * {@link #compareEqualCount(CharSequence, CharSequence)} to compare how many consecutive
 * characters are equal between two strings.
 * And more comparison and string search methods...
 * @author TeamworkGuy2
 * @since 2014-11-3
 */
public class StringCompare {

	private StringCompare() { throw new AssertionError("cannot instantiate static class StringCompare"); }


	/** Check if the specified string starts with any of a set of prefixes
	 * @param str the main string to compare prefixes to
	 * @param prefixes the set of prefixes to compare to the beginning of {@code str}
	 * @return true if {@code str} starts with any one of the {@code prefixes}
	 */
	public static boolean startsWithAny(String str, final String... prefixes) {
		if(prefixes != null) {
			for(String prefix : prefixes) {
				if(prefix != null && str.startsWith(prefix)) {
					return true;
				}
			}
		}
		return false;
	}


	/** Check if the specified string ends with any of a set of suffixes
	 * @param str the main string to compare suffixes to
	 * @param suffixes the set of suffixes to compare to the end of {@code str}
	 * @return true if {@code str} ends with any one of the {@code suffixes}
	 */
	public static boolean endsWithAny(String str, final String... suffixes) {
		if(suffixes != null) {
			for(String suffix : suffixes) {
				if(suffix != null && str.endsWith(suffix)) {
					return true;
				}
			}
		}
		return false;
	}


	/** Check if the first char array starts with the second char array
	 * @param str the full char array to check, must be equal to or longer than {@code subStr}
	 * @param strOff the offset into {@code str} at which to start comparing characters
	 * @param subStr the sub array to compare to the start of {@code str}
	 * @param subStrOff the offset into {@code subStr} at which to start comparing characters
	 * @return true if {@code str} starts with {@code subStr}, false otherwise
	 */
	public static boolean startsWith(final char[] str, int strOff, final char[] subStr, int subStrOff) {
		int i = strOff + (subStr.length - subStrOff) - 1;
		int k = subStr.length - 1;
		if(i >= str.length) {
			return false;
		}
		while(k >= subStrOff) {
			if(str[i] != subStr[k]) {
				return false;
			}
			k--;
			i--;
		}
		return true;
	}


	/** Returns true if any of the list of strings starts with {@code startStr}
	 * @param strs the list of strings, the start of each is compared to {@code startStr}
	 * @param startStr compare this char sequence to the start of each string
	 * @return true if any of the strings in {@code strs} starts with the contents of {@code startStr},
	 * false if none of the strings start with {@code startStr}
	 */
	public static boolean anyStartWith(List<String> strs, CharSequence startStr) {
		for(int i = 0, len = strs.size(); i < len; i++) {
			if(compareStartsWith(strs.get(i), startStr, 0) == 0) {
				return true;
			}
		}
		return false;
	}


	/** Returns true if any of the list of strings starts with {@code startStr}
	 * @param strs the list of strings, the start of each is compared to {@code startStr}
	 * @param startStr compare this char sequence to the start of each string
	 * @param startStrOffset the offset into the char sequence at which to start comparing characters to strings from {@code strs}
	 * @return true if any of the strings in {@code strs} starts with the contents of {@code startStr},
	 * false if none of the strings start with {@code startStr}
	 */
	public static boolean anyStartWith(List<String> strs, CharSequence startStr, int startStrOffset) {
		for(int i = 0, len = strs.size(); i < len; i++) {
			if(compareStartsWith(strs.get(i), startStr, startStrOffset) == 0) {
				return true;
			}
		}
		return false;
	}


	/** Compare a string to the {@link CharSequence}. Returns 0 if {@code str} starts
	 * with the contents of {@code startStr}.
	 * <pre>
	 * str="b" > startStr="ab"
	 * str="ab" < startStr="b"
	 * str="ab" < startStr="abc"
	 * str="abc" == startStr="ab"
	 * str="abc" == startStr="abc"
	 * </pre>
	 * @param str the string to compare to
	 * @param startStr the char sequence to compare to the beginning of {@code str}
	 * @param startStrOffset the offset into the CharSequence at which to start comparing characters to {@code str}
	 * @return 0 if {@code str} starts with {@code startStr}, greater than 0 if {@code str}
	 * is greater than {@code startStr}, less than 0 if {@code str} is less than {@code startStr}
	 */
	public static int compareStartsWith(String str, CharSequence startStr, int startStrOffset) {
		int strLen = str.length();
		int ssRemLen = startStr.length() - startStrOffset;
		int len = strLen > ssRemLen ? ssRemLen : strLen;
		int k = 0;
		for( ; k < len; k++) {
			char c1 = str.charAt(k);
			char c2 = startStr.charAt(startStrOffset + k);
			if(c1 != c2) {
				return c1 - c2;
			}
		}
		// startStr empty or 0 characters matched and str is not empty, then str is greater
		if((ssRemLen == 0 || k == 0) && strLen != 0) {
			return 1;
		}
		return (k == len && strLen >= ssRemLen ? 0 : strLen - ssRemLen);
	}


	/** Compares a string to a {@link CharSequence}
	 * @param str the string to compare
	 * @param charSeq the char sequence to compare
	 * @return true if the string and char sequence are equal, false otherwise
	 */
	public static boolean equal(String str, CharSequence charSeq) {
		if(str.length() != charSeq.length()) { return false; }

		for(int i = str.length() - 1; i > -1; i--) {
			if(str.charAt(i) != charSeq.charAt(i)) { return false; }
		}
		return true;
	}


	/** Check if portions of two strings are equal
	 * @see #equal(char[], int, char[], int, int)
	 */
	public static boolean equal(String str1, int str1Off, String str2, int str2Off, int len) {
		if(str1Off + len > str1.length() || str2Off + len > str2.length()) {
			return false;
		}
		for(int i = len - 1; i > -1; i--) {
			if(str1.charAt(str1Off + i) != str2.charAt(str2Off + i)) {
				return false;
			}
		}
		return true;
	}


	/** Check if portions of two strings are equal
	 * @param str1 the first char array to compare characters from 
	 * @param str1Off the offset into {@code str1} at which to start comparing characters
	 * @param str2 the second char array to compare characters from
	 * @param str2Off the offset into {@code str2} at which to start comparing characters
	 * @param len the number of characters to compare
	 * @return  true if the sub-portions of {@code str1} and {@code str2} match
	 */
	public static boolean equal(char[] str1, int str1Off, final char[] str2, int str2Off, int len) {
		if(str1Off + len > str1.length) {
			return false;
		}
		for(int i = len - 1; i > -1; i--) {
			if(str1[str1Off + i] != str2[str2Off + i]) {
				return false;
			}
		}
		return true;
	}


	/** Check if {@code src} contains all {@code subStrs}
	 */
	public static boolean containsAll(String src, final String[] subStrs) {
		for(String subStr : subStrs) {
			if(src.indexOf(subStr) == -1) {
				return false;
			}
		}
		return subStrs.length > 0;
	}


	/** Check if {@code src} contains all {@code subStrs}
	 */
	public static boolean containsAll(String src, Iterable<String> subStrs) {
		boolean any = false;
		if(subStrs instanceof RandomAccess && subStrs instanceof List) {
			List<String> strsList = (List<String>)subStrs;
			for(int i = 0, size = strsList.size(); i < size; i++) {
				String s = strsList.get(i);
				any = true;
				if(src.indexOf(s) == -1) {
					return false;
				}
			}
		}
		else {
			for(String subStr : subStrs) {
				any = true;
				if(src.indexOf(subStr) == -1) {
					return false;
				}
			}
		}
		return any;
	}


	/** Check if {@code src} contains all {@code subStrs} ignore case
	 */
	public static boolean containsAllIgnoreCase(String src, final String[] subStrs) {
		Locale locale = Locale.getDefault(); // cached for performance
		String srcUpper = src.toUpperCase(locale);
		for(String subStr : subStrs) {
			if(srcUpper.indexOf(subStr.toUpperCase(locale)) == -1) {
				return false;
			}
		}
		return subStrs.length > 0;
	}


	/** Check if {@code src} contains all {@code subStrs} ignore case
	 */
	public static boolean containsAllIgnoreCase(String src, Iterable<String> subStrs) {
		Locale locale = Locale.getDefault(); // cached for performance
		String srcUpper = src.toUpperCase(locale);
		boolean any = false;
		if(subStrs instanceof RandomAccess && subStrs instanceof List) {
			List<String> strsList = (List<String>)subStrs;
			for(int i = 0, size = strsList.size(); i < size; i++) {
				String s = strsList.get(i);
				any = true;
				if(srcUpper.indexOf(s.toUpperCase(locale)) == -1) {
					return false;
				}
			}
		}
		else {
			for(String subStr : subStrs) {
				any = true;
				if(srcUpper.indexOf(subStr.toUpperCase(locale)) == -1) {
					return false;
				}
			}
		}
		return any;
	}


	/** Check if {@code src} contains any {@code subStrs}
	 */
	public static boolean containsAny(String src, final String[] subStrs) {
		for(String subStr : subStrs) {
			if(src.indexOf(subStr) > -1) {
				return true;
			}
		}
		return false;
	}


	/** Check if {@code src} contains any {@code subStrs}
	 */
	public static boolean containsAny(String src, Iterable<String> subStrs) {
		if(subStrs instanceof RandomAccess && subStrs instanceof List) {
			List<String> strsList = (List<String>)subStrs;
			for(int i = 0, size = strsList.size(); i < size; i++) {
				String s = strsList.get(i);
				if(src.indexOf(s) > -1) {
					return true;
				}
			}
		}
		else {
			for(String subStr : subStrs) {
				if(src.indexOf(subStr) > -1) {
					return true;
				}
			}
		}
		return false;
	}


	/** Check if {@code src} contains any {@code subStrs} ignore case
	 */
	public static boolean containsAnyIgnoreCase(String src, final String[] subStrs) {
		Locale locale = Locale.getDefault(); // cached for performance
		String srcUpper = src.toUpperCase(locale);
		for(String subStr : subStrs) {
			if(srcUpper.indexOf(subStr.toUpperCase(locale)) > -1) {
				return true;
			}
		}
		return false;
	}


	/** Check if {@code src} contains any {@code subStrs} ignore case
	 */
	public static boolean containsAnyIgnoreCase(String src, Iterable<String> subStrs) {
		Locale locale = Locale.getDefault(); // cached for performance
		String srcUpper = src.toUpperCase(locale);

		if(subStrs instanceof RandomAccess && subStrs instanceof List) {
			List<String> strsList = (List<String>)subStrs;
			for(int i = 0, size = strsList.size(); i < size; i++) {
				String s = strsList.get(i);
				if(srcUpper.indexOf(s.toUpperCase(locale)) > -1) {
					return true;
				}
			}
		}
		else {
			for(String subStr : subStrs) {
				if(srcUpper.indexOf(subStr.toUpperCase(locale)) > -1) {
					return true;
				}
			}
		}
		return false;
	}


	/** Count {@code subStrs} contained in {@code src}
	 */
	public static int containsCount(String src, final String[] subStrs) {
		int cnt = 0;
		for(String subStr : subStrs) {
			if(src.indexOf(subStr) > -1) {
				cnt++;
			}
		}
		return cnt;
	}


	/** Count {@code subStrs} contained in {@code src}
	 */
	public static int containsCount(String src, Iterable<String> subStrs) {
		int cnt = 0;
		if(subStrs instanceof RandomAccess && subStrs instanceof List) {
			List<String> strsList = (List<String>)subStrs;
			for(int i = 0, size = strsList.size(); i < size; i++) {
				String s = strsList.get(i);
				if(src.indexOf(s) > -1) {
					cnt++;
				}
			}
		}
		else {
			for(String subStr : subStrs) {
				if(src.indexOf(subStr) > -1) {
					cnt++;
				}
			}
		}
		return cnt;
	}


	/** Count {@code subStrs} contained in {@code src} ignore case
	 */
	public static int containsIgnoreCaseCount(String src, final String[] subStrs) {
		Locale locale = Locale.getDefault(); // cached for performance
		String srcUpper = src.toUpperCase(locale);
		int cnt = 0;
		for(String subStr : subStrs) {
			if(srcUpper.indexOf(subStr.toUpperCase(locale)) > -1) {
				cnt++;
			}
		}
		return cnt;
	}


	/** Count {@code subStrs} contained in {@code src} ignore case
	 */
	public static int containsIgnoreCaseCount(String src, Iterable<String> subStrs) {
		Locale locale = Locale.getDefault(); // cached for performance
		String srcUpper = src.toUpperCase(locale);
		int cnt = 0;
		if(subStrs instanceof RandomAccess && subStrs instanceof List) {
			List<String> strsList = (List<String>)subStrs;
			for(int i = 0, size = strsList.size(); i < size; i++) {
				String s = strsList.get(i);
				if(srcUpper.indexOf(s.toUpperCase(locale)) > -1) {
					cnt++;
				}
			}
		}
		else {
			for(String subStr : subStrs) {
				if(srcUpper.indexOf(subStr.toUpperCase(locale)) > -1) {
					cnt++;
				}
			}
		}
		return cnt;
	}


	/** Check if {@code str} contains {@code searchStr} ignore case
	 */
	public static boolean containsIgnoreCase(String str, String searchStr) {
		return indexOfIgnoreCase(str, 0, str.length(), searchStr, 0, searchStr.length(), 0) > -1;
	}


	/** Check if any {@code strs} equal {@code searchStr} ignore case
	 */
	public static boolean containsEqualIgnoreCase(final String[] strs, String searchStr) {
		for(int i = 0, size = strs.length; i < size; i++) {
			if(strs[i] != null && strs[i].equalsIgnoreCase(searchStr)) {
				return true;
			}
		}
		return false;
	}


	/** Check if any {@code strs} equal {@code searchStr} ignore case
	 */
	public static boolean containsEqualIgnoreCase(Iterable<String> strs, String searchStr) {
		if(strs instanceof RandomAccess && strs instanceof List) {
			List<String> strList = (List<String>)strs;
			for(int i = 0, size = strList.size(); i < size; i++) {
				String s = strList.get(i);
				if(s != null && s.equalsIgnoreCase(searchStr)) {
					return true;
				}
			}
		}
		else {
			for(String s : strs) {
				if(s != null && s.equalsIgnoreCase(searchStr)) {
					return true;
				}
			}
		}
		return false;
	}


	/** Check if any {@code strs} contain {@code searchStr} ignore case
	 */
	public static boolean containsIgnoreCase(final String[] strs, String searchStr) {
		Locale locale = Locale.getDefault(); // cached for performance
		String searchStrUpper = searchStr.toUpperCase(locale);
		for(int i = 0, size = strs.length; i < size; i++) {
			if(strs[i] != null && strs[i].toUpperCase(locale).indexOf(searchStrUpper) > -1) {
				return true;
			}
		}
		return false;
	}


	/** Check if any {@code strs} contain {@code searchStr} ignore case
	 */
	public static boolean containsIgnoreCase(Iterable<String> strs, String searchStr) {
		Locale locale = Locale.getDefault(); // cached for performance
		String searchStrUpper = searchStr.toUpperCase(locale);
		if(strs instanceof RandomAccess && strs instanceof List) {
			List<String> strList = (List<String>)strs;
			for(int i = 0, size = strList.size(); i < size; i++) {
				String s = strList.get(i);
				if(s != null && s.toUpperCase(locale).indexOf(searchStrUpper) > -1) {
					return true;
				}
			}
		}
		else {
			for(String s : strs) {
				if(s != null && s.toUpperCase(locale).indexOf(searchStrUpper) > -1) {
					return true;
				}
			}
		}
		return false;
	}


	/** Find the longest matching to {@code chseq} in the array of search entry key strings
	 * @param chseq the char sequence to find a string that matches
	 * @param chseqOffset the offset into {@code chseq} at which to beginning searching
	 * for a matching string
	 * @param searchEntries an array of {@link java.util.Map.Entry Map.Entries}, search the keys for
	 * a match to {@code chseq}
	 * @param isSorted true if {@code searchStrs} is sorted (use binary search to find match),
	 * false if {@code searchStrs} is not sorted and each entry's key should be compared to {@code chseq}
	 * @return the entry from {@code searchStrs} where the entry's key matches the longest substring
	 * portion of {@code chseq} starting at character index {@code chseqOffset} of {@code chseq}
	 */
	public static <T> Map.Entry<String, T> closestMatch(CharSequence chseq, int chseqOffset, final Map.Entry<String, T>[] searchEntries, boolean isSorted) {
		if(isSorted) {
			int searchSeqLen = 1;
			// Possible matching range in search strings
			int index = -1;
			int lastIndex = -1;
			int searchEntriesLen = searchEntries.length;
			int chseqRemLen = chseq.length() - chseqOffset;
			int greatestEqualCount = 0;
			int greatestEqualCountIndex = -1;
			// Search for the index in the sorted array of search strings where strings beginning
			// with N matching letters of the search string begin
			// e.g. search for "carmichael" by searching for the portion of the sorted search strings
			// array where strings starting with "car" or "c" exist
			do {
				lastIndex = index;
				index = EntriesCopy.binarySearch(searchEntries, chseq.subSequence(chseqOffset, Math.min(chseq.length(), chseqOffset+searchSeqLen)).toString());
				if(index < 0) { index = (-index) - 1; }
				if(index >= searchEntriesLen) {
					index = lastIndex;
					break;
				}
				String searchKey = searchEntries[index].getKey();
				int equalCount = compareEqualCount(chseq, chseqOffset, searchKey, 0);
				if(equalCount > greatestEqualCount) {
					greatestEqualCount = equalCount;
					greatestEqualCountIndex = index;
				}

				if(greatestEqualCountIndex > -1 && equalCount < greatestEqualCount) {
					return searchEntries[greatestEqualCountIndex];
				}
				if(equalCount == chseqRemLen) {
					return searchEntries[index];
				}
				if(equalCount >= searchSeqLen) {
					searchSeqLen = equalCount + 1;
				}
				else {
					index = lastIndex;
					break;
				}
			} while(index > -1);

			if(searchSeqLen > 0 && index > -1 && index < searchEntriesLen) {
				return searchEntries[index];
			}
			return null;
		}
		// If the search string entry array is not sorted
		else {
			// Find the string in the list of search strings that matches the best
			int closestMatchIndex = -1;
			int countEqual = -1;
			int bestCount = 1;
			int chseqRemLen = chseq.length() - chseqOffset;
			for(int index = 0, size = searchEntries.length; index < size; index++) {
				String searchStr = searchEntries[index].getKey();
				countEqual = compareEqualCount(chseq, chseqOffset, searchStr, 0);
				if(countEqual > bestCount) {
					bestCount = countEqual;
					closestMatchIndex = index;

					if(bestCount == chseqRemLen || bestCount == searchStr.length()) {
						return searchEntries[closestMatchIndex];
					}
				}
			}
			return closestMatchIndex > -1 ? searchEntries[closestMatchIndex] : null;
		}
	}


	/** Compare how many characters in the two strings are equal before a pair of
	 * unequal characters are encountered.
	 * @param str1 the first char sequence
	 * @param str2 the second char sequence
	 * @return the number of characters equal between the two char sequences
	 */
	public static int compareEqualCount(CharSequence str1, CharSequence str2) {
		int size = str1.length() > str2.length() ? str2.length() : str1.length();
		for(int i = 0; i < size; i++) {
			if(str1.charAt(i) != str2.charAt(i)) { return i; }
		}
		return size;
	}


	/** Compare how many characters in the two char sequences are equal before a
	 * pair of unequal characters are encountered.
	 * @param str1 the first string
	 * @param offset1 the offset into the first char sequence at which to start
	 * comparing characters
	 * @param str2 the second string
	 * @param offset2 the offset into the second char sequence at which to start
	 * comparing characters
	 * @return the number of characters equal between the two char sequences
	 */
	public static int compareEqualCount(CharSequence str1, int offset1, CharSequence str2, int offset2) {
		return compareEqualCount(str1, offset1, str2, offset2,
				Math.min(str1.length() - offset1, str2.length() - offset2));
	}


	/** Compare how many characters in the two char sequences are equal before a
	 * pair of unequal characters are encountered.
	 * @param str1 the first string
	 * @param offset1 the offset into the first char sequence at which to start
	 * comparing characters
	 * @param str2 the second string
	 * @param offset2 the offset into the second char sequence at which to start
	 * comparing characters
	 * @param length the number of characters to compare between the two char sequences
	 * @return the number of characters equal between the two char sequences
	 */
	public static int compareEqualCount(CharSequence str1, int offset1, CharSequence str2, int offset2, int length) {
		int str1OffLen = str1.length() - offset1;
		int str2OffLen = str2.length() - offset2;
		int minOffLen = str1OffLen > str2OffLen ? str2OffLen : str1OffLen;
		int size = (length > minOffLen ? minOffLen : length) + offset1;

		// Compare characters up to the length of the shorter of the two strings
		for(int i1 = offset1, i2 = offset2; i1 < size; i1++, i2++) {
			if(str1.charAt(i1) != str2.charAt(i2)) {
				return i1 - offset1;
			}
		}
		return size - offset1;
	}


	static int indexOfIgnoreCase(String source, int sourceOffset, int sourceCount,
			String target, int targetOffset, int targetCount, int fromIndex) {
		if (fromIndex >= sourceCount) {
			return (targetCount == 0 ? sourceCount : -1);
		}
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		if (targetCount == 0) {
			return fromIndex;
		}

		int first = Character.toUpperCase((int)target.charAt(targetOffset));
		int max = sourceOffset + (sourceCount - targetCount);

		for (int i = sourceOffset + fromIndex; i <= max; i++) {
			/* Look for first character. */
			if (Character.toUpperCase((int)source.charAt(i)) != first) {
				while (++i <= max && Character.toUpperCase((int)source.charAt(i)) != first);
			}

			/* Found first character, now look at the rest of v2 */
			if (i <= max) {
				int j = i + 1;
				int end = j + targetCount - 1;
				for (int k = targetOffset + 1; j < end && Character.toUpperCase((int)source.charAt(j)) == Character.toUpperCase((int)target.charAt(k)); j++, k++);

				if (j == end) {
					/* Found whole string. */
					return i - sourceOffset;
				}
			}
		}
		return -1;
	}
   
}
