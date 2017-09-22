package com.codegemz.elfi.coreapp.helper.emotion;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.codegemz.elfi.apicontracts.StateContract;
import com.codegemz.elfi.apicontracts.EmojiType;

/**
 * Created by adrobnych on 5/26/15.
 */
public class EmotionStateHelper {

    Context context;
    ContentResolver cr;

    public EmotionStateHelper(Context context){
        this.context = context;
        cr = context.getContentResolver();
    }

    public EmojiType getCurrentEmotionType() {
        Cursor c = getEmojiCursor();
        EmojiType result = EmojiType.fromString(c.getString(c.getColumnIndex(StateContract.Columns.VALUE1)));
        c.close();
        return result;
    }

    public int getCurrentEmotionLevel() {
        Cursor c = getEmojiCursor();
        int result = c.getInt(c.getColumnIndex(StateContract.Columns.VALUE2));
        c.close();
        return result;
    }

    public void setCurrentEmotionType(EmojiType currentEmotionType) {
        ContentValues v = new ContentValues();
        v.put(StateContract.Columns.VALUE1,     currentEmotionType.toString());
        cr.update(StateContract.CONTENT_URI, v, "name = ?", new String[]{"emotion"});
    }

    public void setCurrentEmotionLevel(int currentEmotionLevel) {
        ContentValues v = new ContentValues();
        v.put(StateContract.Columns.VALUE2,     "" + currentEmotionLevel);
        cr.update(StateContract.CONTENT_URI, v, "name = ?", new String[]{"emotion"});
    }

    private Cursor getEmojiCursor(){
        Cursor c = cr.query(
                StateContract.CONTENT_URI, null,
                "name = ?",
                new String[]{"emotion"}, null);
        c.moveToFirst();
        return  c;
    }

    public void initializeEmotionSate() {
        Cursor c = getEmojiCursor();
        if(c.getCount() == 0) // no Emotion State in DB -> create
            createDefaultEmotionState();
        c.close();
    }

    private void createDefaultEmotionState(){
        ContentValues v = new ContentValues();
        v.put(StateContract.Columns.NAME,       "emotion");
        v.put(StateContract.Columns.VALUE1,     EmojiType.Happy.toString());
        v.put(StateContract.Columns.VALUE2,     "0");
        cr.insert(StateContract.CONTENT_URI, v);
    }
}
