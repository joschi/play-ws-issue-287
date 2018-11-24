package com.github.joschi;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import play.libs.ws.StandaloneWSClient;
import play.libs.ws.StandaloneWSRequest;
import play.libs.ws.StandaloneWSResponse;
import play.libs.ws.ahc.StandaloneAhcWSClient;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;

public class WSRequestQueryParametersTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void uriEncode() throws Exception {
        StandaloneWSClient wsClient = new StandaloneAhcWSClient(new DefaultAsyncHttpClient(), null);
        String requestUrl = wireMockRule.url("/path");
        stubFor(get(urlEqualTo("/path?the+plus+must+remain")).willReturn(aResponse()
                .withStatus(200)
                .withBody("OK")));

        StandaloneWSRequest wsRequest = wsClient.url(requestUrl)
                .addQueryParameter("the+plus+must+remain", "");
        String url = wsRequest.getUrl();
        assertEquals("Request URL and WSRequest.Url should be identical", requestUrl, url);

        StandaloneWSResponse wsResponse = wsRequest.get().toCompletableFuture().get();
        assertEquals(200, wsResponse.getStatus());

        verify(getRequestedFor(urlMatching("/path")).withQueryParam("the+plus+must+remain", equalTo("")));
    }

    @Test
    public void uriEncode2() throws Exception {
        StandaloneWSClient wsClient = new StandaloneAhcWSClient(new DefaultAsyncHttpClient(), null);
        String requestUrl = wireMockRule.url("/path");
        stubFor(get(urlEqualTo("/path?the+plus+must+remain")).willReturn(aResponse()
                .withStatus(200)
                .withBody("OK")));

        StandaloneWSRequest wsRequest = wsClient.url(requestUrl)
                .setQueryString("the+plus+must+remain");
        String url = wsRequest.getUrl();
        assertEquals("Request URL and WSRequest.Url should be identical", requestUrl, url);

        StandaloneWSResponse wsResponse = wsRequest.get().toCompletableFuture().get();
        assertEquals(200, wsResponse.getStatus());

        verify(getRequestedFor(urlMatching("/path")).withQueryParam("the+plus+must+remain", equalTo("")));
    }
}