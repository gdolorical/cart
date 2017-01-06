package com.amaysim.pricing;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amaysim.core.Product;

/**
 * Strategy Interface for Product pricing 
 * 
 * @author gdolorical
 *
 */
public interface PricingStrategy {

	/**
	 * Computes the total price against an item's quantity
	 * 
	 * @param product
	 * @param quantity
	 * @return double price
	 */
	default double getPrice(Product product, int quantity) {
		double total = 0.0;
		if (product != null) {
			total = product.getPrice() * quantity;
		}
		return total;
	}
	
	/**
	 * Returns the Cart Items based on the Items added in the Shopping Cart 
	 * 
	 * @param product
	 * @param quantity
	 * @return Cart Items
	 */
	default Map<Product, Integer> getItems(Product product, int quantity) {
		Map<Product, Integer> items = new LinkedHashMap<>();
		if(product != null) {
			items.put(product, quantity);	
		}
		return items;
	}

}
