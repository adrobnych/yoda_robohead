package com.codegemz.elfi.apicontracts;

/**
 * Created by adrobnych on 5/21/15.
 */
public enum EmojiType {
    Happy, Sad, Playful, Surprised, Scared, Loving, Talk, Ninja;

    public static EmojiType fromString(String emojiString){
        return EmojiType.valueOf(emojiString);
    }

    private static EmojiType[] all = values();
    public EmojiType next()
    {
        return all[(this.ordinal()+1) % all.length];
    }
}
