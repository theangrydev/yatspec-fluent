package acceptance.example.test;

import io.github.theangrydev.yatspecfluent.SystemUnderTest;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.assertj.core.api.WithAssertions;


public class WeatherApplicationUnderTest implements SystemUnderTest<TestInfrastructure, Request, Response>, WithAssertions {

    private String city;

    @Override
    public Request request(TestInfrastructure testInfrastructure) throws Exception {
        return weatherRequest(testInfrastructure.serverBaseUrl());
    }

    @Override
    public Response call(Request request, TestInfrastructure testInfrastructure) throws Exception {
        OkHttpClient httpClient = testInfrastructure.httpClient();
        return httpClient.newCall(request).execute();
    }

    private Request weatherRequest(String baseUrl) {
        HttpUrl weatherUrl = HttpUrl.parse(baseUrl).newBuilder().addPathSegment("weather").addQueryParameter("city", this.city).build();
        return new Request.Builder().url(weatherUrl).build();
    }

    public void forCity(String city) {
        this.city = city;
    }

    public WeatherApplicationUnderTest requestsTheWeather() {
        return this;
    }
}
