package com.amaysim.cart.pricing;

import java.util.HashMap;
import java.util.Map;

import com.amaysim.cart.core.Product;

/**
 * A promotion pricing strategy where a customer gets a free bundle of {@link Product} 
 * for a specific {@link Product} added.
 * 
 * e.g. Bundle in a free 1 GB Data-pack free-of-charge with every Unlimited 2GB sold
 * 
 * @author gdolorical
 *
 */
public class BundleFreePricingStrategy implements PricingStrategy {
	
	private Product product;
	private Product bundle;
	private int numberOfFree;
	
	public BundleFreePricingStrategy(Product product, Product bundle, int numberOfFree) {
		this.product = product;
		this.bundle = bundle;
		this.numberOfFree = numberOfFree;
	}
	
	@Override
	public Map<Product, Integer> getItems(Product item, int quantity) {
		Map<Product, Integer> freeItems = new HashMap<>();

		if(item != null && product.equals(item)) {
			freeItems.put(this.bundle, this.numberOfFree * quantity);	
		}
		return freeItems;
	}

}
