package ai.elimu.logic.converters;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Created by JRUZ on 30. 11. 2018.
 */
public class StringToNullConverterTest {
    @Test
    public void convert() throws Exception {
        StringToNullConverter convertor = new StringToNullConverter();
        assertNull((convertor.convert("")));
        assertNull((convertor.convert(null)));
        assertThat(convertor.convert("test"),is("test"));
    }

}