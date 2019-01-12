package edu.ncsu.csc.itrust.model.old.enums;

public enum DeliveryType {
    Vaginal("Vaginal Delivery"),
    VaginalVA("Vaginal Delivery Vacuum Assist"),
    VaginalFA("Vaginal Delivery Forceps Assist"),
    Caesarean("Caesarean Section"),
    Miscarriage("Miscarriage"),
	NS("N/S");

    private String name;

    DeliveryType(String name) {
        this.name = name;
    }

    public String getName() {
        if (name == null) return "";
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static DeliveryType fromString(String str) {
        for (DeliveryType delivery : DeliveryType.values()) {
            if (delivery.name.equalsIgnoreCase(str)) {
                return delivery;
            }
        }
        /*throw new IllegalArgumentException("Delivery Type " + str
                + " does not exist");*/
        return NS;
    }
}
