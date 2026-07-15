package shippingRateService;

public class ShippingRateService {

    public double calculateRate(Shipment shipment) {

        double weight = shipment.getWeight();

        // 1. Weight Validation
        if (weight < 0.1 || weight > 50) {
            throw new InvalidShipmentException("Weight must be between 0.1kg and 50kg");
        }

        // 2. Base Cost (Weight-based)
        double baseCost;

        if (weight <= 1.0) {
            baseCost = 10;
        } else if (weight <= 5.0) {
            baseCost = 20;
        } else if (weight <= 10.0) {
            baseCost = 35;
        } else {
            baseCost = 50;
        }

        // 3. Destination Zone Multiplier
        double multiplier;

        switch (shipment.getZone()) {
            case LOCAL:
                multiplier = 1.0;
                break;
            case NATIONAL:
                multiplier = 1.5;
                break;
            case INTERNATIONAL:
                multiplier = 2.5;
                break;
            default:
                multiplier = 1.0;
        }

        double cost = baseCost * multiplier;

        // 4. Loyalty Discount
        double discountRate;

        switch (shipment.getLoyaltyStatus()) {
            case SILVER:
                discountRate = 0.05;
                break;
            case GOLD:
                discountRate = 0.10;
                break;
            case NONE:
            default:
                discountRate = 0.0;
        }

        cost = cost - (cost * discountRate);

        return cost;
    }
}
