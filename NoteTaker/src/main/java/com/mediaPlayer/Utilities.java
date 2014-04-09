package com.mediaPlayer;

/**
 * Kevin Damazyn
 * taken from androidhive.com
 * molded for note taker
 */
public class Utilities {

    //Convert milliseconds to Hours:Minutes:Seconds
    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        //Convert total duration into time
            int hours = (int)( milliseconds / (1000*60*60));
            int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
            int seconds = (int)((milliseconds % (1000*60*60)) % (1000*60) / 1000);
            //add hours if there that long
            if(hours > 0){
                finalTimerString = hours + ":";
            }

            //adding a 0 to seconds if it is one digit
            if(seconds < 10){
                secondsString = "0" + seconds;
            }else {
                secondsString = "" + seconds;
            }

            finalTimerString = finalTimerString + minutes + ":" + secondsString;

            //return time string
            return finalTimerString;
    }

    //function to get progress percentage
    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        //calculating percentage
        percentage = (((double)currentSeconds)/totalSeconds)*100;

        //return percentage
        return percentage.intValue();
    }

    //function to change progress timer
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        //return current duration in milliseconds
        return currentDuration * 1000;
    }
}
