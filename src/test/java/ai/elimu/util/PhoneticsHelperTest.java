package ai.elimu.util;

import ai.elimu.model.content.Word;
import ai.elimu.model.enums.Locale;
import ai.elimu.model.enums.content.allophone.SoundType;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PhoneticsHelperTest {
    @Test
    public void getSoundTypeEn() throws Exception {
        Locale locale = Locale.EN;
        SoundType soundType = null;
        assertThat(PhoneticsHelper.getSoundType("i",locale),is(soundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("t",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ð",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("n",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("d",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ɪ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("æ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("u",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("ɛɪ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("s",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("r",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("m",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ʌ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("l",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ɔ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("z",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("h",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ɛ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("k",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ɑ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("g",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ɑɪ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("b",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ə",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("p",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ʊ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("f",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("aʊ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("j",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ʃ",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("v",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("əʊ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("ŋ",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("θ",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("dʒ",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ɔɪ",locale),is(SoundType.VOWEL));
        assertThat(PhoneticsHelper.getSoundType("r",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("tʃ",locale),is(SoundType.CONSONANT));
        assertThat(PhoneticsHelper.getSoundType("ʒ",locale),is(SoundType.CONSONANT));
        
    }

    @Test
    public void testGetAllophones_localeEN() {
        Word word = new Word();
        word.setLocale(Locale.EN);
        word.setText("zero");
        word.setPhonetics("ˈzɪrɔʊ");
        List<String> allophones = PhoneticsHelper.getAllophones(word);
        assertThat(allophones.get(0), is("z"));
        assertThat(allophones.get(1), is("ɪ"));
        assertThat(allophones.get(2), is("r"));
        assertThat(allophones.get(3), is("ɔ"));
        assertThat(allophones.get(4), is("ʊ"));
        
        word.setText("one");
        word.setPhonetics("wʌn");
        allophones = PhoneticsHelper.getAllophones(word);
        assertThat(allophones.get(0), is("w"));
        assertThat(allophones.get(1), is("ʌ"));
        assertThat(allophones.get(2), is("n"));
        
        word.setText("five");
        word.setPhonetics("fɑɪv");
        allophones = PhoneticsHelper.getAllophones(word);
        assertThat(allophones.get(0), is("f"));
        assertThat(allophones.get(1), is("ɑɪ"));
        assertThat(allophones.get(2), is("v"));
        
        word.setText("eight");
        word.setPhonetics("ɛɪt");
        allophones = PhoneticsHelper.getAllophones(word);
        assertThat(allophones.get(0), is("ɛɪ"));
        assertThat(allophones.get(1), is("t"));
    }
    
    @Test
    public void testGetAllophones_localeSW() {
        Word word = new Word();
        word.setLocale(Locale.SW);
        word.setText("sifuri");
        word.setPhonetics("siˈfuɾi");
        List<String> allophones = PhoneticsHelper.getAllophones(word);
        assertThat(allophones.get(0), is("s"));
        assertThat(allophones.get(1), is("i"));
        assertThat(allophones.get(2), is("f"));
        assertThat(allophones.get(3), is("u"));
        assertThat(allophones.get(4), is("ɾ"));
        assertThat(allophones.get(5), is("i"));
        
        word.setText("moja");
        word.setPhonetics("ˈmɔjɑ");
        allophones = PhoneticsHelper.getAllophones(word);
        assertThat(allophones.get(0), is("m"));
        assertThat(allophones.get(1), is("ɔ"));
        assertThat(allophones.get(2), is("j"));
        assertThat(allophones.get(3), is("ɑ"));
        
        word.setText("mbili");
        word.setPhonetics("mˈbili");
        allophones = PhoneticsHelper.getAllophones(word);
        assertThat(allophones.get(0), is("mb"));
        assertThat(allophones.get(1), is("i"));
        assertThat(allophones.get(2), is("l"));
        assertThat(allophones.get(3), is("i"));
        
        word.setText("nne");
        word.setPhonetics("nˈnɛ");
        allophones = PhoneticsHelper.getAllophones(word);
        assertThat(allophones.get(0), is("n"));
        assertThat(allophones.get(1), is("n"));
        assertThat(allophones.get(2), is("ɛ"));
    }

}
