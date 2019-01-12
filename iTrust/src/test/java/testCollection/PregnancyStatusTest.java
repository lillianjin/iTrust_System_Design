package testCollection;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;

public class PregnancyStatusTest extends TestCase {
	public void testFromString() throws Exception {
		assertEquals(PregnancyStatus.Initial, PregnancyStatus.fromString("Initial"));
		assertEquals(PregnancyStatus.Complete, PregnancyStatus.fromString("Complete"));
		assertEquals(PregnancyStatus.OfficeVisit, PregnancyStatus.fromString("OfficeVisit"));
		assertEquals(PregnancyStatus.NS, PregnancyStatus.fromString("undefined status"));
	}
}
