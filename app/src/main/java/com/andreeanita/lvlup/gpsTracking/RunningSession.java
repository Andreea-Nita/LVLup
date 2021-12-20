package com.andreeanita.lvlup.gpsTracking;

public class RunningSession {
    public String date;
    public String activityTime;
    public String pace;
    public String timeElapsed;
    public String finalDistance;
    public String image;
    public String tipe;

    public RunningSession() {
    }

    public RunningSession(String date, String activityTime, String pace, String timeElapsed, String finalDistance, String image, String tipe) {
        this.date = date;
        this.activityTime = activityTime;
        this.pace = pace;
        this.timeElapsed = timeElapsed;
        this.finalDistance = finalDistance;
        this.image = image;
        this.tipe = tipe;
    }

    public String getDate() {
        return date;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public String getPace() {
        return pace;
    }

    public String getTimeElapsed() {
        return timeElapsed;
    }

    public String getFinalDistance() {
        return finalDistance;
    }

    public String getImage() {
        return image;
    }

    public String getTipe() {
        return tipe;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public void setTimeElapsed(String timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public void setFinalDistance(String finalDistance) {
        this.finalDistance = finalDistance;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }
}
