package com.codegemz.elfi.spec;

import com.codegemz.elfi.apicontracts.EmojiType;

import org.junit.*;

import static org.junit.Assert.*;


/**
 * Created by adrobnych on 5/22/15.
 */
public class EmojiTypeTest {

    @Test
    public void shouldReturnEmojiTypeByString(){
        assertEquals(EmojiType.Happy, EmojiType.fromString("Happy"));
    }
}
