package ai.elimu.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by JRUZ on 30. 11. 2018.
 */
public class MailerTest {
    @Test
    public void sendPlainText() throws Exception {
        try {
            Mailer.sendPlainText("jakub.ruzicka@student.tuke.sk", "", "elumi-test", "test-mail", "");
        } catch (Exception e) {
            assertFalse(e != null);
        }

    }

    @Test
    public void sendHtml() throws Exception {
        try {
            Mailer.sendHtml("jakub.ruzicka@student.tuke.sk", "", "elumi-test", "test-mail", "test", "");
        } catch (Exception e) {
            assertFalse(e != null);
        }
    }

    @Test
    public void sendHtmlWithButton() throws Exception {
        try {
            Mailer.sendHtmlWithButton("jakub.ruzicka@student.tuke.sk", "", "elumi-test", "test-mail", "test", "", "link", "http://elimu.ai/");
        } catch (Exception e) {
            assertFalse(e != null);
        }
    }

}