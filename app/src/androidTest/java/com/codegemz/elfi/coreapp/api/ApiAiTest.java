package com.codegemz.elfi.coreapp.api;

import android.test.AndroidTestCase;

import com.codegemz.elfi.coreapp.helper.api_ai.ApiAiHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.GsonFactory;
import ai.api.RequestExtras;
import ai.api.model.AIContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

/**
 * Created by adrobnych on 8/2/15.
 */
public class ApiAiTest extends AndroidTestCase {
    private AIConfiguration config;
    private AIDataService aiDataService;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        config = new AIConfiguration(ApiAiHelper.ACCESS_TOKEN, ApiAiHelper.SUBSCRIPTION_KEY,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDataService = new AIDataService(getContext(), config);

    }

    private Gson gson = GsonFactory.getGson();

    public void testHelloAiResponce() {

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("Hello");

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            // process response object here...

            assertTrue("got: " + gson.toJson(aiResponse),
                    gson.toJson(aiResponse).matches("^.*greetingProactive.*$"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }

    public void testElfiCanDoAiResponce() {

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("ELFi, what can you do?");

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            // process response object here...

            assertTrue("got: " + gson.toJson(aiResponse),
                    gson.toJson(aiResponse).matches(
                            "^.*ELFi can be a home robot, a robotics teacher, a contester and a worker.*$"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }

    public void testWeatherAiResponce() {

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("temperature in heraklion");

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            final Result result = aiResponse.getResult();
            // process response object here...

            assertTrue("got: " + result.getFulfillment().getSpeech(),
                    result.getFulfillment().getSpeech().matches(
                            "^.*in Heraklion.*$"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }

    public void testContestsAiResponce() {

        List<AIContext> contexts = new ArrayList<>();
        contexts.add(new AIContext("raisedRightHand"));

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("put it down");
        aiRequest.setContexts(contexts);

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            final Result result = aiResponse.getResult();
            // process response object here...

            assertTrue("got: " + result.getAction(),
                    result.getAction().matches(
                            "putRightHandDown"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }
}
