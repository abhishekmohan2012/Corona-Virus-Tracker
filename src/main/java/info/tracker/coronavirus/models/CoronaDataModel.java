package info.tracker.coronavirus.models;

public class CoronaDataModel {
    private String state;
    private String country;
    private int latestCases;
    private int diffFromPrevDay;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    @Override
    public String toString() {
        return "CoronaDataModel{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", infectedCase=" + latestCases +
                ", diffFromPrevDay=" + diffFromPrevDay +
                '}';
    }
}
