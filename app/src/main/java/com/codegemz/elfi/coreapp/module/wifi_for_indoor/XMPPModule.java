package com.codegemz.elfi.coreapp.module.wifi_for_indoor;

import android.content.Context;

import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.coreapp.helper.xmpp.XMPPClientBinder;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by adrobnych on 9/22/15.
 */
@Module(
        injects = {
                BrainActivity.class
        },
        library = true)
public class XMPPModule {


    private final QBUser qbUser = new QBUser("elfi2", "elfielfi");

    private XMPPClientBinder xmppBinder;

    @Provides
    @Singleton
    public XMPPClientBinder providesXMPPBinder() {

        xmppBinder = new XMPPClientBinder(qbUser);
        return xmppBinder;
    }

}
