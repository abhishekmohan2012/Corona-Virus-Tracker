package info.tracker.coronavirus.models;

import java.util.ArrayList;
import java.util.List;

public class CoronaCountryModel {
    private String country;
    private List<CoronaStateModel> stateModelsList = new ArrayList<>();
    private int latestCases;
    private int diffFromPrevDay;
    private boolean updated;
    private Double longitude;
    private Double latitude;
    private int death;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<CoronaStateModel> getStateModelsList() {
        return stateModelsList;
    }

    public void setStateModelsList(List<CoronaStateModel> stateModelsList) {
        this.stateModelsList = stateModelsList;
    }

    public int getLatestCases() {
        return latestCases;
    }

    public void setLatestCases(int latestCases) {
        this.latestCases = latestCases;
    }

    public int getDiffFromPrevDay() {
        return diffFromPrevDay;
    }

    public void setDiffFromPrevDay(int diffFromPrevDay) {
        this.diffFromPrevDay = diffFromPrevDay;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    @Override
    public String toString() {
        return "CoronaCountryModel{" +
                "country='" + country + '\'' +
                ", stateModelsList=" + stateModelsList +
                ", latestCases=" + latestCases +
                ", diffFromPrevDay=" + diffFromPrevDay +
                ", updated=" + updated +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", death=" + death +
                '}';
    }
}
