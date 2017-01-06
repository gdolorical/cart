package com.amaysim.pricing;

import com.amaysim.core.Product;

/**
 * A promotion pricing strategy where a customer gets discount to the total price
 * with an available Promo Code.
 * 
 * e.g. Adding the promo code 'I<3AMAYSIM' will apply a 10% discount across the board.
 * 
 * @author gdolorical
 *
 */
public class PromoCodePricingStrategy implements PricingStrategy {
	
	private final double priceFactor;

	public PromoCodePricingStrategy(int percent) { 
		if (percent < 0 || percent > 100 ) {
			throw new IllegalArgumentException("percent must be between 0 to 100");
		}
		this.priceFactor = (100 - percent) / 100.0;
	 }
	
	@Override
	public double getPrice(Product item, int quantity) {
		return item.getPrice() * priceFactor;
	}

}
