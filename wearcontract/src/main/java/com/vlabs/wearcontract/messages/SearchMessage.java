package com.vlabs.wearcontract.messages;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.DataMapBuilder;

public class SearchMessage {

    public static final String KEY_SEARCH_TERM = "searm-term";

    private final String mTerm;

    public SearchMessage(final String term) {
        mTerm = term;
    }

    public SearchMessage(final DataMap dataMap) {
        mTerm = dataMap.getString(KEY_SEARCH_TERM);
    }

    public String term() {
        return mTerm;
    }

    public DataMap asDataMap() {
        return new DataMapBuilder()
                .putString(KEY_SEARCH_TERM, mTerm)
                .getMap();
    }

}
