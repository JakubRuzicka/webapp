package ai.elimu.util;

import ai.elimu.model.enums.Locale;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by JRUZ on 30. 11. 2018.
 */
public class IpaToLetterConverterTest {
    @Test
    public void getLetters() throws Exception {
        String letter = null;
        Locale localeEn = Locale.EN;
        Locale localeEs = Locale.ES;
        Locale localeSw = Locale.SW;
        assertThat(IpaToLetterConverter.getLetters("i",localeEn),is("i"));
        assertThat(IpaToLetterConverter.getLetters("t",localeEn),is("t"));
        assertThat(IpaToLetterConverter.getLetters("ð",localeEn),is("th"));
        assertThat(IpaToLetterConverter.getLetters("i",localeEn),is("i"));
        assertThat(IpaToLetterConverter.getLetters("i",localeSw),is(letter));
        assertThat(IpaToLetterConverter.getLetters("i",localeEs),is(letter));
    }

}