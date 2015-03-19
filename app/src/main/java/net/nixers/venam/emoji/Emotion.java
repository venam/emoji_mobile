package net.nixers.venam.emoji;


public class Emotion {
    private long id;
    private String emotion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return emotion;
    }
}
