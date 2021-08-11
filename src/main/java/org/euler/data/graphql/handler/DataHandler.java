package org.euler.data.graphql.handler;

import graphql.schema.DataFetcher;
import org.dataloader.DataLoader;

import java.util.Map;

public interface DataHandler {

    Map<String, DataLoader> getDataLoaderMap();

    Map<String, DataFetcher> getDataFetcherMap();
}
