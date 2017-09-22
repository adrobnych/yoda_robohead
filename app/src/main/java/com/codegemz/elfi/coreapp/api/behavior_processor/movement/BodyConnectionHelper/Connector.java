package com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper;

/**
 * Created by adrobnych on 4/8/16.
 */
public interface Connector {
    void setConnectionAdapterState(boolean state);

    boolean connect();
    void close();
    public void writeSingleMessage(String command);
    public void writeCompositeMessage(String compiledAlgorithm);
    public String waitAndReadMessage();
    public String getTag();
    public String fieldDelimiter();
    public String commandDelimiter();
}
