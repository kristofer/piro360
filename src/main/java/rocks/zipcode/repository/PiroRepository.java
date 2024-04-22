package rocks.zipcode.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.Piro;

/**
 * Spring Data JPA repository for the Piro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PiroRepository extends JpaRepository<Piro, Long> {
    @Query("select piro from Piro piro where piro.owner.login = ?#{authentication.name}")
    List<Piro> findByOwnerIsCurrentUser();
}
