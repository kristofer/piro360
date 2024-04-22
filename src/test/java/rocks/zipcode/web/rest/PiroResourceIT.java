package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.PiroAsserts.*;
import static rocks.zipcode.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.IntegrationTest;
import rocks.zipcode.domain.Piro;
import rocks.zipcode.repository.PiroRepository;

/**
 * Integration tests for the {@link PiroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PiroResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_URLTOVIDEO = "AAAAAAAAAA";
    private static final String UPDATED_S_3_URLTOVIDEO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/piros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PiroRepository piroRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPiroMockMvc;

    private Piro piro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Piro createEntity(EntityManager em) {
        Piro piro = new Piro()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .s3urltovideo(DEFAULT_S_3_URLTOVIDEO)
            .created(DEFAULT_CREATED);
        return piro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Piro createUpdatedEntity(EntityManager em) {
        Piro piro = new Piro()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .s3urltovideo(UPDATED_S_3_URLTOVIDEO)
            .created(UPDATED_CREATED);
        return piro;
    }

    @BeforeEach
    public void initTest() {
        piro = createEntity(em);
    }

    @Test
    @Transactional
    void createPiro() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Piro
        var returnedPiro = om.readValue(
            restPiroMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(piro)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Piro.class
        );

        // Validate the Piro in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPiroUpdatableFieldsEquals(returnedPiro, getPersistedPiro(returnedPiro));
    }

    @Test
    @Transactional
    void createPiroWithExistingId() throws Exception {
        // Create the Piro with an existing ID
        piro.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(piro)))
            .andExpect(status().isBadRequest());

        // Validate the Piro in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        piro.setTitle(null);

        // Create the Piro, which fails.

        restPiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(piro)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPiros() throws Exception {
        // Initialize the database
        piroRepository.saveAndFlush(piro);

        // Get all the piroList
        restPiroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(piro.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].s3urltovideo").value(hasItem(DEFAULT_S_3_URLTOVIDEO)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }

    @Test
    @Transactional
    void getPiro() throws Exception {
        // Initialize the database
        piroRepository.saveAndFlush(piro);

        // Get the piro
        restPiroMockMvc
            .perform(get(ENTITY_API_URL_ID, piro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(piro.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.s3urltovideo").value(DEFAULT_S_3_URLTOVIDEO))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPiro() throws Exception {
        // Get the piro
        restPiroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPiro() throws Exception {
        // Initialize the database
        piroRepository.saveAndFlush(piro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the piro
        Piro updatedPiro = piroRepository.findById(piro.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPiro are not directly saved in db
        em.detach(updatedPiro);
        updatedPiro.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).s3urltovideo(UPDATED_S_3_URLTOVIDEO).created(UPDATED_CREATED);

        restPiroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPiro.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPiro))
            )
            .andExpect(status().isOk());

        // Validate the Piro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPiroToMatchAllProperties(updatedPiro);
    }

    @Test
    @Transactional
    void putNonExistingPiro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piro.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPiroMockMvc
            .perform(put(ENTITY_API_URL_ID, piro.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(piro)))
            .andExpect(status().isBadRequest());

        // Validate the Piro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPiro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPiroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(piro))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPiro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPiroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(piro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Piro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePiroWithPatch() throws Exception {
        // Initialize the database
        piroRepository.saveAndFlush(piro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the piro using partial update
        Piro partialUpdatedPiro = new Piro();
        partialUpdatedPiro.setId(piro.getId());

        restPiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPiro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPiro))
            )
            .andExpect(status().isOk());

        // Validate the Piro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPiroUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPiro, piro), getPersistedPiro(piro));
    }

    @Test
    @Transactional
    void fullUpdatePiroWithPatch() throws Exception {
        // Initialize the database
        piroRepository.saveAndFlush(piro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the piro using partial update
        Piro partialUpdatedPiro = new Piro();
        partialUpdatedPiro.setId(piro.getId());

        partialUpdatedPiro
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .s3urltovideo(UPDATED_S_3_URLTOVIDEO)
            .created(UPDATED_CREATED);

        restPiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPiro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPiro))
            )
            .andExpect(status().isOk());

        // Validate the Piro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPiroUpdatableFieldsEquals(partialUpdatedPiro, getPersistedPiro(partialUpdatedPiro));
    }

    @Test
    @Transactional
    void patchNonExistingPiro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piro.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPiroMockMvc
            .perform(patch(ENTITY_API_URL_ID, piro.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(piro)))
            .andExpect(status().isBadRequest());

        // Validate the Piro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPiro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(piro))
            )
            .andExpect(status().isBadRequest());

        // Validate the Piro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPiro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        piro.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPiroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(piro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Piro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePiro() throws Exception {
        // Initialize the database
        piroRepository.saveAndFlush(piro);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the piro
        restPiroMockMvc
            .perform(delete(ENTITY_API_URL_ID, piro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return piroRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Piro getPersistedPiro(Piro piro) {
        return piroRepository.findById(piro.getId()).orElseThrow();
    }

    protected void assertPersistedPiroToMatchAllProperties(Piro expectedPiro) {
        assertPiroAllPropertiesEquals(expectedPiro, getPersistedPiro(expectedPiro));
    }

    protected void assertPersistedPiroToMatchUpdatableProperties(Piro expectedPiro) {
        assertPiroAllUpdatablePropertiesEquals(expectedPiro, getPersistedPiro(expectedPiro));
    }
}
