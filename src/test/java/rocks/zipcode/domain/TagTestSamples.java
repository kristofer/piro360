package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TagTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Tag getTagSample1() {
        return new Tag().id(1L).title("title1").description("description1");
    }

    public static Tag getTagSample2() {
        return new Tag().id(2L).title("title2").description("description2");
    }

    public static Tag getTagRandomSampleGenerator() {
        return new Tag().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
