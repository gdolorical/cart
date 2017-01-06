package com.amaysim.cart.core;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.amaysim.cart.exception.InvalidPromoCodeException;
import com.amaysim.cart.pricing.BundleFreePricingStrategy;
import com.amaysim.cart.pricing.BundlePricingStrategy;
import com.amaysim.cart.pricing.BuyXGetYPricingStrategy;
import com.amaysim.cart.pricing.NormalPricingStrategy;
import com.amaysim.cart.pricing.PricingStrategy;
import com.amaysim.cart.pricing.PromoCodePricingStrategy;
import com.amaysim.cart.util.Constants;
import com.amaysim.cart.util.PromoCodes;

public class ShoppingCartTest {

	ShoppingCart cart;
	Map<String, PricingStrategy> rules;
	Product small;
	Product medium;
	Product large;
	Product oneGb;
	
	@Rule
	public ExpectedException  thrown = ExpectedException.none();
	
	@Before
	public void setup() {
		rules = new HashMap<>();
		small = new Product("ult_small", "Unlimited 1GB", 24.90);
		medium = new Product("ult_medium", "Unlimited 2GB", 29.90);
		large = new Product("ult_large", "Unlimited 5GB", 44.90);
		oneGb = new Product("1gb", "1 GB Data-pack", 9.90);
	}

	@After
	public void tearDown() {
		
	}
	
	@Test
	public void shouldTestScenario1() {
		//Given
		PricingStrategy normal = new NormalPricingStrategy();
		PricingStrategy buyxgety = new BuyXGetYPricingStrategy(small, 3);
		PricingStrategy bundle = new BundlePricingStrategy(large, 3, 39.90);
		PricingStrategy promoCode = new PromoCodePricingStrategy(10);
		rules.put(Constants.NORMAL_PRICING, normal);
		rules.put(Constants.BUYXGETY_PRICING, buyxgety);
		rules.put(Constants.BUNDLE_PRICING, bundle);
		rules.put(Constants.PROMO_CODE_PRICING, promoCode);
		
		//When
		
		cart = new ShoppingCart(rules);
		cart.add(small);
		cart.add(small);
		cart.add(small);
		cart.add(large);
		
		//Then
		assertEquals("3 x Unlimited 1GB\n" + 
			 		 "1 x Unlimited 5GB\n", cart.toString());
		assertEquals(94.70, cart.total(), 0.00D);
		assertEquals("3 x Unlimited 1GB\n" + 
			 	 	 "1 x Unlimited 5GB\n", cart.items());
	}
	
	@Test
	public void shouldTestScenario2() {
		//Given
		PricingStrategy normal = new NormalPricingStrategy();
		PricingStrategy bundle = new BundlePricingStrategy(large, 3, 39.90);
		rules.put(Constants.NORMAL_PRICING, normal);
		rules.put(Constants.BUNDLE_PRICING, bundle);
		
		//When
		cart = new ShoppingCart(rules);
		cart.add(small);
		cart.add(large);
		cart.add(large);
		cart.add(large);
		cart.add(small);
		cart.add(large);
		
		//Then
		assertEquals("2 x Unlimited 1GB\n" + 
				 	 "4 x Unlimited 5GB\n", cart.toString());
		assertEquals(209.40, cart.total(), 0.00D);
		assertEquals("2 x Unlimited 1GB\n" + 
				 	 "4 x Unlimited 5GB\n", cart.items());
	}
	
	@Test
	public void shouldTestScenario3() {
		//Given
		PricingStrategy normal = new NormalPricingStrategy();
		PricingStrategy bundleFree = new BundleFreePricingStrategy(medium, oneGb, 1);
		rules.put(Constants.NORMAL_PRICING, normal);
		rules.put(Constants.BUNDLE_FREE_PRICING, bundleFree);

		//When
		cart = new ShoppingCart(rules);
		cart.add(small);
		cart.add(medium);
		cart.add(medium);
	
		//Then
		assertEquals("1 x Unlimited 1GB\n" + 
				 	 "2 x Unlimited 2GB\n", cart.toString());
		assertEquals(84.70, cart.total(), 0.00D);
		assertEquals("1 x Unlimited 1GB\n" +
					 "2 x Unlimited 2GB\n" +
					 "2 x 1 GB Data-pack\n", cart.items());
	}

