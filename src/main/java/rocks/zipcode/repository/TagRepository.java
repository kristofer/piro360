package rocks.zipcode.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.Tag;

/**
 * Spring Data JPA repository for the Tag entity.
 *
 * When extending this class, extend TagRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TagRepository extends TagRepositoryWithBagRelationships, JpaRepository<Tag, Long> {
    @Query("select tag from Tag tag where tag.owner.login = ?#{authentication.name}")
    List<Tag> findByOwnerIsCurrentUser();

    default Optional<Tag> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Tag> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Tag> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
