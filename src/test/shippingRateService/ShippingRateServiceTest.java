package shippingRateService;

import org.junit.Test;
import org.junit.runner.RunWith;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class ShippingRateServiceTest {

	/**
	 * Assumptions:
	 * 1. Zone and LoyaltyStatus are always provided as valid enum values (null not tested).
	 * 2. If weight is invalid, InvalidShipmentException is thrown immediately.
	 */
	
	ShippingRateService service = new ShippingRateService();
	
	// Section 1: EP-Valid Weight Ranges
	//Assumption: Zone = LOCAL, LoyaltyStatus = NONE for simplicity in testing weight-based cost.
	
	private Object[] epValidParams() {
		return new Object[] {
				new Object[] {0.5, 10.0},
				new Object[] {3.0, 20.0},
				new Object[] {7.0, 35.0},
				new Object[] {30.0, 50.0}
		};
	}
	@Test
	@Parameters(method = "epValidParams")
	public void WeightRangeValidEP(double weight, double expectedResult) {
		Shipment shipment = new Shipment(weight, DestinationZone.LOCAL, LoyaltyStatus.NONE);
		double actualResult = service.calculateRate(shipment);
		assertEquals(expectedResult, actualResult, 0.001);
	}
	
	// Section 2: EP-Invalid Weight Ranges
	private Object[] epInvalidParams() {
		return new Object[] {
				new Object[] {0.0},
				new Object[] {100.0}
		};
	}
	@Test(expected = InvalidShipmentException.class)
	@Parameters(method = "epInvalidParams")
	public void WeightRangeInvalidEP(double weight) {
		Shipment shipment = new Shipment(weight, DestinationZone.LOCAL, LoyaltyStatus.NONE);
		service.calculateRate(shipment);
	}
	
	// Section 3: BVA - Valid Weight Ranges
	private Object[] bvaValidParams() {
		return new Object[] {
				new Object[] {0.1, 10.0},
				new Object[] {1.0, 10.0},
				new Object[] {1.1, 20.0},
				new Object[] {5.0, 20.0},
				new Object[] {5.1, 35.0},
				new Object[] {10.0, 35.0},
				new Object[] {10.1, 50.0},
				new Object[] {50.0, 50.0}
		};
	}
	@Test
	@Parameters(method = "bvaValidParams")
	public void WeightRangeValidBVA(double weight, double expectedResult) {
		Shipment shipment = new Shipment(weight, DestinationZone.LOCAL, LoyaltyStatus.NONE);
		double actualResult = service.calculateRate(shipment);
		assertEquals(expectedResult, actualResult, 0.001);
	}
	
	// Section 4: BVA - Invalid Weight Ranges
	private Object[] bvaInvalidParams() {
		return new Object[] {
				new Object[] {0.0},
				new Object[] {50.1}
		};
	}
	@Test(expected = InvalidShipmentException.class)
	@Parameters(method = "bvaInvalidParams")
	public void WeightRangeInvalidBVA(double weight) {
		Shipment shipment = new Shipment(weight, DestinationZone.LOCAL, LoyaltyStatus.NONE);
		service.calculateRate(shipment);
	}
	
	//Section 5: Decision Table Testing
	 private Object[] dtParams() {
	        return new Object[]{
	 
	            // ── R1: LOCAL, NONE (multiplier=1.0, discount=0%) ──────────────
	            new Object[]{0.0,  "LOCAL", "NONE", -1.0},   // excp.
	            new Object[]{0.1,  "LOCAL", "NONE", 10.0},
	            new Object[]{1.0,  "LOCAL", "NONE", 10.0},
	            new Object[]{1.1,  "LOCAL", "NONE", 20.0},
	            new Object[]{5.0,  "LOCAL", "NONE", 20.0},
	            new Object[]{5.1,  "LOCAL", "NONE", 35.0},
	            new Object[]{10.0, "LOCAL", "NONE", 35.0},
	            new Object[]{10.1, "LOCAL", "NONE", 50.0},
	            new Object[]{50.0, "LOCAL", "NONE", 50.0},
	            new Object[]{50.1, "LOCAL", "NONE", -1.0},   // excp.
	 
	            // ── R2: LOCAL, SILVER (multiplier=1.0, discount=5%) ───────────
	            new Object[]{0.0,  "LOCAL", "SILVER", -1.0},
	            new Object[]{0.1,  "LOCAL", "SILVER",  9.5},
	            new Object[]{1.0,  "LOCAL", "SILVER",  9.5},
	            new Object[]{1.1,  "LOCAL", "SILVER", 19.0},
	            new Object[]{5.0,  "LOCAL", "SILVER", 19.0},
	            new Object[]{5.1,  "LOCAL", "SILVER", 33.25},
	            new Object[]{10.0, "LOCAL", "SILVER", 33.25},
	            new Object[]{10.1, "LOCAL", "SILVER", 47.5},
	            new Object[]{50.0, "LOCAL", "SILVER", 47.5},
	            new Object[]{50.1, "LOCAL", "SILVER", -1.0},
	 
	            // ── R3: LOCAL, GOLD (multiplier=1.0, discount=10%) ────────────
	            new Object[]{0.0,  "LOCAL", "GOLD", -1.0},
	            new Object[]{0.1,  "LOCAL", "GOLD",  9.0},
	            new Object[]{1.0,  "LOCAL", "GOLD",  9.0},
	            new Object[]{1.1,  "LOCAL", "GOLD", 18.0},
	            new Object[]{5.0,  "LOCAL", "GOLD", 18.0},
	            new Object[]{5.1,  "LOCAL", "GOLD", 31.5},
	            new Object[]{10.0, "LOCAL", "GOLD", 31.5},
	            new Object[]{10.1, "LOCAL", "GOLD", 45.0},
	            new Object[]{50.0, "LOCAL", "GOLD", 45.0},
	            new Object[]{50.1, "LOCAL", "GOLD", -1.0},
	 
	            // ── R4: NATIONAL, NONE (multiplier=1.5, discount=0%) ──────────
	            new Object[]{0.0,  "NATIONAL", "NONE", -1.0},
	            new Object[]{0.1,  "NATIONAL", "NONE", 15.0},
	            new Object[]{1.0,  "NATIONAL", "NONE", 15.0},
	            new Object[]{1.1,  "NATIONAL", "NONE", 30.0},
	            new Object[]{5.0,  "NATIONAL", "NONE", 30.0},
	            new Object[]{5.1,  "NATIONAL", "NONE", 52.5},
	            new Object[]{10.0, "NATIONAL", "NONE", 52.5},
	            new Object[]{10.1, "NATIONAL", "NONE", 75.0},
	            new Object[]{50.0, "NATIONAL", "NONE", 75.0},
	            new Object[]{50.1, "NATIONAL", "NONE", -1.0},
	 
	            // ── R5: NATIONAL, SILVER (multiplier=1.5, discount=5%) ────────
	            new Object[]{0.0,  "NATIONAL", "SILVER", -1.0},
	            new Object[]{0.1,  "NATIONAL", "SILVER", 14.25},
	            new Object[]{1.0,  "NATIONAL", "SILVER", 14.25},
	            new Object[]{1.1,  "NATIONAL", "SILVER", 28.5},
	            new Object[]{5.0,  "NATIONAL", "SILVER", 28.5},
	            new Object[]{5.1,  "NATIONAL", "SILVER", 49.875},
	            new Object[]{10.0, "NATIONAL", "SILVER", 49.875},
	            new Object[]{10.1, "NATIONAL", "SILVER", 71.25},
	            new Object[]{50.0, "NATIONAL", "SILVER", 71.25},
	            new Object[]{50.1, "NATIONAL", "SILVER", -1.0},
	 
	            // ── R6: NATIONAL, GOLD (multiplier=1.5, discount=10%) ─────────
	            new Object[]{0.0,  "NATIONAL", "GOLD", -1.0},
	            new Object[]{0.1,  "NATIONAL", "GOLD", 13.5},
	            new Object[]{1.0,  "NATIONAL", "GOLD", 13.5},
	            new Object[]{1.1,  "NATIONAL", "GOLD", 27.0},
	            new Object[]{5.0,  "NATIONAL", "GOLD", 27.0},
	            new Object[]{5.1,  "NATIONAL", "GOLD", 47.25},
	            new Object[]{10.0, "NATIONAL", "GOLD", 47.25},
	            new Object[]{10.1, "NATIONAL", "GOLD", 67.5},
	            new Object[]{50.0, "NATIONAL", "GOLD", 67.5},
	            new Object[]{50.1, "NATIONAL", "GOLD", -1.0},
	 
	            // ── R7: INTERNATIONAL, NONE (multiplier=2.5, discount=0%) ─────
	            new Object[]{0.0,  "INTERNATIONAL", "NONE", -1.0},
	            new Object[]{0.1,  "INTERNATIONAL", "NONE", 25.0},
	            new Object[]{1.0,  "INTERNATIONAL", "NONE", 25.0},
	            new Object[]{1.1,  "INTERNATIONAL", "NONE", 50.0},
	            new Object[]{5.0,  "INTERNATIONAL", "NONE", 50.0},
	            new Object[]{5.1,  "INTERNATIONAL", "NONE", 87.5},
	            new Object[]{10.0, "INTERNATIONAL", "NONE", 87.5},
	            new Object[]{10.1, "INTERNATIONAL", "NONE", 125.0},
	            new Object[]{50.0, "INTERNATIONAL", "NONE", 125.0},
	            new Object[]{50.1, "INTERNATIONAL", "NONE", -1.0},
	 
	            // ── R8: INTERNATIONAL, SILVER (multiplier=2.5, discount=5%) ───
	            new Object[]{0.0,  "INTERNATIONAL", "SILVER", -1.0},
	            new Object[]{0.1,  "INTERNATIONAL", "SILVER", 23.75},
	            new Object[]{1.0,  "INTERNATIONAL", "SILVER", 23.75},
	            new Object[]{1.1,  "INTERNATIONAL", "SILVER", 47.5},
	            new Object[]{5.0,  "INTERNATIONAL", "SILVER", 47.5},
	            new Object[]{5.1,  "INTERNATIONAL", "SILVER", 83.125},
	            new Object[]{10.0, "INTERNATIONAL", "SILVER", 83.125},
	            new Object[]{10.1, "INTERNATIONAL", "SILVER", 118.75},
	            new Object[]{50.0, "INTERNATIONAL", "SILVER", 118.75},
	            new Object[]{50.1, "INTERNATIONAL", "SILVER", -1.0},
	 
	            // ── R9: INTERNATIONAL, GOLD (multiplier=2.5, discount=10%) ────
	            new Object[]{0.0,  "INTERNATIONAL", "GOLD", -1.0},
	            new Object[]{0.1,  "INTERNATIONAL", "GOLD", 22.5},
	            new Object[]{1.0,  "INTERNATIONAL", "GOLD", 22.5},
	            new Object[]{1.1,  "INTERNATIONAL", "GOLD", 45.0},
	            new Object[]{5.0,  "INTERNATIONAL", "GOLD", 45.0},
	            new Object[]{5.1,  "INTERNATIONAL", "GOLD", 78.75},
	            new Object[]{10.0, "INTERNATIONAL", "GOLD", 78.75},
	            new Object[]{10.1, "INTERNATIONAL", "GOLD", 112.5},
	            new Object[]{50.0, "INTERNATIONAL", "GOLD", 112.5},
	            new Object[]{50.1, "INTERNATIONAL", "GOLD", -1.0},
	        };
	    }
	 
	    /**
	     * Decision Table test — valid cases (expectedCost != -1)
	     */
	    @Test
	    @Parameters(method = "dtParams")
	    public void testDT_AllRules(double weight, String zone, String loyalty, double expectedCost) {
	        Shipment shipment = new Shipment(weight,
	                DestinationZone.valueOf(zone),
	                LoyaltyStatus.valueOf(loyalty));
	 
	        if (expectedCost == -1.0) {
	            // Expect exception for invalid weight
	            try {
	                service.calculateRate(shipment);
	                fail("Expected InvalidShipmentException for weight=" + weight);
	            } catch (InvalidShipmentException e) {
	                // pass
	            }
	        } else {
	            double result = service.calculateRate(shipment);
	            assertEquals(
	                "DT: weight=" + weight + " zone=" + zone + " loyalty=" + loyalty,
	                expectedCost, result, 0.001);
	        }
	    }
	 
	 // Section 6: Exception Message
	    @Test
	    public void testInvalidWeight_ExceptionMessage() {
	        try {
	            Shipment shipment = new Shipment(0.0, DestinationZone.LOCAL, LoyaltyStatus.NONE);
	            service.calculateRate(shipment);
	            fail("Expected InvalidShipmentException");
	        } catch (InvalidShipmentException e) {
	            assertEquals("Weight must be between 0.1kg and 50kg", e.getMessage());
	        }
	    }
	 
	    @Test
	    public void testInvalidWeight_Above_ExceptionMessage() {
	        try {
	            Shipment shipment = new Shipment(100.0, DestinationZone.LOCAL, LoyaltyStatus.NONE);
	            service.calculateRate(shipment);
	            fail("Expected InvalidShipmentException");
	        } catch (InvalidShipmentException e) {
	            assertEquals("Weight must be between 0.1kg and 50kg", e.getMessage());
	        }
	    }
	    
}



