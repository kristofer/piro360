package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.PiroTestSamples.*;
import static rocks.zipcode.domain.TagTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class PiroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Piro.class);
        Piro piro1 = getPiroSample1();
        Piro piro2 = new Piro();
        assertThat(piro1).isNotEqualTo(piro2);

        piro2.setId(piro1.getId());
        assertThat(piro1).isEqualTo(piro2);

        piro2 = getPiroSample2();
        assertThat(piro1).isNotEqualTo(piro2);
    }

    @Test
    void tagsTest() throws Exception {
        Piro piro = getPiroRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        piro.addTags(tagBack);
        assertThat(piro.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getPiros()).containsOnly(piro);

        piro.removeTags(tagBack);
        assertThat(piro.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getPiros()).doesNotContain(piro);

        piro.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(piro.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getPiros()).containsOnly(piro);

        piro.setTags(new HashSet<>());
        assertThat(piro.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getPiros()).doesNotContain(piro);
    }
}
