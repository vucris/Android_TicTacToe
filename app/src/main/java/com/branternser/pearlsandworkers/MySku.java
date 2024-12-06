package com.branternser.pearlsandworkers0906;

public enum MySku {

    ONETURN("com.branternser.pearlsandworkers0906.1turn", "US"),
    FIVETURN("com.branternser.pearlsandworkers0906.2turn", "US"),
    TENTURN("com.branternser.pearlsandworkers0906.10turn", "US"),
    FIFTEENTURN("com.branternser.pearlsandworkers0906.15turn", "US"),
    TWELTYTURN("com.branternser.pearlsandworkers0906.20turn", "US");
    private final String sku;
    private final String availableMarkpetplace;

    public static MySku fromSku(final String sku, final String marketplace) {
        if (ONETURN.getSku().equals(sku) && (ONETURN.getAvailableMarketplace() == marketplace || ONETURN.getAvailableMarketplace().equals(marketplace))) {
            return ONETURN;
        }
        if (FIVETURN.getSku().equals(sku) && (FIVETURN.getAvailableMarketplace() == marketplace || FIVETURN.getAvailableMarketplace().equals(marketplace))) {
            return FIVETURN;
        }
        if (TENTURN.getSku().equals(sku) && (TENTURN.getAvailableMarketplace() == marketplace || TENTURN.getAvailableMarketplace().equals(marketplace))) {
            return TENTURN;
        }
        if (FIFTEENTURN.getSku().equals(sku) && (FIFTEENTURN.getAvailableMarketplace() == marketplace || FIFTEENTURN.getAvailableMarketplace().equals(marketplace))) {
            return FIFTEENTURN;
        }
        if (TWELTYTURN.getSku().equals(sku) && (TWELTYTURN.getAvailableMarketplace() == marketplace || TWELTYTURN.getAvailableMarketplace().equals(marketplace))) {
            return TWELTYTURN;
        }
        return null;
    }
    public String getSku() {
        return this.sku;
    }
    public String getAvailableMarketplace() {
        return this.availableMarkpetplace;
    }

    private MySku(final String sku, final String availableMarkpetplace) {
        this.sku = sku;
        this.availableMarkpetplace = availableMarkpetplace;
    }

}