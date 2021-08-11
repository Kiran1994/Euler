package org.euler.data.graphql.handler;

import com.google.inject.Inject;
import graphql.schema.DataFetcher;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.MappedBatchLoader;
import org.euler.data.model.User;
import org.euler.data.dao.UserDAO;
import org.euler.data.graphql.SchemaConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class UserHandler implements DataHandler {

    private static final String USER_BY_ID = "userById";
    private static final String USERS_BY_NAME = "usersByName";

    private final UserDAO userDAO;

    private final BatchLoader<Long, User> userByIdBatchLoader = new BatchLoader<>() {
        @Override
        public CompletionStage<List<User>> load(List<Long> userIds) {
            return CompletableFuture.supplyAsync(() -> userDAO.getUsersByIds(userIds));
        }
    };

    private final MappedBatchLoader<String, List<User>> usersByNameBatchLoader = new MappedBatchLoader<>() {
        @Override
        public CompletionStage<Map<String, List<User>>> load(Set<String> names) {
            List<String> namesList = names.stream().collect(Collectors.toList());

            return CompletableFuture.supplyAsync(() ->
                    userDAO.getUsersByName(namesList).stream().collect(Collectors.groupingBy(User::getName))
            );
        }
    };

    private final DataFetcher userByIdDataFetcher = environment -> {
        DataLoader<Long, Object> dataLoader = environment.getDataLoader(USER_BY_ID);
        Long id = Long.parseLong(environment.getArgument(SchemaConstants.QueryParam.ID));

        return dataLoader.load(id);
    };

    private final DataFetcher usersByNameDataFetcher = environment -> {
        DataLoader<String, Object> dataLoader = environment.getDataLoader(USERS_BY_NAME);
        String name = environment.getArgument(SchemaConstants.QueryParam.NAME);

        return dataLoader.load(name);
    };

    @Inject
    public UserHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private DataLoader<Long, User> getUserByIdDataLoader() {
        return DataLoader.newDataLoader(userByIdBatchLoader);
    }

    private DataLoader<String, List<User>> getUsersByNameDataLoader() {
        return DataLoader.newMappedDataLoader(usersByNameBatchLoader);
    }

    @Override
    public Map<String, DataLoader> getDataLoaderMap() {
        Map<String, DataLoader> dataLoaderMap = new HashMap<>();
        dataLoaderMap.put(USER_BY_ID, getUserByIdDataLoader());
        dataLoaderMap.put(USERS_BY_NAME, getUsersByNameDataLoader());

        return dataLoaderMap;
    }

    @Override
    public Map<String, DataFetcher> getDataFetcherMap() {
        Map<String, DataFetcher> dataFetcherMap = new HashMap<>();
        dataFetcherMap.put(SchemaConstants.QueryName.USER_BY_ID, userByIdDataFetcher);
        dataFetcherMap.put(SchemaConstants.QueryName.USERS_BY_NAME, usersByNameDataFetcher);

        return dataFetcherMap;
    }
}
