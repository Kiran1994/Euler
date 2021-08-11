package org.euler.data.graphql;

public class SchemaConstants {

    public static class Types {
        public static final String QUERY_TYPE = "QueryType";
    }

    public static class QueryName {
        public static final String USER_BY_ID = "userById";
        public static final String USERS_BY_NAME = "usersByName";
    }

    public static class QueryParam {
        public static final String ID = "id";
        public static final String NAME = "name";
    }
}
