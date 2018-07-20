@SuppressWarnings("requires-transitive-automatic")
module org.javacord.core {
    requires logging.interceptor;

    requires java.logging;

    requires transitive org.javacord.api;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive nv.websocket.client;
    requires transitive slf4j.api;

    requires transitive java.desktop;

    exports org.javacord.core.util to org.javacord.api;

    provides org.javacord.api.util.internal.DelegateFactoryDelegate
        with org.javacord.core.util.DelegateFactoryDelegateImpl;
}
