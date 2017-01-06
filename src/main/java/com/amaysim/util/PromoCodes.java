package com.amaysim.util;

/**
 * Constant Enumeration of Promo Codes
 * 
 * @author gdolorical
 * 
 */
public enum PromoCodes {
	ILOVEAMAYSIM ("I<3AMAYSIM", .10);

    private final String name;
	private final double percent;

	private PromoCodes(String name, double percent) {
        this.name = name;
        this.percent = percent;
    }
	
	public String getName() {
  		return name;
	}
	
	public double getPercent() {
		return percent;
	}
	
    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }
    
}
