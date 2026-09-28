package com.StockPulse.demo.repository;

import com.StockPulse.demo.entity.PricingSuggestion;
import com.StockPulse.demo.entity.SuggestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PricingSuggestionRepository extends JpaRepository<PricingSuggestion, Long> {
    List<PricingSuggestion> findByStatusOrderByCreatedAtAsc(SuggestionStatus status);
    List<PricingSuggestion> findByProductIdAndStatus(String productId, SuggestionStatus status);
}