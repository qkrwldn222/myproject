package com.sist.model;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.sist.dao.*;
public class Model {
	public void databoard_list(HttpServletRequest request){
		// list.jsp 처리
		String page=request.getParameter("page");
		if(page==null){
			page="1";
		}
		int curpage = Integer.parseInt(page);
		DataBoardDAO dao  = new DataBoardDAO();
		List<DataBoardVO> list = dao.databoardListData(curpage);
		int totalpage = dao.databoardTotalPage();
		request.setAttribute("curpage", curpage);
		request.setAttribute("list", list);
		request.setAttribute("totalpage", totalpage);
	}
	public void databoard_insert_ok(HttpServletRequest request,HttpServletResponse response){
		try {
			request.setCharacterEncoding("UTF-8");
			String path="c:\\upload";
			String enctype="UTF-8";
			int size=100*1024*1024;
			MultipartRequest mr = new MultipartRequest(request, path,size,enctype,new DefaultFileRenamePolicy());
																					// DefaultFileRenamePolicy 같은 파일명 => a.jpg => a1.jpg
			String name=mr.getParameter("name");
			String subject=mr.getParameter("subject");
			String content=mr.getParameter("content");
			String pwd=mr.getParameter("pwd");
			DataBoardVO vo = new DataBoardVO();
			vo.setName(name);
			vo.setSubject(subject);
			vo.setContent(content);
			vo.setPwd(pwd);
			String filename=mr.getOriginalFileName("upload");
			if(filename==null){ //파일 없을 경우
				vo.setFilename("");
				vo.setFilesize(0);
			}else{
				File file= new File(path+"\\"+filename);
				vo.setFilename(filename);
				vo.setFilesize((int)file.length());
			}
			DataBoardDAO dao  = new DataBoardDAO();
			dao.databoardInsert(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void databoard_detail(HttpServletRequest request){
		String no= request.getParameter("no");
		String curpage = request.getParameter("page");
		DataBoardDAO dao = new DataBoardDAO();
		DataBoardVO vo = dao.databoardDetailData(Integer.parseInt(no));
		//받은 데이터 JSP로 전송
		request.setAttribute("vo", vo);
		request.setAttribute("curpage", curpage);
		
		List<DataBoardReplyVO> list =dao.databoardReplyData(Integer.parseInt(no));
		request.setAttribute("list", list);
		request.setAttribute("len", list.size());
	}
	//다운로드 처리	
	
	public void databoard_updateData(HttpServletRequest request){
		String no =request.getParameter("no");
		String page = request.getParameter("page");
		DataBoardDAO dao = new DataBoardDAO();
		DataBoardVO vo = dao.databoardupdateData(Integer.parseInt(no));
		request.setAttribute("vo",vo);
		request.setAttribute("curpage", page);
	}
	public void databoard_update_ok(HttpServletRequest request){
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String name=request.getParameter("name");
		String subject=request.getParameter("subject");
		String content=request.getParameter("content");
		String pwd=request.getParameter("pwd");
		String no = request.getParameter("no");
		String page= request.getParameter("page");
		DataBoardVO vo = new DataBoardVO();
		vo.setNo(Integer.parseInt(no));
		vo.setName(name);
		vo.setSubject(subject);
		vo.setContent(content);
		vo.setPwd(pwd);
		DataBoardDAO dao =new DataBoardDAO();
		boolean bCheck = dao.databoardUpdate(vo);
		request.setAttribute("bCheck", bCheck);
		request.setAttribute("page", page);
		request.setAttribute("no", no);
	}
	
	public void login(HttpServletRequest request){
		String id =request.getParameter("id");
		String pwd = request.getParameter("pwd");
		DataBoardDAO dao = new DataBoardDAO();
		String result = dao.isLogin(id, pwd);
		
		if(!(result.equals("noid")||result.equals("nopwd"))){
			HttpSession session=request.getSession();
			session.setAttribute("id", id);
			session.setAttribute("name", result);
		}
		request.setAttribute("result", result);
	}
	
	public void reply_insert(HttpServletRequest request){
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {}
		
		String msg=request.getParameter("msg");
		String bno=request.getParameter("bno");
		String page=request.getParameter("page");
		HttpSession session = request.getSession();
		String id =(String)session.getAttribute("id");
		String name =(String)session.getAttribute("name");
		System.out.println(name);
		
		DataBoardReplyVO vo = new DataBoardReplyVO();
		
		vo.setId(id);
		vo.setName(name);
		vo.setBno(Integer.parseInt(bno));
		vo.setMsg(msg);
		request.setAttribute("bno", bno);
		request.setAttribute("page", page);
		DataBoardDAO dao = new DataBoardDAO();
		dao.replyInsert(vo);
		
	}
	
	public void reply_delete(HttpServletRequest request){
		String no = request.getParameter("no");
		String page = request.getParameter("page");
		String bno=request.getParameter("bno");
		
		request.setAttribute("no", bno);
		request.setAttribute("page", page);
		
		DataBoardDAO dao = new DataBoardDAO();
		dao.replyDelete(Integer.parseInt(no));
	}
	
	public void reply_update(HttpServletRequest request){
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {}
		String no = request.getParameter("no");
		String page = request.getParameter("page");
		String bno=request.getParameter("bno");
		String msg=request.getParameter("msg");
		
		DataBoardDAO dao = new DataBoardDAO();
		dao.replyUpdate(Integer.parseInt(no), msg);
		request.setAttribute("no", bno);
		request.setAttribute("page", page);
		
		
		
	}
}
