package ai.elimu.util;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by JRUZ on 30. 11. 2018.
 */
public class ChecksumHelperTest {
    /**
     *  MD5 - > http://onlinemd5.com/
     * @throws Exception
     */
    @Test
    public void calculateMD5() throws Exception {

        assertThat(ChecksumHelper.calculateMD5("test1".getBytes(StandardCharsets.UTF_8)),is("5A105E8B9D40E1329780D62EA2265D8A".toLowerCase()));


        assertThat(ChecksumHelper.calculateMD5("Long string lorem ipsum lorem ipsum".getBytes(StandardCharsets.UTF_8)),is("F41C9067C09D730821BCDFF1034064B8".toLowerCase()));


        assertThat(ChecksumHelper.calculateMD5("123123".getBytes(StandardCharsets.UTF_8)),is("4297F44B13955235245B2497399D7A93".toLowerCase()));

    }

}