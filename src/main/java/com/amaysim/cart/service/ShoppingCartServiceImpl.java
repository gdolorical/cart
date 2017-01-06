package com.amaysim.cart.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amaysim.cart.core.Product;
import com.amaysim.cart.pricing.PricingStrategy;
import com.amaysim.cart.util.Constants;
import com.amaysim.cart.util.PromoCodes;

/**
 * Shopping Cart Service Implementation
 * 
 * @author gdolorical
 *
 */
public class ShoppingCartServiceImpl implements ShoppingCartService {
	
	private static final ShoppingCartServiceImpl instance = new ShoppingCartServiceImpl();
	
	protected ShoppingCartServiceImpl() {
	}
	
	public static ShoppingCartServiceImpl getInstance() {
		return instance;
	}

	@Override
	public double getTotal(Map<Product, Integer> itemsAdded, Map<String, PricingStrategy> pricingStrategies) {
		double total = 0.0;
		Product promo = null;
		PricingStrategy strategy;
		
		for (Map.Entry<Product, Integer> entry : itemsAdded.entrySet()) {
		
			Product product = entry.getKey();
			String code = product.getCode();
			Integer quantity = entry.getValue();
			
			strategy = pricingStrategies.get(Constants.NORMAL_PRICING);
			
			if ("ult_small".equals(code)) {
				if (quantity >= 3) {
					strategy = pricingStrategies.get(Constants.BUYXGETY_PRICING);
				}
				total += strategy.getPrice(product, quantity);
			} 
			
			if ("ult_medium".equals(code)) {
				if(pricingStrategies.get(Constants.BUNDLE_FREE_PRICING) != null) {
					strategy = pricingStrategies.get(Constants.BUNDLE_FREE_PRICING);
					strategy.getPrice(product, quantity);
				}
				total += strategy.getPrice(product, quantity);
			} 
			
			if ("ult_large".equals(code)) {
				if (itemsAdded.get(product) > 3) {
					strategy = pricingStrategies.get(Constants.BUNDLE_PRICING);
				} 
				total += strategy.getPrice(product, quantity);
			}
			
			if ("1gb".equals(code)) {
				total += strategy.getPrice(product, quantity);
			}
			
			if ("promo".equals(code)) {
				promo = product;
			}
		}
		
		if(promo != null) {
			strategy = pricingStrategies.get(Constants.PROMO_CODE_PRICING);
			promo.setPrice(total);
			total = strategy.getPrice(promo, 1); 
		}
		
		return total;
	}

	@Override
	public Product checkPromoCode(String promoCode) {
		Product promo = null;
			
		for (PromoCodes code : PromoCodes.values()) {
			if(code.equalsName(promoCode)) {
				String promoName = "\'" + code.getName() + "\'" + " Promo Applied";
				promo = new Product("promo", promoName, code.getPercent());
				break;
			}
		}
		
		return promo;
	}

	@Override
	public String getCartItems(Map<Product, Integer> itemsAdded, Map<String, PricingStrategy> pricingStrategies) {
		
		Map<Product, Integer> cartItems = removePromo(itemsAdded);
		
		for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
			
			Product product = entry.getKey();
			String code = product.getCode();
			Integer quantity = entry.getValue();
			
			if ("ult_medium".equals(code) && pricingStrategies.get(Constants.BUNDLE_FREE_PRICING) != null) {
				PricingStrategy strategy = pricingStrategies.get(Constants.BUNDLE_FREE_PRICING);					
				cartItems.putAll(strategy.getItems(product, quantity));	
			} 
		}
		
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<Product, Integer> productMap : cartItems.entrySet()) { 
			builder.append(productMap.getValue() + " x " + productMap.getKey().getName() + "\n");
		}
		
		return builder.toString();
	}

	/**
	 * Removes the Promo from the Cart Items
	 * 
	 * @param cartItems
	 */
	public Map<Product, Integer> removePromo(Map<Product, Integer> itemsAdded) {
		
		Map<Product, Integer> cartItems = new LinkedHashMap<>();
		
		for (Map.Entry<Product, Integer> entry : itemsAdded.entrySet()) {
			Product product = entry.getKey();
			Integer quantity = entry.getValue();
			
			if(!product.getName().contains("Promo Applied")) {
				cartItems.put(product, quantity);
			}
		}
		return cartItems;
	}
	
}
