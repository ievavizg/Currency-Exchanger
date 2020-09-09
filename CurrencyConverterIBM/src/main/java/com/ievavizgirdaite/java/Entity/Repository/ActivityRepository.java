package com.ievavizgirdaite.java.Entity.Repository;

import com.ievavizgirdaite.java.Entity.Activity;
import com.ievavizgirdaite.java.Entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
