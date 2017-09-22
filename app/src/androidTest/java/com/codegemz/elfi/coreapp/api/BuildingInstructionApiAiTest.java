package com.codegemz.elfi.coreapp.api;

import android.test.AndroidTestCase;

import com.codegemz.elfi.coreapp.helper.api_ai.ApiAiHelper;
import com.google.gson.Gson;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.GsonFactory;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

/**
 * Created by adrobnych on 8/14/15.
 */
public class BuildingInstructionApiAiTest extends AndroidTestCase {

    private AIConfiguration config;
    private AIDataService aiDataService;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        config = new AIConfiguration(ApiAiHelper.ACCESS_TOKEN, ApiAiHelper.SUBSCRIPTION_KEY,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDataService = new AIDataService(getContext(), config);

    }

    private Gson gson = GsonFactory.getGson();


    public void testInitFamilyMemberConversationAiResponce() {

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("building instruction");

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            final Result result = aiResponse.getResult();

            assertTrue("got fulfillment: " + result.getFulfillment().getSpeech(),
                    result.getFulfillment().getSpeech().equals(
                            "please look at this. Use arrows to navigate between steps."));
            assertTrue("got action: " + result.getAction(),
                    result.getAction().equals(
                            "showBuildingInstruction"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }
}
