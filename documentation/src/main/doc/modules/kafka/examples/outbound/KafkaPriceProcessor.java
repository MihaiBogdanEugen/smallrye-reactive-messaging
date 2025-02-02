package outbound;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class KafkaPriceProcessor {

    // tag::code[]
    @Incoming("in")
    @Outgoing("out")
    public double process(int in) {
        return in * 0.88;
    }
    // end::code[]

}
