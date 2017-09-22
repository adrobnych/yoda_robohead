package com.codegemz.elfi.model;

import com.codegemz.elfi.apicontracts.EmojiType;
import com.codegemz.elfi.coreapp.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adrobnych on 5/21/15.
 */
public class EmojiManager {

    private HashMap<EmojiType, List<Integer>> emojiMap;

    public EmojiManager(){
        emojiMap = new HashMap<>();

        List<Integer> happyList = Arrays.asList(R.drawable.new_emo_static_neutral, R.drawable.new_emo_dynamic_smile);
        emojiMap.put(EmojiType.Happy, happyList);

        List<Integer> sadList = Arrays.asList(R.drawable.new_emo_static_sad);
        emojiMap.put(EmojiType.Sad, sadList);

        List<Integer> playfulList = Arrays.asList(R.drawable.new_emo_dynamic_playful1, R.drawable.new_emo_dynamic_playful);
        emojiMap.put(EmojiType.Playful, playfulList);

        List<Integer> surprisedList = Arrays.asList(R.drawable.new_emo_dynamic_surprized);
        emojiMap.put(EmojiType.Surprised, surprisedList);

        List<Integer> scaredList = Arrays.asList(R.drawable.new_emo_dynamic_surprized1);
        emojiMap.put(EmojiType.Scared, scaredList);

        List<Integer> lovingList = Arrays.asList(R.drawable.new_emo_dynamic_loving);
        emojiMap.put(EmojiType.Loving, lovingList);

        List<Integer> talkList = Arrays.asList(R.drawable.new_emo_dynamic_talk);
        emojiMap.put(EmojiType.Talk, talkList);

        List<Integer> ninjaList = Arrays.asList(R.drawable.new_emo_ninja);
        emojiMap.put(EmojiType.Ninja, ninjaList);
    }

    public int getEmojiDrawableId(EmojiType emojiType, int emojiLevel){
        if(emojiLevel >= emojiMap.get(emojiType).size())
            return -1;
        return emojiMap.get(emojiType).get(emojiLevel);
    }

    public int getMaxEmotionLevel(EmojiType et) {
        return emojiMap.get(et).size() - 1;
    }

    public Map<EmojiType, Integer> getNewEmotionAndLevel(EmojiType currentET, int currenEL) {
        Map<EmojiType, Integer> newEmotion = new HashMap<>();

        if(getMaxEmotionLevel(currentET) > currenEL)
            newEmotion.put(currentET, currenEL+1);
        else {
            if (currentET == EmojiType.Loving)  // last emotion in enum
                newEmotion.put(EmojiType.Happy, 0);
            else
                newEmotion.put(currentET.next(), 0);
        }

        return newEmotion;
    }

}
