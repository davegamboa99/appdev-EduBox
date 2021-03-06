package com.elearneur.edubox.notifications.helper;

public class EventTest {
    private String date, time, title, info, contentType, timeRange;
    private boolean read, finished;

    public EventTest(String date, String time, String title, String info, String contentType, String timeRange, boolean read , boolean finished) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.info = info;
        this.contentType = contentType;
        this.timeRange = timeRange;
        this.read = read;
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
