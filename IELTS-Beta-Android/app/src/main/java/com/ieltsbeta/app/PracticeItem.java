package com.ieltsbeta.app;

public class PracticeItem {
    public String skill;       // Listening / Reading / etc
    public String category;    // Test / Tip
    public String title;       // Video title
    public String description; // Small description
    public String videoUrl;    // YouTube link

    public PracticeItem(String skill, String category,
                        String title, String description, String videoUrl) {
        this.skill = skill;
        this.category = category;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
    }
}