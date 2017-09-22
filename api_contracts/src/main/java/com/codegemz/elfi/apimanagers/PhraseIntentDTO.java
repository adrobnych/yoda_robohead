package com.codegemz.elfi.apimanagers;

import java.util.List;

/**
 * Created by adrobnych on 4/2/16.
 */
public class PhraseIntentDTO {
    private List<String> pattenrns;

    private int id;

    private String spoken_answer;

    private String intent_broadcast;

    private String intent_json_extras;

    private String input_context;

    private String output_context;

    public PhraseIntentDTO(List<String> pattenrns, String spoken_answer, String intent_broadcast, String intent_json_extras, String input_context, String output_context) {
        this.pattenrns = pattenrns;
        this.spoken_answer = spoken_answer;
        this.intent_broadcast = intent_broadcast;
        this.intent_json_extras = intent_json_extras;
        this.input_context = input_context;
        this.output_context = output_context;
    }

    public List<String> getPattenrns() {
        return pattenrns;
    }

    public void setPattenrns(List<String> pattenrns) {
        this.pattenrns = pattenrns;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpoken_answer() {
        return spoken_answer;
    }

    public void setSpoken_answer(String spoken_answer) {
        this.spoken_answer = spoken_answer;
    }

    public String getIntent_broadcast() {
        return intent_broadcast;
    }

    public void setIntent_broadcast(String intent_broadcast) {
        this.intent_broadcast = intent_broadcast;
    }

    public String getIntent_json_extras() {
        return intent_json_extras;
    }

    public void setIntent_json_extras(String intent_json_extras) {
        this.intent_json_extras = intent_json_extras;
    }

    public String getInput_context() {
        return input_context;
    }

    public void setInput_context(String input_context) {
        this.input_context = input_context;
    }

    public String getOutput_context() {
        return output_context;
    }

    public void setOutput_context(String output_context) {
        this.output_context = output_context;
    }
}
