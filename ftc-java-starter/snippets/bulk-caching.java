// FRAGMENT - not an OpMode. Paste this into your own file.

// Bulk reads. Fixes a slow loop.
//
// Every hardware read normally costs a separate round trip to the hub.
// Reading four drive encoders plus a lift encoder five times a loop can
// cost you 20+ ms. Bulk caching reads EVERYTHING in one trip, once.
//
// MANUAL mode: you decide when the cache refreshes. Call clearBulkCache()
// once at the top of every loop and every read after it is free.

import com.qualcomm.hardware.lynx.LynxModule;
import java.util.List;

// --- before waitForStart() ---
List<LynxModule> hubs = hardwareMap.getAll(LynxModule.class);
for (LynxModule hub : hubs) {
    hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
}

// --- first line inside the loop ---
while (opModeIsActive()) {
    for (LynxModule hub : hubs) hub.clearBulkCache();

    // everything below now reads from the cache
    int a = frontLeft.getCurrentPosition();
    int b = backLeft.getCurrentPosition();
    // ...
}
