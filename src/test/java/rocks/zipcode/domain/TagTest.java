package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.PiroTestSamples.*;
import static rocks.zipcode.domain.TagTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void pirosTest() throws Exception {
        Tag tag = getTagRandomSampleGenerator();
        Piro piroBack = getPiroRandomSampleGenerator();

        tag.addPiros(piroBack);
        assertThat(tag.getPiros()).containsOnly(piroBack);

        tag.removePiros(piroBack);
        assertThat(tag.getPiros()).doesNotContain(piroBack);

        tag.piros(new HashSet<>(Set.of(piroBack)));
        assertThat(tag.getPiros()).containsOnly(piroBack);

        tag.setPiros(new HashSet<>());
        assertThat(tag.getPiros()).doesNotContain(piroBack);
    }
}
