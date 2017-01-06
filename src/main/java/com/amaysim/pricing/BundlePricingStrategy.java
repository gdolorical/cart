package com.amaysim.pricing;

import com.amaysim.core.Product;

/**
 * A promotion pricing strategy where a customer gets a discount 
 * for a specific quantity of <code>Product</code> added.
 * 
 * e.g. The Unlimited 5GB sim will have a bulk discount applied; 
 * 	    whereby the price will drop to $39.90 each for the first month, 
 * 		if the customer buys more than 3.
 * 
 * @author gdolorical
 *
 */
public class BundlePricingStrategy implements PricingStrategy {
	
	private Product product;
	private int bundleSize;
	private double discountedAmount;
	
	public BundlePricingStrategy(Product product, int bundleSize, Double discountedAmount) {
		this.product = product;
		this.bundleSize = bundleSize;
		this.discountedAmount = discountedAmount;
	}

	@Override
	public double getPrice(Product item, int quantity) {
		double total = 0.0;
		if (item != null && item.equals(product)) {
			if (quantity > bundleSize) {
				total += quantity * discountedAmount;
			} else {
				total = total + quantity * product.getPrice(); 	 
			}
		}
		return total;
	}

}
