package org.zerock.springex.mapper;

import org.zerock.springex.domain.TodoVO;
import org.zerock.springex.dto.PageRequestDTO;

import java.util.List;

public interface TodoMapper {

    // test용
    String getTime();

    /**
     * VO 데이터 저장
     * @param todoVO
     */
    void insert(TodoVO todoVO);

    /**
     * 모든 정보 리스트로 가져오기
     * @return todoVO
     */
    List<TodoVO> selectAll();

    /**
     * tno:번호로 VO 하나 가져오기
     * @param tno
     * @return TodoVO
     */
    TodoVO selectOne(Long tno);

    /**
     * tno:번호로 데이터 하나 삭제
     * @param tno
     */
    void delete(Long tno);

    /**
     * 데이터 수정하기
     * @param todoVO
     */
    void update(TodoVO todoVO);

    /**
     * 페이징
     * @param pageRequestDTO
     * @return todoVO리스트
     */
    List<TodoVO> selectList(PageRequestDTO pageRequestDTO);

    /**
     * 페이징 번호 카운팅
     * @param pageRequestDTO
     * @return todo레코드 갯수 반환
     */
    int getCount(PageRequestDTO pageRequestDTO);

}

