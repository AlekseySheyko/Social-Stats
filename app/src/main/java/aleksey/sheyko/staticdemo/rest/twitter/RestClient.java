package aleksey.sheyko.staticdemo.rest.twitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import aleksey.sheyko.staticdemo.rest.twitter.service.ApiService;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {

    private static final String BASE_URL = "your base url";
    private ApiService apiService;

    public RestClient() {
        Gson gson = new GsonBuilder()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }
}
