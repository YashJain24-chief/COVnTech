package com.example.covntech;

//Custom Data structure/class
public class dataFormat {

    private String mCountry;

    private long mConfirmed;

    private long mRecovered;

    private long mDeath;

    public long mTotalConfirmed;

    public long mTotalRecovered;

    public long mTotalDeath;

    dataFormat(String country, long confirmed, long recovered, long death) {
        mCountry = country;
        mConfirmed = confirmed;
        mRecovered = recovered;
        mDeath = death;
    }

    dataFormat(long totalconfirmed, long totalrecovered, long totaldeath) {

    }

    public String getmCountry() {
        return mCountry;
    }

    public long getmConfirmed() {
        return mConfirmed;
    }

    public long getmRecovered() {
        return mRecovered;
    }

    public long getmDeath() {
        return mDeath;
    }

    public long getmTotalConfirmed() {
        return mTotalConfirmed;
    }

    public long getmTotalRecovered() {
        return mTotalRecovered;
    }

    public long getmTotalDeath() {
        return mTotalDeath;
    }

}
