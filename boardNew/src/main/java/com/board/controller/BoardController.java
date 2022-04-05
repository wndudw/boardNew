package com.board.controller;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.board.domain.BoardVO;
import com.board.domain.Page;
import com.board.domain.ReplyVO;
import com.board.service.BoardService;
import com.board.service.ReplyService;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	@Inject
	BoardService service;
	
	@Inject
	ReplyService ReplyService;
	
	
	@GetMapping("/list")
	public void getList(Model model) throws Exception {
		//model은 컨트롤러와 뷰를 연결시켜주는 역할
		
		List<BoardVO> list = null;
		list = service.list();
		
		model.addAttribute("list", list);
	}
	
	@GetMapping("/write")
	public void getWrite() throws Exception{
		
	}
	
	@PostMapping("/write")
	public String postWrite(BoardVO vo) throws Exception{
		service.write(vo);
		
		return "redirect:/board/list";
	}
	
	//게시글 조회
	@GetMapping("/view")
	public void getView(@RequestParam("bno") int bno, Model model) throws Exception{
		//@RequestParam : url의 쿼리스트링에 있는 특정값을 가져올수있는것. -> 쿼리스트링 bno를 찾아 그 값을 int bno에 넣어준다
		BoardVO vo = service.view(bno);
		
		model.addAttribute("view", vo);
		
		//댓글 조회
		List<ReplyVO> reply = null;
		reply = ReplyService.list(bno);
		model.addAttribute("reply",reply);
	}
	
	@GetMapping("/modify")
	public void getModify(@RequestParam("bno") int bno, Model model) throws Exception{
		BoardVO vo = service.view(bno);
		
		model.addAttribute("modify", vo);
	}
	
	@PostMapping("/modify")
	public String postModify(BoardVO vo) throws Exception{
		
		service.modify(vo);
		
		return "redirect:/board/view?bno=" + vo.getBno();
	}
	
	@GetMapping("/delete")
	public String getDelete(@RequestParam("bno") int bno) throws Exception{
		
		service.delete(bno);
		
		return "redirect:/board/list";
	}
	
	
	@GetMapping("/listPage")
	public void getListPage(Model model, @RequestParam("num") int num) throws Exception {
		Page page = new Page();
		
		page.setNum(num);
		page.setCount(service.count());		
		
		List<BoardVO> list = null; 
		list = service.listPage(page.getDisplayPost(), page.getPostNum());
		
		model.addAttribute("list", list);
		
		/*
		model.addAttribute("pageNum", page.getPageNum());
		
		model.addAttribute("startPageNum", page.getStartPageNum());
		model.addAttribute("endPageNum", page.getEndPageNum());
		 
		model.addAttribute("prev", page.getPrev());
		model.addAttribute("next", page.getNext());
		*/
		
		model.addAttribute("page", page);
		
		model.addAttribute("select", num);
		
		/*
		// 게시물 총 갯수
		int count = service.count();
		
		// 한 페이지에 출력할 게시물 갯수
		int postNum = 10;
		
		// 하단 페이징 번호 ([ 게시물 총 갯수 ÷ 한 페이지에 출력할 갯수 ]의 올림)
		int pageNum = (int)Math.ceil((double)count/postNum);
		
		// 출력할 게시물
		int displayPost = (num - 1) * postNum;
					
		
		// 한번에 표시할 페이징 번호의 갯수
		int pageNum_cnt = 10;
		
		// 표시되는 페이지 번호 중 마지막 번호
		int endPageNum = (int)(Math.ceil((double)num / (double)pageNum_cnt) * pageNum_cnt);
		
		// 표시되는 페이지 번호 중 첫번째 번호
		int startPageNum = endPageNum - (pageNum_cnt - 1);
		// 마지막 번호 재계산
		int endPageNum_tmp = (int)(Math.ceil((double)count / (double)pageNum_cnt));
			
		if(endPageNum > endPageNum_tmp) {
			endPageNum = endPageNum_tmp;
		}
		
		boolean prev = startPageNum == 1 ? false : true; 
		boolean next = endPageNum * pageNum_cnt >= count ? false : true;
		
		List<BoardVO> list = null; 
		list = service.listPage(displayPost, postNum);
		model.addAttribute("list", list);		 
		model.addAttribute("pageNum", pageNum);
		
		// 시작 및 끝 번호
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);
		// 이전 및 다음 
		model.addAttribute("prev", prev);
		model.addAttribute("next", next);		 
		
		// 현재 페이지
		model.addAttribute("select", num);
		*/
		
		}
	
	//게시물 목록 + 페이징 추가 + 검색
	@GetMapping("/listPageSearch")
	public void getListPageSearch(Model model, @RequestParam("num") int num,@RequestParam(value="searchType", required = false, defaultValue = "title") String searchType, 
			@RequestParam(value="keyword", required = false, defaultValue = "") String keyword) throws Exception{
		Page page = new Page();
		
		page.setNum(num);
		//page.setCount(service.count());		
		page.setCount(service.searchCount(searchType, keyword));
		
		//검색타입과 검색어
		//page.setSearchTypeKeyword(searchType, keyword);
		page.setSearchType(searchType);
		page.setKeyword(keyword);
		
		List<BoardVO> list = null; 
		//list = service.listPage(page.getDisplayPost(), page.getPostNum());
		list = service.listPageSearch(page.getDisplayPost(), page.getPostNum(), searchType, keyword);
		
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("select", num);
		
		//model.addAttribute("searchType", searchType);
		//model.addAttribute("keyword", keyword);
	}
	
}
