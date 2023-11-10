//package finaltask.manager;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
//
//import java.util.*;
//
//
//// аналог HttpTaskManager
//public class DataManager {
//
//    public static final String MAIN = "main";
//    public static final String SIDE = "side";
//
//    private final KVClient kvClient;
//    private final Gson gson;
//
//    private final Map<String, Data> mainDataByKey;
//    private final Map<String, Data> sideDataByKey;
//
//    public DataManager() {
//        kvClient = new KVClient();
//        gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .create();
//        mainDataByKey = new HashMap<>();
//        sideDataByKey = new HashMap<>();
//    }
//
//    public Data getMainByKey(String key) {
//        return mainDataByKey.get(key);
//    }
//
//    public Data getSideByKey(String key) {
//        return sideDataByKey.get(key);
//    }
//
//    private void load() {
//        String mainDataSrt = kvClient.load(MAIN);
//        if (mainDataSrt != null) {
//            List<Data> mainData = gson.fromJson(mainDataSrt, new TypeToken<ArrayList<Data>>() {});
//            mainData.forEach(d -> mainDataByKey.put(d.key(), d));
//        }
//
//        String sideDataSrt = kvClient.load(SIDE);
//        if (sideDataSrt != null) {
//            List<Data> sideData = gson.fromJson(sideDataSrt, new TypeToken<ArrayList<Data>>() {});
//            sideData.forEach(d -> sideDataByKey.put(d.key(), d));
//        }
//    }
//
//    public void saveMainData(Data data) {
//        mainDataByKey.put(data.getKey(), data);
//        String json = new Gson().toJson(data);
//        kvClient.put(MAIN, json);
//    }
//
//    public void saveSideData(Data data) {
//        sideDataByKey.put(data.getKey(), data);
//        String json = new Gson().toJson(data);
//        kvClient.put(SIDE, json);
//    }
//
//
//    static class Data {
//        private final String key;
//        private final String name;
//
//        Data(String key, String name) {
//            this.key = key;
//            this.name = name;
//        }
//
//        public String key() {
//            return key;
//        }
//
//        public String name() {
//            return name;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            Data data = (Data) o;
//            return Objects.equals(key, data.key) && Objects.equals(name, data.name);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(key, name);
//        }
//    }
//}