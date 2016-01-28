package com.vlabs.androiweartest.oughter;

import com.vlabs.Converter;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.dummy.DummyWearStation;

import java.util.ArrayList;
import java.util.List;

public class OuterStation {

    public static class StationConverter implements Converter<OuterStation, WearStation> {

        @Override
        public WearStation convert(final OuterStation outerStation) {
            return DummyWearStation.Dummies.get(outerStation.mId);
        }
    }

    public static class StationsListConverter implements Converter<List<OuterStation>, List<WearStation>> {

        @Override
        public List<WearStation> convert(final List<OuterStation> stations) {

            final ArrayList<WearStation> result = new ArrayList<>();
            final StationConverter converter = new StationConverter();

            for (OuterStation station : stations) {
                result.add(converter.convert(station));
            }

            return result;
        }
    }


    public final int mId;

    public OuterStation(final int id) {
        mId = id;
    }

}
