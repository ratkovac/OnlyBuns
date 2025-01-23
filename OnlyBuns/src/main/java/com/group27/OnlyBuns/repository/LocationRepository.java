package com.group27.OnlyBuns.repository;

import com.group27.OnlyBuns.model.Location;
import com.group27.OnlyBuns.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
