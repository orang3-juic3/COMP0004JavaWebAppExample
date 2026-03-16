package uk.ac.ucl.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import uk.ac.ucl.main.Config;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static uk.ac.ucl.main.Config.SEARCH_CACHE_SIZE;

/**
 * The main Model class is primarily responsible for creating and maintaining mappings of data types to DataFrames.
 * For the core csv files, a permanent lazy-loaded map is held in memory
 * For search results, a global LRU cache is used
 */
public class Model {

    private final Map<HospitalDataType, DataFrame> coreFrames = new ConcurrentHashMap<>();

    // Prevent map from unnecessarily resizing itself when we already know its max size
    // Exploits LinkedHashMap's built-in features that make LRU caching easier
    private final Map<Search, DataFrame> searchFrameCache = Collections.synchronizedMap(
            new LinkedHashMap<>((int) Math.ceil(SEARCH_CACHE_SIZE / 0.75f), 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Search, DataFrame> eldest) {
                    return size() > SEARCH_CACHE_SIZE;
                }
            }
    );

    /*
    This class implements the singleton pattern using double-checked locking to ensure thread safety,
    as described in https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
     */
    private static volatile Model instance = null;

    private Model() {}

    public static Model getInstance() {
        Model ref = instance;
        if (ref == null) {
            synchronized (Model.class) {
                ref = instance;
                if (ref == null) {
                    instance = ref = new Model();
                }
            }
        }
        return ref;
    }

    /*
    Lazily loads dataframes corresponding to each csv file in a thread-safe manner
     */
    public DataFrame getFrame(@Nonnull HospitalDataType type) {
        return coreFrames.computeIfAbsent(type, t -> {
            try (DataLoader d = DataLoader.loadFromFile(Config.DATA_TYPE_PATH_MAP.get(t))) {
                return d.getDataFrame();
            } catch (IOException e) {
                throw new ServerErrorException(e);
            }
        });
    }

    @Nullable
    public DataFrame getSearchFrame(@Nonnull Search search) {
        return searchFrameCache.get(search);
    }

    public void cacheSearchFrame(@Nonnull Search search, @Nonnull DataFrame frame) {
        searchFrameCache.put(search, frame);
    }
}
