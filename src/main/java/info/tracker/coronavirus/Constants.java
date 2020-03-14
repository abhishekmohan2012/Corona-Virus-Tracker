package info.tracker.coronavirus;

public interface Constants {

    public static final String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
    public static final String DEATH_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Deaths.csv";
    public static final int NOT_FOUND = 404;
    public static final int SUCCESS = 200;
    public static final String STATE = "Province/State";
    public static final String COUNTRY = "Country/Region";
    public static final String LATITUDE = "Lat";
    public static final String LONGITUDE = "Long";
}
