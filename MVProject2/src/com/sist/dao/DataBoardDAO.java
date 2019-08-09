package com.sist.dao;

import java.util.*;

import oracle.net.aso.d;

import java.sql.*;
public class DataBoardDAO {
	private Connection conn; // Socket 첨부 (연결기기) ==> TCP
	private PreparedStatement ps; // InputStream, OutputStream(SQL문장 전송)
	private final String URL="jdbc:oracle:thin:@localhost:1521:ORCL";
	
	public DataBoardDAO(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 리플렉션 : 클래스이름을 읽어서 클래스를 제어 => Spring
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getConnection(){
		try {
			conn=DriverManager.getConnection(URL,"scott","tiger");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disConnection(){
		try {
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<DataBoardVO> databoardListData(int page){
		List<DataBoardVO> list = new ArrayList<DataBoardVO>();
		try {
			getConnection();
			String sql="select no,subject,name,regdate,hit,num from (select no,subject,name,regdate,hit,rownum as num from (select no,subject,name,regdate,hit from databoard order by no desc)) where num between ? and ?";
			ps=conn.prepareStatement(sql);
			int rowsize=10;
			int start = (rowsize*page)-(rowsize-1);
			int end = rowsize*page;
			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				DataBoardVO vo=new DataBoardVO();
				vo.setNo(rs.getInt(1));
				vo.setSubject(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setHit(rs.getInt(5));
				
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			disConnection();
		}
		return list;
	}
	public void databoardInsert(DataBoardVO vo){
		try {
			getConnection();
			String sql="insert into databoard(no,name,subject,content,pwd,filename,filesize) values((select nvl(max(no)+1,1) from databoard),?,?,?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setString(2, vo.getSubject());
			ps.setString(3, vo.getContent());
			ps.setString(4, vo.getPwd());
			ps.setString(5, vo.getFilename());
			ps.setInt(6, vo.getFilesize());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			disConnection();
		}
	}
	public DataBoardVO databoardDetailData(int no){
		DataBoardVO vo = new DataBoardVO();
		try {
			getConnection();
			String sql="update databoard set hit=hit+1 where no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ps.executeUpdate();
			sql="select no,name,subject,content,regdate,hit,filename,filesize from databoard where no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs = ps.executeQuery();
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			vo.setRegdate(rs.getDate(5));
			vo.setHit(rs.getInt(6));
			vo.setFilename(rs.getString(7));
			vo.setFilesize(rs.getShort(8));
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			disConnection();
		}
		return vo;
	}
	public int databoardTotalPage(){
		int total=0;
		try {
			getConnection();
			String sql="select ceil(count(*)/10.0) from databoard";
			ps=conn.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			disConnection();
		}
		
		return total;
	}
	public DataBoardVO databoardupdateData(int no){
		DataBoardVO vo = new DataBoardVO();
		try {
			getConnection();
			String sql="select no,name,subject,content from databoard where no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs = ps.executeQuery();
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			disConnection();
		}
		return vo;
	}
	public boolean databoardUpdate(DataBoardVO vo){
		boolean bCheck=false;
		try {
			getConnection();
			String sql="select pwd from databoard where no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getNo());
			ResultSet rs = ps.executeQuery();
			rs.next();
			String db_pwd = rs.getString(1);
			rs.close();
			if(db_pwd.equals(vo.getPwd())){
				bCheck=true;
				sql="update databoard set name=?,subject=?,content=? where no=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, vo.getName());
				ps.setString(2, vo.getSubject());
				ps.setString(3, vo.getContent());
				ps.setInt(4, vo.getNo());
				ps.executeUpdate();
			}else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			disConnection();
		}
		
		return bCheck;
	}
	
	public String isLogin(String id,String pwd){
		String result="";
		
		try {
			getConnection();
			String sql="select count(*) from member where id=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			rs.close();
			if(count==0){
				result="noid";
			}else{
				sql="select pwd,name from member where id=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, id);
				rs = ps.executeQuery();
				rs.next();
				String db_pwd=rs.getString(1);
				String name = rs.getString(2);
				rs.close();
				if(db_pwd.equals(pwd)){
					result=name;
				}else{
					result="nopwd";
				}		
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			disConnection();
		}
		return result;
	}
	public List<DataBoardReplyVO> databoardReplyData(int bno){
		List<DataBoardReplyVO> list = new ArrayList<DataBoardReplyVO>();
		try {
			getConnection();
			String sql="select no,bno,id,name,msg,to_char(regdate,'YYYY-MM-dd HH24:MI:SS') from databoardreply where bno=? order by no desc";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, bno);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				DataBoardReplyVO vo = new DataBoardReplyVO();
				vo.setNo(rs.getInt(1));
				vo.setBno(rs.getInt(2));
				vo.setId(rs.getString(3));
				vo.setName(rs.getString(4));
				vo.setMsg(rs.getString(5));
				vo.setDbday(rs.getString(6));
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			disConnection();
		}
		return list;
	}
	public void replyInsert(DataBoardReplyVO vo){
		try {
			getConnection();
			String sql="insert into databoardreply values((select nvl(max(no)+1,1) from databoardreply), ?,?,?,?,sysdate)";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getBno());
			ps.setString(2, vo.getId());
			ps.setString(3, vo.getName());
			ps.setString(4, vo.getMsg());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			disConnection();
		}
	}
	public void replyDelete(int no){
		try {
			getConnection();
			String sql="delete from databoardreply where no=?";
			ps= conn.prepareStatement(sql);
			ps.setInt(1, no);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			disConnection();
		}
	}
	
	public void replyUpdate(int no,String msg){
		try {
			getConnection();
			String sql="update databoardreply set msg=? where no=?";
			ps= conn.prepareStatement(sql);
			ps.setString(1, msg);
			ps.setInt(2, no);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			disConnection();
		}
	}
}
