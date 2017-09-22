package com.codegemz.elfi.coreapp.helper.xmpp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codegemz.elfi.coreapp.BrainActivity;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivateChat;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.listeners.QBPrivateChatManagerListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.List;

/**
 * Created by adrobnych on 9/21/15.
 */
public class XMPPClientBinder {

    private QBUser qbUser;

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    public XMPPClientBinder(QBUser qbUser){
        this.qbUser = qbUser;
    }

    public XMPPClientBinder initWithContext(Context context) {
        setContext(context);
        QBSettings.getInstance().fastConfigInit("28478", "akuYLAGnktvFNaA", "GvHWG8GfxcSyLNY");

        if (!QBChatService.isInitialized()) {
            QBChatService.init(context);
        }

        return this;
    }

    public void listenAndAnswer() {
        QBPrivateChatManager privateChatManager = QBChatService.getInstance().getPrivateChatManager();
        privateChatManager.addPrivateChatManagerListener(privateChatManagerListener);



    }

    public void processLogin() {
        QBAuth.createSession(qbUser, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success, login to chat

                qbUser.setId(session.getUserId());

                QBChatService.getInstance().login(qbUser, new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {
                        Log.d("user login: ", "OK");
                        try {
                            QBChatService.getInstance().startAutoSendPresence(60);
                        } catch (SmackException.NotLoggedInException e) {
                            Log.e("user login: ", "Problem: " + e);
                        }
                        QBChatService.getInstance().addConnectionListener(connectionListener);

                        startXMPPCommandsListening();
                    }

                    @Override
                    public void onError(List errors) {
                        Log.e("user login: ", "Problem");
                    }
                });
            }

            @Override
            public void onError(List<String> errors) {
                Log.e("create session: ", "Problem");
            }
        });

    }

    private void startXMPPCommandsListening() {
        listenAndAnswer();
    }


    ConnectionListener connectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection connection) {

        }

        @Override
        public void authenticated(XMPPConnection connection) {

        }

        @Override
        public void connectionClosed() {

        }

        @Override
        public void connectionClosedOnError(Exception e) {
            // connection closed on error. It will be established soon
        }

        @Override
        public void reconnectingIn(int seconds) {

        }

        @Override
        public void reconnectionSuccessful() {

        }

        @Override
        public void reconnectionFailed(Exception e) {

        }
    };

    QBMessageListener<QBPrivateChat> privateChatMessageListener = new QBMessageListener<QBPrivateChat>() {
        @Override
        public void processMessage(QBPrivateChat privateChat, final QBChatMessage chatMessage) {
            Log.e("Process message: ", "Message received: " + chatMessage.getBody());

            ((BrainActivity)context).getVrl().analyse(chatMessage.getBody());

//            QBPrivateChatManager privateChatManager = QBChatService.getInstance().getPrivateChatManager();
//
//            QBChatMessage chatAnswerMessage = new QBChatMessage();
//            chatAnswerMessage.setBody("test passed: " + chatMessage.getBody().toString());
//            chatAnswerMessage.setProperty("save_to_history", "1"); // Save a message to history
//
//
//            try {
//                privateChat.sendMessage(chatAnswerMessage);
//            } catch (XMPPException e) {
//                Log.e("processMessage:", "Problem: " + e);
//            } catch (SmackException.NotConnectedException e) {
//                Log.e("processMessage:", "Problem: " + e);
//            }
        }

        @Override
        public void processError(QBPrivateChat privateChat, QBChatException error, QBChatMessage originMessage){

        }

    };

    QBPrivateChatManagerListener privateChatManagerListener = new QBPrivateChatManagerListener() {
        @Override
        public void chatCreated(final QBPrivateChat privateChat, final boolean createdLocally) {
            if(!createdLocally){
                privateChat.addMessageListener(privateChatMessageListener);
            }
        }
    };
}
