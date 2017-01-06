package com.amaysim.pricing;

import com.amaysim.core.Product;

/**
 * A promotion pricing strategy where a customer gets a free discount
 * for a specific quantity of <code>Product</code> added. 
 * 
 * e.g. A 3 for 2 deal on Unlimited 1GB Sims.
 * 		If you buy 3 Unlimited 1GB Sims, 
 * 		you will pay the price of 2 only for the first month.
 * 
 * @author gdolorical
 *
 */
public class BuyXGetYPricingStrategy implements PricingStrategy {
	
	private Product product;
	private int buyQuantity;
	
	public BuyXGetYPricingStrategy(Product product, int buyQuantity) {
		this.product = product;
		this.buyQuantity = buyQuantity;
	}

	@Override
	public double getPrice(Product item, int quantity) {
		double total = 0.0;
		if (item != null && item.equals(product)) {
			if (quantity >= buyQuantity) {
				int numberOfLess = quantity / buyQuantity;
				total += (quantity * item.getPrice()) - (product.getPrice() * numberOfLess);
			} else {
				total += quantity * item.getPrice();
			} 
		}
		return total;
	}

}
