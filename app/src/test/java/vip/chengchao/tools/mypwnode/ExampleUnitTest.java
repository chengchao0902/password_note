package vip.chengchao.tools.mypwnode;

import org.junit.Test;

import vip.chengchao.tools.mypwnode.cipher.AES;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testAes() {
        String password = "123456789";
        String string = "12345";
        String aes = AES.encrypt(password, string);
        assertEquals("89776274E93A5813D6953F5BC35C82DC", aes);
        assertEquals(string, AES.decrypt(password, aes));
    }
}