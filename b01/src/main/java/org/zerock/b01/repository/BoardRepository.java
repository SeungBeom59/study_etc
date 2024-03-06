package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.zerock.b01.domain.Board;
import org.zerock.b01.repository.search.BoardSearch;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @Query(value = "select now()" , nativeQuery = true)
    String getTime();
}
