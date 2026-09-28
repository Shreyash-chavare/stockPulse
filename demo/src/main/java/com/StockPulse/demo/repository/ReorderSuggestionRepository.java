package com.StockPulse.demo.repository;

import com.StockPulse.demo.entity.ReorderSuggestion;
import com.StockPulse.demo.entity.SuggestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReorderSuggestionRepository extends JpaRepository<ReorderSuggestion, Long> {
    List<ReorderSuggestion> findByStatusOrderByCreatedAtAsc(SuggestionStatus status);
    List<ReorderSuggestion> findByProductIdAndStatus(String productId, SuggestionStatus status);
}