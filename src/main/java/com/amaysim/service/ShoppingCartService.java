package com.amaysim.service;

import com.amaysim.core.Product;

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
	double getTotal();
	
	/**
	 * Displays the Expected Cart Items
	 * 
	 * @return String of Cart Items
	 */
	String getCartItems();
	
	
	/**
	 * Provides a mechanism to the check validity of Promo Code
	 * 
	 * @param promoCode
	 * @return <code>Product</code>
	 */
	Product checkPromoCode(String promoCode);
}
