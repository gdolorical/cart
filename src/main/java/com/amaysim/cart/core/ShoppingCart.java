package com.amaysim.cart.core;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amaysim.cart.exception.InvalidPromoCodeException;
import com.amaysim.cart.pricing.PricingStrategy;
import com.amaysim.cart.service.ShoppingCartService;
import com.amaysim.cart.service.ShoppingCartServiceImpl;

/**
 * Main Application Shopping Cart 
 * 
 * @author gdolorical
 *
 */
public class ShoppingCart {

	private Map<Product, Integer> itemsAdded;
	private Map<String, PricingStrategy> pricingStrategies;
	
	private static final ShoppingCartService cartService = ShoppingCartServiceImpl.getInstance();
	
	public ShoppingCart(Map<String, PricingStrategy> pricingStrategies) {
		this.pricingStrategies = pricingStrategies;
		this.itemsAdded = new LinkedHashMap<>();
	}
	
	/**
	 * Adds {@link Product} to shopping cart
	 * 
	 * @param product
	 */
	public void add(Product product) {
		 int previousQuantity = itemsAdded.containsKey(product) ? itemsAdded.get(product) : 0;
		 itemsAdded.put(product, previousQuantity + 1);
	}
	
	/**
	 * Adds {@link Product} to shopping cart with Promo Code
	 * 
	 * @param product
	 * @param promoCode
	 */
	public void add(Product product, String promoCode) {

		Product promo = cartService.checkPromoCode(promoCode);
		
		if (promo != null) {
			itemsAdded.put(product, 1);
			itemsAdded.put(promo, 1);
		} else {
			throw new InvalidPromoCodeException("Promo Code not available.");
		}
	}
	
	/**
	 * Returns the total price based on the {@link Product} added and Pricing Promotion
	 * 
	 * @return double total 
	 */
	public double total() {
		return Math.ceil(cartService.getTotal(itemsAdded, pricingStrategies) * 100) / 100;
	}

	/**
	 * Returns the Expected Cart Items which includes either includes a free bundled {@link Product}
	 * 
	 * @return String
	 */
	public String items() {
		return cartService.getCartItems(itemsAdded, pricingStrategies);
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		
		if (itemsAdded != null) {
			for (Map.Entry<Product, Integer> productMap : itemsAdded.entrySet()) {
				 if ("promo".equals(productMap.getKey().getCode())) {
					 builder.append(productMap.getKey().getName());
				 } else {
					 builder.append(productMap.getValue() + " x " + productMap.getKey().getName() + "\n");
				 }
			}
		}
		return builder.toString();
	}
}