	@Test
	public void shouldTestScenario4() {
		//Given
		PricingStrategy normal = new NormalPricingStrategy();
		PricingStrategy promoCode = new PromoCodePricingStrategy(10);
		rules.put(Constants.NORMAL_PRICING, normal);
		rules.put(Constants.PROMO_CODE_PRICING, promoCode);

		//When
		cart = new ShoppingCart(rules);
		cart.add(small);
		cart.add(oneGb, "I<3AMAYSIM");
		
		//Then
		assertEquals("1 x Unlimited 1GB\n" +
		 	 	 "1 x 1 GB Data-pack\n" +
		 	 	 "\'I<3AMAYSIM\' Promo Applied", cart.toString());
		assertEquals(31.32, cart.total(), 0.00D);
		assertEquals("1 x Unlimited 1GB\n" +
			 	 	 "1 x 1 GB Data-pack\n", cart.items());
	}
	
	@Test
	public void shouldListItems() {
		//Given
		rules.put(Constants.NORMAL_PRICING, new NormalPricingStrategy());
		
		//WHen
		cart = new ShoppingCart(rules);
		cart.add(small);
		cart.add(medium);
		cart.add(large);
		cart.add(small);
		cart.add(oneGb);
		
		//Then
		assertEquals("2 x Unlimited 1GB\n" +
				 	 "1 x Unlimited 2GB\n" +
				 	 "1 x Unlimited 5GB\n" +
				 	 "1 x 1 GB Data-pack\n", cart.items());
	}
	
	@Test
	public void shouldTestNormalPricing() {
		//Given
		rules.put(Constants.NORMAL_PRICING, new NormalPricingStrategy());
		
		//When
		cart = new ShoppingCart(rules);
		cart.add(small);
		cart.add(medium);
		cart.add(large);
		cart.add(small);
		cart.add(oneGb);
		
		//Then
		assertEquals(134.5, cart.total(), 0.00D);
	}
	
	@Test
	public void shouldTestInvalidPromoCodePercentage() {
		//Expect
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Percent must be between 0 to 100");
		
		//Given
		PricingStrategy promoCode = new PromoCodePricingStrategy(101);
		rules.put(Constants.PROMO_CODE_PRICING, promoCode);
		
		//When
		//cart = new ShoppingCart(rules);
		//cart.add(oneGb, "I<3AMAYSIM");
	}
	
	@Test
	public void shouldTestAllPromo() {
		//Given
		PricingStrategy normal = new NormalPricingStrategy();
		PricingStrategy buyxgety = new BuyXGetYPricingStrategy(small, 3);
		PricingStrategy bundle = new BundlePricingStrategy(large, 3, 39.90);
		PricingStrategy bundleFree = new BundleFreePricingStrategy(medium, oneGb, 1);
		PricingStrategy promoCode = new PromoCodePricingStrategy(10);
		rules.put(Constants.NORMAL_PRICING, normal);
		rules.put(Constants.BUYXGETY_PRICING, buyxgety);
		rules.put(Constants.BUNDLE_PRICING, bundle);
		rules.put(Constants.BUNDLE_FREE_PRICING, bundleFree);
		rules.put(Constants.PROMO_CODE_PRICING, promoCode);
		
		//When
		cart = new ShoppingCart(rules);
		cart.add(small);
		cart.add(small);
		cart.add(small);
		cart.add(medium);
		cart.add(medium);
		cart.add(large);
		cart.add(large);
		cart.add(large);
		cart.add(large);
		cart.add(oneGb, PromoCodes.ILOVEAMAYSIM.getName());
		
		//Then
		assertEquals("3 x Unlimited 1GB\n" +
					 "2 x Unlimited 2GB\n" +
					 "4 x Unlimited 5GB\n" +
					 "1 x 1 GB Data-pack\n" +
					 "\'I<3AMAYSIM\' Promo Applied", cart.toString());		
		assertEquals(251.19, cart.total(), 0.00D);
		assertEquals("3 x Unlimited 1GB\n" +
				 	 "2 x Unlimited 2GB\n" +
				 	 "4 x Unlimited 5GB\n" +
				 	 "2 x 1 GB Data-pack\n", cart.items());
	}
	
	@Test
	public void shouldTestInvalidPromoCode() {
		//Expect
		thrown.expect(InvalidPromoCodeException.class);
		thrown.expectMessage("Promo Code not available.");
		
		//Given
		PricingStrategy normal = new NormalPricingStrategy();
		PricingStrategy promoCode = new PromoCodePricingStrategy(10);
		rules.put(Constants.NORMAL_PRICING, normal);
		rules.put(Constants.PROMO_CODE_PRICING, promoCode);

		//When
		cart = new ShoppingCart(rules);
		cart.add(small, "I<3PHILIPPINES");
	}
	
	@Test
	public void shouldReturnZeroWhenNoProductIsAdded() {
		//Given
		PricingStrategy normal = new NormalPricingStrategy();
		rules.put(Constants.NORMAL_PRICING, normal);
		
		//When
		cart = new ShoppingCart(rules);
		
		//Then
		assertEquals(0.0, cart.total(), 0.00D);
	}
		
}
