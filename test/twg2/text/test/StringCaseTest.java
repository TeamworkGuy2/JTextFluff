package twg2.text.test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import twg2.text.stringUtils.StringCase;

/** Tests for {@link StringCase}
 * @author TeamworkGuy2
 * @since 2015-0-0
 */
public class StringCaseTest {

	static class StringCases {
		List<String> shouldPass;
		List<String> shouldFail;

		public StringCases(List<String> shouldPass, List<String> shouldFail) {
			this.shouldPass = shouldPass;
			this.shouldFail = shouldFail;
		}

	}


	@Test
	public void stringCaseCheck() {
		StringCases camelCase = new StringCases(list("abc"), list("", "a_c", "Abc"));
		StringCases titleCase = new StringCases(list("AlphaChar"), list("", "alphaChar", "Alpha_Char", "Alpha-Char"));
		StringCases dashCase = new StringCases(list("alpha-char", "-b"), list("", "alphaChar", "Alpha-char", "Alpha-Char"));
		StringCases underscoreLowerCase = new StringCases(list("moon_base", "_b"), list("", "moon_Base", "Moon_base", "Moon_Base"));
		StringCases underscoreTitleCase = new StringCases(list("Nanite_Cloud", "_B"), list("", "nanite_cloud", "Nanite_clound", "nanite_Cloud"));

		toStringCases(camelCase, "camelCase", StringCase::isCamelCase);
		toStringCases(titleCase, "TitleCase", StringCase::isTitleCase);
		toStringCases(dashCase, "dash-case", StringCase::isDashCase);
		toStringCases(underscoreLowerCase, "underscore_lower-case", StringCase::isUnderscoreLowerCase);
		toStringCases(underscoreTitleCase, "Underscore_Upper_Case", StringCase::isUnderscoreTitleCase);
	}


	private static <T> void toStringCases(StringCases cases, String converterName, Function<String, Boolean> converter) {
		convertTest(cases.shouldPass, converterName, converter, true);
		convertTest(cases.shouldFail, converterName, converter, false);
	}


	private static <T> void convertTest(List<String> cases, String converterName, Function<String, T> converter, T expectedResult) {
		for(int i = 0, size = cases.size(); i < size; i++) {
			T res = converter.apply(cases.get(i));
			Assert.assertEquals(converterName + " '" + cases.get(i) + "'", expectedResult, res);
		}
	}


	@Test
	public void stringToCase() {
		String[] strs =                { "abc", "Alpha", "subPar",  "Subpar", "SupPar",  "Al_Cid", "a-b", "at-b-", "-c", "var_val_byte", "A_", "_A", "a", "_" };

		String[] camelCase =           { "abc", "alpha", "subPar",  "subpar", "supPar",  "alCid",  "aB",  "atB",   "C",  "varValByte",   "a",  "a",  "a", "_" };
		String[] titleCase =           { "Abc", "Alpha", "SubPar",  "Subpar", "SupPar",  "AlCid",  "AB",  "AtB",   "C",  "VarValByte",   "A",  "A",  "A", "_" };
		String[] dashCase =            { "abc", "alpha", "sub-par", "subpar", "sup-par", "al-cid", "a-b", "at-b-", "-c", "var-val-byte", "a-", "-a", "a", "-" };
		String[] underscoreLowerCase = { "abc", "alpha", "sub_par", "subpar", "sup_par", "al_cid", "a_b", "at_b_", "_c", "var_val_byte", "a_", "_a", "a", "_" };
		String[] underscoreTitleCase = { "Abc", "Alpha", "Sub_Par", "Subpar", "Sup_Par", "Al_Cid", "A_B", "At_B_", "_C", "Var_Val_Byte", "A_", "_A", "A", "_" };

		for(int i = 0, size = strs.length; i < size; i++) {
			Assert.assertEquals("camelCase " + i, camelCase[i], StringCase.toCamelCase(strs[i]));
			Assert.assertEquals("TitleCase " + i, titleCase[i], StringCase.toTitleCase(strs[i]));
			Assert.assertEquals("dash-case " + i, dashCase[i], StringCase.toDashCase(strs[i]));
			Assert.assertEquals("underscore_lower_case " + i, underscoreLowerCase[i], StringCase.toUnderscoreLowerCase(strs[i]));
			Assert.assertEquals("Underscore_Title_Case " + i, underscoreTitleCase[i], StringCase.toUnderscoreTitleCase(strs[i]));
		}
	}


	private static final List<String> list(String... strs) {
		return Arrays.asList(strs);
	}

}
