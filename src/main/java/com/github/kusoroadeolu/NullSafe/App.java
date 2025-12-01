import java.lang.instrument.Instrumentation;

import static com.github.kusoroadeolu.NullSafe.AgentTransformer.transform;
import static net.bytebuddy.agent.ByteBuddyAgent.install;

void main() {
    premain("", install());
}

public static void premain(String args,  Instrumentation instrument) {
    transform(instrument);
}












