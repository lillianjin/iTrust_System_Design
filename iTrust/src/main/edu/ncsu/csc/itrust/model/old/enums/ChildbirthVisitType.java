package edu.ncsu.csc.itrust.model.old.enums;

public enum ChildbirthVisitType {
	PreScheduled("Pre-scheduled"),
	ER("ER"),
	NS("N/S");
	
	private String name;
	
	ChildbirthVisitType(String name) {
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
	
	public static ChildbirthVisitType fromString(String str) {
        for (ChildbirthVisitType childbirthVisit : ChildbirthVisitType.values()) {
            if (childbirthVisit.name.equalsIgnoreCase(str)) {
                return childbirthVisit;
            }
        }
//        throw new IllegalArgumentException("Childbirth Visit Type " + str
//                + " does not exist");
        return NS;
    }
	
}
