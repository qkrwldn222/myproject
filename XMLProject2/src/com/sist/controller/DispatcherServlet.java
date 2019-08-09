package com.sist.controller;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import com.sist.model.Model;

public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map map=new HashMap();

	public void init(ServletConfig config) throws ServletException {
		String path=config.getInitParameter("contextConfigLocation");
		System.out.println(path);
		try {
			//XML 파서기 생성
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			//파서기 
			DocumentBuilder db=dbf.newDocumentBuilder();
			//xml에 읽은 데이터저장
			Document doc=db.parse(new File(path));
			
			//XML => ROOT를 확인
			Element root = doc.getDocumentElement();
			System.out.println(root.getTagName());
			
			NodeList list=root.getElementsByTagName("bean");
			for(int i=0; i<list.getLength();i++){
				Element bean = (Element)list.item(i);
				String id =bean.getAttribute("id");
				String cls=bean.getAttribute("class");
				Class clsName=Class.forName(cls);
				Object obj = clsName.newInstance();
				map.put(id, obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 사용자 요청
			String cmd =request.getRequestURI();
			cmd=cmd.substring(request.getContextPath().length()+1,cmd.lastIndexOf("."));
			Model model=(Model)map.get(cmd);
			//System.out.println(model);
			String jsp=model.HandlerRequest(request);
			if(jsp.startsWith("redirect")){
				response.sendRedirect(jsp.substring(jsp.indexOf(":")+1));
			}else{
				RequestDispatcher rd = request.getRequestDispatcher(jsp);
				rd.forward(request, response);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
