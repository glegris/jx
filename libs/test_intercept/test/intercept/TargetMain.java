package test.intercept;

import jx.zero.*;
import jx.zero.debug.*;

public class TargetMain {
    public static void init(Naming naming) {
	DebugChannel d = (DebugChannel) naming.lookup("DebugChannel0");
	DebugPrintStream out = new DebugPrintStream(new DebugOutputStream(d));
	Debug.out = out;

	// lookup portal
	Daddy daddy = (Daddy) naming.lookup("Daddy");

	// make call
	daddy.hello();
	
    }
}
