package kr.co.ict2.ngg.dao;


import kr.co.ict2.ngg.vo.AIResVO;
import kr.co.ict2.ngg.vo.MemberVO;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {
    public void memberadd(MemberVO vo);
    

    public MemberVO mydetail(int mnum); // 마이페이지
	
	public void myprofileupdate(MemberVO vo); // 프사변경
	
	public int myidcheck(String mnick); // 닉네임 중복체크
	
    public void myupdate(MemberVO vo); // 내 정보 수정하기

    public void delAccount(int mnum);


    public List<AIResVO> myhistory (int mnum);  // 마이 히스토리

    public void delHistory(int rnum); //히스토리 삭제
    
    public void seasonupdate(MemberVO vo);
}
