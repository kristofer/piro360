package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PiroTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Piro getPiroSample1() {
        return new Piro().id(1L).title("title1").description("description1").s3urltovideo("s3urltovideo1");
    }

    public static Piro getPiroSample2() {
        return new Piro().id(2L).title("title2").description("description2").s3urltovideo("s3urltovideo2");
    }

    public static Piro getPiroRandomSampleGenerator() {
        return new Piro()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .s3urltovideo(UUID.randomUUID().toString());
    }
}
