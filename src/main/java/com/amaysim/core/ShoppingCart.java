package com.amaysim.core;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amaysim.exception.InvalidPromoCodeException;
import com.amaysim.pricing.PricingStrategy;
import com.amaysim.service.ShoppingCartServiceImpl;

/**
 * Main Application Shopping Cart 
 * 
 * @author gdolorical
 *
 */
public class ShoppingCart {

	private Map<Product, Integer> itemsAdded;
	private Map<String, PricingStrategy> strategies;
	
	private ShoppingCartServiceImpl cartService;
	
	public ShoppingCart(Map<String, PricingStrategy> pricingStrategies) {
		this.strategies = pricingStrategies;
		this.itemsAdded = new LinkedHashMap<>();
		this.cartService = new ShoppingCartServiceImpl(pricingStrategies, getItems());
	}
	
	public void add(Product product) {
		 int previousQuantity = itemsAdded.containsKey(product) ? itemsAdded.get(product) : 0;
		 itemsAdded.put(product, previousQuantity + 1);
	}
	
	public void add(Product product, String promoCode) {

		Product promo = cartService.checkPromoCode(promoCode);
		
		if (promo != null) {
			itemsAdded.put(product, 1);
			itemsAdded.put(promo, 1);
		} else {
			throw new InvalidPromoCodeException("Promo Code not available.");
		}
	}
	
	public double total() {
		return Math.ceil(cartService.getTotal() * 100) / 100;
	}

	public String items() {
		return cartService.getCartItems();
	}
	
	public Map<Product, Integer> getItems() {
		return itemsAdded;
	}

	public void setItems(Map<Product, Integer> items) {
		this.itemsAdded = items;
	}
	
	public Map<String, PricingStrategy> getStrategies() {
		return strategies;
	}

	public void setStrategies(Map<String, PricingStrategy> strategies) {
		this.strategies = strategies;
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
