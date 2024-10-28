package se.lexicon.marketplaceapi_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.marketplaceapi_springboot.domain.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Transactional
    @Modifying
    @Query("update Profile p set p.country = :country where p.profileId = :id")
    void updateProfileById(@Param("id") Long id, @Param("country") String country);
}
