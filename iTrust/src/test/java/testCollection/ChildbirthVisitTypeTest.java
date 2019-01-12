package testCollection;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.model.old.enums.ChildbirthVisitType;

public class ChildbirthVisitTypeTest extends TestCase {
	public void testFromString() throws Exception {
		assertEquals(ChildbirthVisitType.PreScheduled, ChildbirthVisitType.fromString("Pre-scheduled"));
		assertEquals(ChildbirthVisitType.ER, ChildbirthVisitType.fromString("ER"));
		assertEquals(ChildbirthVisitType.NS, ChildbirthVisitType.fromString("undefined type"));
		
	}
}
