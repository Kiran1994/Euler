package org.euler.utils;

public class Constants {

    public static class ThreadContext {
        public static final String REQUEST_ID = "requestId";
        public static final String CLIENT_ID = "clientId";
    }

    public static class RequestHeaders {
        public static final String REQUEST_ID = "x_request_id";
        public static final String CLIENT_ID = "x_client_id";
    }

    public static class Guice {
        public static final String DATA_HANDLERS = "dataHandlers";
    }
}
