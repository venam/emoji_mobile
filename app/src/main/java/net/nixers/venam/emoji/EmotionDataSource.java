package net.nixers.venam.emoji;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EmotionDataSource {
    // Database fields
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = { SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_EMOTION };

    public EmotionDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Emotion createEmotion(String emotion) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_EMOTION, emotion);
        long insertId = database.insert(SQLiteHelper.TABLE_EMOTIONS, null,
                values);
        Cursor cursor = database.query(SQLiteHelper.TABLE_EMOTIONS,
                allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Emotion newComment = cursorToEmotion(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteEmotion(Emotion emotion) {
        long id = emotion.getId();
        database.delete(SQLiteHelper.TABLE_EMOTIONS, SQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Emotion> getAllComments() {
        List<Emotion> emotions = new ArrayList<Emotion>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_EMOTIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Emotion emotion = cursorToEmotion(cursor);
            emotions.add(emotion);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return emotions;
    }

    private Emotion cursorToEmotion(Cursor cursor) {
        Emotion comment = new Emotion();
        comment.setId(cursor.getLong(0));
        comment.setEmotion(cursor.getString(1));
        return comment;
    }
}
