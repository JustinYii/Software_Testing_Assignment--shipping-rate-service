package shippingRateService;

public class Shipment {

    private double weight;
    private DestinationZone zone;
    private LoyaltyStatus loyaltyStatus;

    public Shipment(double weight, DestinationZone zone, LoyaltyStatus loyaltyStatus) {
        this.weight = weight;
        this.zone = zone;
        this.loyaltyStatus = loyaltyStatus;
    }

    public double getWeight() {
        return weight;
    }

    public DestinationZone getZone() {
        return zone;
    }

    public LoyaltyStatus getLoyaltyStatus() {
        return loyaltyStatus;
    }
}