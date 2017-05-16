package com.electrocomponents.messagerelay.process.persist;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class LocalInitialContextFactory implements InitialContextFactory {

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        Context context = PowerMockito.mock(Context.class);
        Mockito.when(context.lookup("java:app/AppName")).thenReturn("message-relay-core");
        return context;
        
    }

}
