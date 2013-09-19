package com.acme.cap.graph;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class FirstGuiceApp {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new GraphModule());

        FirstGuice firstOne = injector.getInstance(FirstGuice.class);
        firstOne.createIndexOnLabelWithProperty(EntityLabel.ACCOUNT, "username");
        firstOne.checkIndexOnLabel(EntityLabel.ACCOUNT);
        firstOne.dropIndexOnLabel(EntityLabel.ACCOUNT);
        firstOne.checkIndexOnLabel(EntityLabel.ACCOUNT);
        firstOne.shutdownDb();
    }
}
