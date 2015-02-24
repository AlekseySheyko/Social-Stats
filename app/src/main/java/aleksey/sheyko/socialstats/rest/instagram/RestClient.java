package aleksey.sheyko.socialstats.rest.instagram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.converter.GsonConverter;

public class RestClient {

    private static final String BASE_URL = "https://api.instagram.com";
    private ApiService apiService;

    public RestClient() {

        Gson gson = new GsonBuilder()

                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }
}
