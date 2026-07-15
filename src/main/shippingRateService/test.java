package shippingRateService;

public class test {
    public static void main(String[] args) {

        ShippingRateService service = new ShippingRateService();

        Shipment shipment = new Shipment(
                3.5,
                DestinationZone.NATIONAL,
                LoyaltyStatus.SILVER
        );

        double cost = service.calculateRate(shipment);

        System.out.println("Final Shipping Cost: RM " + cost);
    }
}
