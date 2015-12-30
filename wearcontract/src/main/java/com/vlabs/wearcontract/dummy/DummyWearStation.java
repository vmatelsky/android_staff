package com.vlabs.wearcontract.dummy;

import com.vlabs.wearcontract.WearStation;

import java.util.Arrays;
import java.util.List;

public class DummyWearStation {

    public static WearStation Dummy1 = new WearStation(new WearStation.Adapter() {
        @Override
        public String getUniqueId() {
            return "LR1469";
        }

        @Override
        public String getName() {
            return "Z100";
        }

        @Override
        public String getListenLink() {
            return "ihr://play/live/1469";
        }

        @Override
        public String getImagePath() {
            return "/images/PN-7505082002197876956";
        }

        @Override
        public boolean getIsFavorite() {
            return false;
        }
    });

    public static WearStation Dummy2 = new WearStation(new WearStation.Adapter() {
        @Override
        public String getUniqueId() {
            return "LRRM2509";
        }

        @Override
        public String getName() {
            return "HOT 99.5";
        }

        @Override
        public String getListenLink() {
            return "ihr://play/live/2509";
        }

        @Override
        public String getImagePath() {
            return "/images/PN-4238286328523247710";
        }

        @Override
        public boolean getIsFavorite() {
            return false;
        }
    });

    public static WearStation Dummy3 = new WearStation(new WearStation.Adapter() {
        @Override
        public String getUniqueId() {
            return "DL2946";
        }

        @Override
        public String getName() {
            return "Rock Running";
        }

        @Override
        public String getListenLink() {
            return "ihr://play/sherpa/custom/original/2946";
        }

        @Override
        public String getImagePath() {
            return "/images/PN-722682260474432563";
        }

        @Override
        public boolean getIsFavorite() {
            return false;
        }
    });

    public static WearStation Dummy4 = new WearStation(new WearStation.Adapter() {
        @Override
        public String getUniqueId() {
            return "LR5371";
        }

        @Override
        public String getName() {
            return "95.5 PLJ";
        }

        @Override
        public String getListenLink() {
            return "ihr://play/live/5371";
        }

        @Override
        public String getImagePath() {
            return "/images/PN6882682208816492607";
        }

        @Override
        public boolean getIsFavorite() {
            return false;
        }
    });

    public static WearStation Dummy5 = new WearStation(new WearStation.Adapter() {
        @Override
        public String getUniqueId() {
            return "LRRM2245";
        }

        @Override
        public String getName() {
            return "106.1 KISS FM";
        }

        @Override
        public String getListenLink() {
            return "ihr://play/live/2245";
        }

        @Override
        public String getImagePath() {
            return "/images/PN7606224893369494086";
        }

        @Override
        public boolean getIsFavorite() {
            return false;
        }
    });

    public static List<WearStation> Dummies = Arrays.asList(Dummy1, Dummy2, Dummy3, Dummy4, Dummy5);

}
