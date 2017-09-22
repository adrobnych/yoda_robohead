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
import ai.api.model.AIContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

/**
 * Created by adrobnych on 8/2/15.
 */
public class NewFamilyMemberApiAiTest extends AndroidTestCase {
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


    public void testInitFamilyMemberConversationAiResponce() {

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("new family member");

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            final Result result = aiResponse.getResult();

            assertTrue("got fulfillment: " + result.getFulfillment().getSpeech(),
                    result.getFulfillment().getSpeech().equals(
                            "hi! what is your name?"));
            assertTrue("got action: " + result.getAction(),
                    result.getAction().equals(
                            "startOfConversationAboutNewFamilyMember"));
            assertTrue("got context: " + result.getContexts()[0].getName(),
                    result.getContexts()[0].getName().equals(
                            "new_family_member_conversation"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }

    public void testFamilyMemberConversationSetNameAiResponce() {

        //important: manual addition of input context not needed in app context
        List<AIContext> contexts = new ArrayList<>();
        contexts.add(new AIContext("new_family_member_conversation"));

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("my name is mark");
        aiRequest.setContexts(contexts);

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            final Result result = aiResponse.getResult();

            assertTrue("got fulfillment: " + result.getFulfillment().getSpeech(),
                    result.getFulfillment().getSpeech().equals(
                            "it's nice to meet you mark! who are you in the family?"));
            assertTrue("got context1: " + result.getContexts()[0].getName(),
                    result.getContexts()[0].getName().equals(
                            "new_family_member_conversation"));
            assertTrue("got action: " + result.getAction(),
                    result.getAction().equals(
                            "familyMemberSetName"));
            assertTrue("got parameter: " + result.getStringParameter("fmname"),
                    result.getStringParameter("fmname").equals(
                            "mark"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }

    public void testFamilyMemberConversationSetRoleAiResponce() {

        List<AIContext> contexts = new ArrayList<>();
        contexts.add(new AIContext("new_family_member_conversation"));

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("i am a son in this family");
        aiRequest.setContexts(contexts);

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            final Result result = aiResponse.getResult();

            assertTrue("got fulfillment: " + result.getFulfillment().getSpeech(),
                    result.getFulfillment().getSpeech().equals(
                            "OK! how old are you?"));
            assertTrue("got context1: " + result.getContexts()[0].getName(),
                    result.getContexts()[0].getName().equals(
                            "new_family_member_conversation"));
            assertTrue("got action: " + result.getAction(),
                    result.getAction().equals(
                            "familyMemberSetRole"));
            assertTrue("got parameter: " + result.getStringParameter("FMRole"),
                    result.getStringParameter("FMRole").equals(
                            "Son"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }

    public void testFamilyMemberConversationSetageAiResponce() {

        List<AIContext> contexts = new ArrayList<>();
        contexts.add(new AIContext("new_family_member_conversation"));

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("i am 15");
        aiRequest.setContexts(contexts);

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            final Result result = aiResponse.getResult();

            assertTrue("got fulfillment: " + result.getFulfillment().getSpeech(),
                    result.getFulfillment().getSpeech().equals(
                            "What is the name of your school or company?"));
            assertTrue("got context1: " + result.getContexts()[0].getName(),
                    result.getContexts()[0].getName().equals(
                            "new_family_member_conversation"));
            assertTrue("got context2: " + result.getContexts()[1].getName(),
                    result.getContexts()[1].getName().equals(
                            "name_of_company"));
            assertTrue("got action: " + result.getAction(),
                    result.getAction().equals(
                            "familyMemberSetAge"));
            assertTrue("got parameter: " + result.getStringParameter("fmage"),
                    result.getStringParameter("fmage").equals(
                            "15"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }

    public void testFamilyMemberConversationSetCompanyNameAiResponce() {

        List<AIContext> contexts = new ArrayList<>();
        contexts.add(new AIContext("new_family_member_conversation"));
        contexts.add(new AIContext("name_of_company"));

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("I'm going to uzhhorod gymnasium");
        aiRequest.setContexts(contexts);

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            final Result result = aiResponse.getResult();


            assertTrue("got fulfillment: " + result.getFulfillment().getSpeech(),
                    result.getFulfillment().getSpeech().equals(
                            "what is your job title?"));
            assertTrue("got context1: " + result.getContexts()[0].getName(),
                    result.getContexts()[0].getName().equals(
                            "new_family_member_conversation"));
            assertTrue("got context2: " + result.getContexts()[1].getName(),
                    result.getContexts()[1].getName().equals(
                            "name_of_company"));
            assertTrue("got context2: " + result.getContexts()[2].getName(),
                    result.getContexts()[2].getName().equals(
                            "job_title"));
            assertTrue("got action: " + result.getAction(),
                    result.getAction().equals(
                            "familyMemberSetCompanyName"));
            assertTrue("got parameter: " + result.getStringParameter("fmcompanyname"),
                    result.getStringParameter("fmcompanyname").equals(
                            "uzhhorod gymnasium"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }

    public void testFamilyMemberConversationSetJobTitleAiResponce() {

        List<AIContext> contexts = new ArrayList<>();
        contexts.add(new AIContext("new_family_member_conversation"));
        contexts.add(new AIContext("job_title"));

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery("My job title is robot maker");
        aiRequest.setContexts(contexts);

        try {
            final AIResponse aiResponse = aiDataService.request(aiRequest);
            final Result result = aiResponse.getResult();


            assertTrue("got context1: " + result.getContexts()[0].getName(),
                    result.getContexts()[0].getName().equals(
                            "new_family_member_conversation"));
            assertTrue("got action: " + result.getAction(),
                    result.getAction().equals(
                            "familyMemberSetJob"));
            assertTrue("got parameter: " + result.getStringParameter("fmjob"),
                    result.getStringParameter("fmjob").equals(
                            "robot maker"));
            assertTrue("got fulfillment: " + result.getFulfillment().getSpeech(),
                    result.getFulfillment().getSpeech().equals(
                            "thank you so much! please register you bluetooth indicator in near future"));


        } catch (final AIServiceException e) {
            e.printStackTrace();
        }

    }

}