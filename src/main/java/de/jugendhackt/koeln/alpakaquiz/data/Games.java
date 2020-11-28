package de.jugendhackt.koeln.alpakaquiz.data;

import com.google.common.collect.HashBiMap;

public class Games {
    public static final HashBiMap<Integer, Quiz> games = HashBiMap.create();

    static {
        games.put(123, new Quiz());
        games.put(321, new Quiz());
    }
}
