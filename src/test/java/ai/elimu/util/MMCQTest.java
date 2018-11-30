package ai.elimu.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


/**
 * Created by JRUZ on 30. 11. 2018.
 */
public class MMCQTest {
    @Test
    public void getColorIndex() throws Exception {
        assertThat(MMCQ.getColorIndex(255,255,255),is(269535));
        assertThat(MMCQ.getColorIndex(0,0,0),is(0));
    }

}