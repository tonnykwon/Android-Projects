package com.tony.tonythetiger.team_plane.SelectedList;

/**
 * Created by tony on 2017-11-19.
 */

public class SelectedItem {

    // 팀, 개인 관련 모든 정보
    private String title;
    private String content;

    public SelectedItem(String title, String context) {
        this.title = title;
        this.content = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String context) {
        this.content = context;
    }
}
