package testCollection;

import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import junit.framework.TestCase;

public class DeliveryTypeTest extends TestCase {
	public void testFromString() throws Exception {
		assertEquals(DeliveryType.Vaginal, DeliveryType.fromString("Vaginal Delivery"));
		assertEquals(DeliveryType.VaginalVA, DeliveryType.fromString("Vaginal Delivery Vacuum Assist"));
		assertEquals(DeliveryType.VaginalFA, DeliveryType.fromString("Vaginal Delivery Forceps Assist"));
		assertEquals(DeliveryType.Caesarean, DeliveryType.fromString("Caesarean Section"));
		assertEquals(DeliveryType.Miscarriage, DeliveryType.fromString("Miscarriage"));
		assertEquals(DeliveryType.NS, DeliveryType.fromString("non-existent delivery type"));
	}

}
