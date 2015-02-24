package aleksey.sheyko.socialstats.rest.instagram;

import retrofit.http.GET;
import retrofit.http.Query;

public interface ApiService {

    @GET("/oauth/authorize")
    public String navigateToLogin(@Query("client_id") String clientId,
                                  @Query("redirect_uri") String callbackUrl,
                                  @Query("response_type") String authCode);

}
