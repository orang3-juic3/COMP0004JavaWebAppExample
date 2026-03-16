package uk.ac.ucl.model;

import uk.ac.ucl.main.Main;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Model {

    private static volatile Model instance = null;
    private final Map<HospitalDataType, DataFrame> coreFrames = new ConcurrentHashMap<>();
    public static int RESULTS_PER_PAGE = 20;


    private Model() {}
    // https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
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

    public DataFrame getFrame(HospitalDataType type) {
        return coreFrames.computeIfAbsent(type, t -> {
            try (DataLoader d = DataLoader.loadFromFile(Main.DATA_TYPE_PATH_MAP.get(t))) {
                return d.getDataFrame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
