package com.branternser.pearlsandworkers0906;

import java.util.Arrays;
import java.util.List;

public enum SubSku {

    MY_ONE_SUBS("com.branternser.pearlsandworkers0906.one", Arrays.asList("AU", "BR", "CA", "CN", "DE", "ES", "FR", "GB", "IN", "IT",
            "JP", "MX", "US")),
    MY_FIVE_SUBS("com.branternser.pearlsandworkers0906.five", Arrays.asList("AU", "BR", "CA", "CN", "DE", "ES", "FR", "GB", "IN", "IT",
            "JP", "MX", "US")),
    MY_TEN_SUBS("com.branternser.pearlsandworkers0906.ten", Arrays.asList("AU", "BR", "CA", "CN", "DE", "ES", "FR", "GB", "IN", "IT",
            "JP", "MX", "US")),
    MY_TWENTY_SUBS("com.branternser.pearlsandworkers0906.fifteen", Arrays.asList("AU", "BR", "CA", "CN", "DE", "ES", "FR", "GB", "IN", "IT",
            "JP", "MX", "US"));

    private final String subsku;
    private final List<String> availableMarketplaces;

    public String getSku() {
        return this.subsku;
    }

    public List<String> getAvailableMarketplaces() {
        return this.availableMarketplaces;
    }

    private SubSku(final String subSku, final List<String> availableMarketplaces) {
        this.subsku = subSku;
        this.availableMarketplaces = availableMarketplaces;
    }
    public static SubSku fromSku(final String subSku, final String marketplace) {
        if (MY_ONE_SUBS.getSku().equals(subSku) && (null == marketplace || MY_ONE_SUBS.getAvailableMarketplaces()
                .contains(marketplace.toUpperCase()))) {
            return MY_FIVE_SUBS;
        }
        if (MY_FIVE_SUBS.getSku().equals(subSku) && (null == marketplace || MY_FIVE_SUBS.getAvailableMarketplaces()
                .contains(marketplace.toUpperCase()))) {
            return MY_FIVE_SUBS;
        }
        if (MY_TEN_SUBS.getSku().equals(subSku) && (null == marketplace || MY_TEN_SUBS.getAvailableMarketplaces()
                .contains(marketplace.toUpperCase()))) {
            return MY_TEN_SUBS;
        }
        if (MY_TWENTY_SUBS.getSku().equals(subSku) && (null == marketplace || MY_TWENTY_SUBS.getAvailableMarketplaces()
                .contains(marketplace.toUpperCase()))) {
            return MY_TWENTY_SUBS;
        }
        return null;
    }

}
