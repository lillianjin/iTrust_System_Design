package edu.ncsu.csc.itrust.model.old.enums;

public enum PregnancyStatus {
    Initial("Initial"),
    Complete("Complete"),
    OfficeVisit("OfficeVisit"),
	NS("N/S");

    private String name;

    PregnancyStatus(String name) { this.name = name; }

    public String getName() {
        if (name == null) return "";
        return name;
    }

    @Override
    public String toString() { return getName(); }

    public static PregnancyStatus fromString(String str) {
        for (PregnancyStatus preg : PregnancyStatus.values()) {
            if (preg.name.equalsIgnoreCase(str)) {
                return preg;
            }
        }
//        throw new IllegalArgumentException("Pregnancy Status " + str
//                + "does not exist");
        return NS;
    }
}
