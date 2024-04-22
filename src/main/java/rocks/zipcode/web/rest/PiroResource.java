package rocks.zipcode.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rocks.zipcode.domain.Piro;
import rocks.zipcode.repository.PiroRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.Piro}.
 */
@RestController
@RequestMapping("/api/piros")
@Transactional
public class PiroResource {

    private final Logger log = LoggerFactory.getLogger(PiroResource.class);

    private static final String ENTITY_NAME = "piro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PiroRepository piroRepository;

    public PiroResource(PiroRepository piroRepository) {
        this.piroRepository = piroRepository;
    }

    /**
     * {@code POST  /piros} : Create a new piro.
     *
     * @param piro the piro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new piro, or with status {@code 400 (Bad Request)} if the piro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Piro> createPiro(@Valid @RequestBody Piro piro) throws URISyntaxException {
        log.debug("REST request to save Piro : {}", piro);
        if (piro.getId() != null) {
            throw new BadRequestAlertException("A new piro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        piro = piroRepository.save(piro);
        return ResponseEntity.created(new URI("/api/piros/" + piro.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, piro.getId().toString()))
            .body(piro);
    }

    /**
     * {@code PUT  /piros/:id} : Updates an existing piro.
     *
     * @param id the id of the piro to save.
     * @param piro the piro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated piro,
     * or with status {@code 400 (Bad Request)} if the piro is not valid,
     * or with status {@code 500 (Internal Server Error)} if the piro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Piro> updatePiro(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Piro piro)
        throws URISyntaxException {
        log.debug("REST request to update Piro : {}, {}", id, piro);
        if (piro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, piro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!piroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        piro = piroRepository.save(piro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, piro.getId().toString()))
            .body(piro);
    }

    /**
     * {@code PATCH  /piros/:id} : Partial updates given fields of an existing piro, field will ignore if it is null
     *
     * @param id the id of the piro to save.
     * @param piro the piro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated piro,
     * or with status {@code 400 (Bad Request)} if the piro is not valid,
     * or with status {@code 404 (Not Found)} if the piro is not found,
     * or with status {@code 500 (Internal Server Error)} if the piro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Piro> partialUpdatePiro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Piro piro
    ) throws URISyntaxException {
        log.debug("REST request to partial update Piro partially : {}, {}", id, piro);
        if (piro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, piro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!piroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Piro> result = piroRepository
            .findById(piro.getId())
            .map(existingPiro -> {
                if (piro.getTitle() != null) {
                    existingPiro.setTitle(piro.getTitle());
                }
                if (piro.getDescription() != null) {
                    existingPiro.setDescription(piro.getDescription());
                }
                if (piro.gets3urltovideo() != null) {
                    existingPiro.sets3urltovideo(piro.gets3urltovideo());
                }
                if (piro.getCreated() != null) {
                    existingPiro.setCreated(piro.getCreated());
                }

                return existingPiro;
            })
            .map(piroRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, piro.getId().toString())
        );
    }

    /**
     * {@code GET  /piros} : get all the piros.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of piros in body.
     */
    @GetMapping("")
    public List<Piro> getAllPiros() {
        log.debug("REST request to get all Piros");
        return piroRepository.findAll();
    }

    /**
     * {@code GET  /piros/:id} : get the "id" piro.
     *
     * @param id the id of the piro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the piro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Piro> getPiro(@PathVariable("id") Long id) {
        log.debug("REST request to get Piro : {}", id);
        Optional<Piro> piro = piroRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(piro);
    }

    /**
     * {@code DELETE  /piros/:id} : delete the "id" piro.
     *
     * @param id the id of the piro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePiro(@PathVariable("id") Long id) {
        log.debug("REST request to delete Piro : {}", id);
        piroRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
