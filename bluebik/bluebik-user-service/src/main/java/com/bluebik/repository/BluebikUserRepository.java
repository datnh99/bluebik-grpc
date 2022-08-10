package com.bluebik.repository;

import com.bluebik.model.BluebikUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BluebikUserRepository extends JpaRepository<BluebikUser, Long> {
    BluebikUser findBluebikUserById(int id);

    List<BluebikUser> findAllByNameStartsWith(String keyword);
}
