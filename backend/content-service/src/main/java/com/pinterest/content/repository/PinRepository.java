package com.pinterest.content.repository;

import com.pinterest.content.model.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, Long> {

}
