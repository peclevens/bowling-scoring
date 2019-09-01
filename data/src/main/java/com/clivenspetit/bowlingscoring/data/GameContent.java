package com.clivenspetit.bowlingscoring.data;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class GameContent {

    private String content;
    private long lastModified;

    public GameContent() {

    }

    public GameContent(String content, long lastModified) {
        this.content = content;
        this.lastModified = lastModified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
