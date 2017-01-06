package com.amaysim.cart.service;

import java.util.Map;

import com.amaysim.cart.core.Product;
import com.amaysim.cart.pricing.PricingStrategy;

/**
 * Shopping Cart Service
 * 
 * @author gdolorical
 *
 */
public interface ShoppingCartService {
	
	/**
	 * Computes the total amount of items added
	 * 
	 * @return double Total Amount
	 */
	double getTotal(Map<Product, Integer> itemsAdded, Map<String, PricingStrategy> pricingStrategies);
	
	/**
	 * Displays the Expected Cart Items
	 * 
	 * @return String of Cart Items
	 */
	String getCartItems(Map<Product, Integer> itemsAdded, Map<String, PricingStrategy> pricingStrategies);
	
	
	/**
	 * Provides a mechanism to the check validity of Promo Code
	 * 
	 * @param promoCode
	 * @return <code>Product</code>
	 */
	Product checkPromoCode(String promoCode);
	
}
