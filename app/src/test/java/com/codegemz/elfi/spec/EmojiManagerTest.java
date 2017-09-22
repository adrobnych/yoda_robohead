package com.codegemz.elfi.spec;

import com.codegemz.elfi.coreapp.R;
import com.codegemz.elfi.model.EmojiManager;
import com.codegemz.elfi.apicontracts.EmojiType;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import java.util.Map;


public class EmojiManagerTest {
    private EmojiManager em;

    @Before
    public void setUp(){
        em = new EmojiManager();
    }

    @Test
    public void shouldReturnEmojiDrawableId(){
        assertEquals(R.drawable.new_emo_static_neutral, em.getEmojiDrawableId(EmojiType.Happy, 0));
    }

    @Test
    public void shouldReturnEmojiDrawableIdForHappyLevel1(){
        assertEquals(R.drawable.new_emo_dynamic_smile, em.getEmojiDrawableId(EmojiType.Happy, 1));
    }

    @Test
    public void shouldReturnMinusOneForHappyNotExistingLevel(){
        assertEquals(-1, em.getEmojiDrawableId(EmojiType.Happy, 2));
    }


    @Test
    public void shouldReturnEmojiDrawableIdForSad(){
        assertEquals(R.drawable.new_emo_static_sad, em.getEmojiDrawableId(EmojiType.Sad, 0));
    }


    @Test
    public void shouldReturnMinusOneForSadNotExistingLevel(){
        assertEquals(-1, em.getEmojiDrawableId(EmojiType.Sad, 1));
    }

    @Test
    public void shouldReturnEmojiDrawableIdForPlayful(){
        assertEquals(R.drawable.new_emo_dynamic_playful1, em.getEmojiDrawableId(EmojiType.Playful, 0));
    }

    @Test
    public void shouldReturnEmojiDrawableIdForPlayfulLevel1(){
        assertEquals(R.drawable.new_emo_dynamic_playful, em.getEmojiDrawableId(EmojiType.Playful, 1));
    }

    @Test
    public void shouldReturnMinusOneForPlayfulNotExistingLevel(){
        assertEquals(-1, em.getEmojiDrawableId(EmojiType.Playful, 2));
    }

    @Test
    public void shouldReturnEmojiDrawableIdForSurprised(){
        assertEquals(R.drawable.new_emo_dynamic_surprized, em.getEmojiDrawableId(EmojiType.Surprised, 0));
    }

    @Test
    public void shouldReturnMinusOneForSurprisedNotExistingLevel(){
        assertEquals(-1, em.getEmojiDrawableId(EmojiType.Surprised, 1));
    }

    @Test
    public void shouldReturnEmojiDrawableIdForScared(){
        assertEquals(R.drawable.new_emo_dynamic_surprized1, em.getEmojiDrawableId(EmojiType.Scared, 0));
    }

    @Test
    public void shouldReturnMinusOneForScaredNotExistingLevel(){
        assertEquals(-1, em.getEmojiDrawableId(EmojiType.Scared, 1));
    }

    @Test
    public void shouldReturnEmojiDrawableIdForLoving(){
        assertEquals(R.drawable.new_emo_dynamic_loving, em.getEmojiDrawableId(EmojiType.Loving, 0));
    }

    @Test
    public void shouldReturnMinusOneForLovingNotExistingLevel(){
        assertEquals(-1, em.getEmojiDrawableId(EmojiType.Loving, 1));
    }

    @Test
    public void shouldReturnMaxEmotionLevelForEmotion(){
        assertEquals(1, em.getMaxEmotionLevel(EmojiType.Happy));
        assertEquals(0, em.getMaxEmotionLevel(EmojiType.Sad));
    }

    @Test
    public void shouldReturnRandomEmotionTypeAndLevel(){
        Map<EmojiType, Integer> newEmotion = em.getNewEmotionAndLevel(EmojiType.Sad, 0);
        assertTrue(newEmotion.keySet().size() == 1);
        assertFalse( newEmotion.keySet().contains(EmojiType.Sad) &&
                    newEmotion.values().contains(0));
    }
}
