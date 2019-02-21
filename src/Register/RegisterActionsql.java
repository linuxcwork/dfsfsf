package Register;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.DateFormatter;
import javax.xml.crypto.Data;

import com.sun.org.apache.bcel.internal.generic.NEW;

import dbbasetool.dbbasetool;

import base.PersonalInformation;

public class RegisterActionsql extends HttpServlet {

	private static final long serialVersionUID = 1L;
	protected Connection register_cnn = null;
	private String register_driverName = null;
	private String register_url = null;
	private Connection connect = null;
	private PersonalInformation personalInformation = null;
	/**
	 * Constructor of the object.
	 */
	public RegisterActionsql() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ResultSet rs = null;
		Statement statement = null;
		long count_full = 0;
		String table = null;
		String sort = null;
		dbbasetool db = new dbbasetool(connect);
		PrintWriter out = response.getWriter();
		personalInformation.name = request.getParameter("username");
		personalInformation.passwd = request.getParameter("passwd");
		personalInformation.telphone = request.getParameter("phone");
		try {
			statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = statement.executeQuery("SELECT * FROM sqlinfo;");	
			if(rs == null){
				//statement.execute("UPDATE sqlinfo SET isfull=1 WHERE tablename="+rs.getString("tablename")+";");
				Date date = new Date();
				table = new SimpleDateFormat("yyyyMMddhhmm_ss").format(date);
				db.createdbtable(table);
				statement.execute("INSERT INTO sqlinfo (count,tablename,isfull) VALUES (0,'"+table+"',0);");
			}else {
				while(rs.next()){
					table = rs.getString("tablename");
					if(queryuseraccordtelphone(table, personalInformation.telphone))
					{
						out.println("fail");
						return;
					}
				
					if(rs.getBoolean("isfull") == true){
						System.out.print(count_full);
						count_full++;
					} else if (rs.getLong("count") >= 10000){
						statement.execute("UPDATE sqlinfo SET isfull=1 WHERE tablename="+rs.getString("tablename")+";");
						Date date = new Date();
						table = new SimpleDateFormat("yyyyMMddhhmm_ss").format(date);
						db.createdbtable(table);
						statement.execute("INSERT INTO sqlinfo (count,tablename,isfull) VALUES (0,'"+table+"',0);");
						personalInformation.id = table+"0";
					} else {
						table = rs.getString("tablename");
						sort = String.valueOf(rs.getInt("count")+1);
						personalInformation.id = table+sort;
						statement.execute("UPDATE SQLINFO SET count='"+sort+"' WHERE tablename='"+table+"';");
						System.out.println("while"+sort+table);
						break;
					}			
					System.out.println("while"+rs.getString("count").toString()+rs.getObject("tablename"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(personalInformation.id+":"+personalInformation.name+":"+personalInformation.passwd+":"+personalInformation.telphone);
		//personalInformation. = request.getParameter("verification");
		try {
				db.addUser(table, personalInformation);
				out.println("ok");
				db.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("doget");
		out.flush();
		out.close();

		try {
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ResultSet rs = null;
		Statement statement = null;
		long count_full = 0;
		String table = null;
		String sort = null;
		
		String verString = request.getParameter("verifycode");
		if(verString != null){
			System.out.println("ver");
			return;
		}
		dbbasetool db = new dbbasetool(connect);
		PrintWriter out = response.getWriter();
		personalInformation.name = request.getParameter("username");
		personalInformation.passwd = request.getParameter("passwd");
		personalInformation.telphone = request.getParameter("phone");
		try {
			statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = statement.executeQuery("SELECT * FROM sqlinfo;");	
			if(rs == null){
				//statement.execute("UPDATE sqlinfo SET isfull=1 WHERE tablename="+rs.getString("tablename")+";");
				Date date = new Date();
				table = new SimpleDateFormat("yyyyMMddhhmm_ss").format(date);
				db.createdbtable(table);
				statement.execute("INSERT INTO sqlinfo (count,tablename,isfull) VALUES (0,'"+table+"',0);");
			}else {
				while(rs.next()){
					table = rs.getString("tablename");
					if(queryuseraccordtelphone(table, personalInformation.telphone))
					{
						out.println("registered");
						return;
					}
				
					if(rs.getBoolean("isfull") == true){
						System.out.print(count_full);
						count_full++;
					} else if (rs.getLong("count") >= 10000){
						statement.execute("UPDATE sqlinfo SET isfull=1 WHERE tablename="+rs.getString("tablename")+";");
						Date date = new Date();
						table = new SimpleDateFormat("yyyyMMddhhmm_ss").format(date);
						db.createdbtable(table);
						statement.execute("INSERT INTO sqlinfo (count,tablename,isfull) VALUES (0,'"+table+"',0);");
						personalInformation.id = table+"0";
					} else {
						table = rs.getString("tablename");
						sort = String.valueOf(rs.getInt("count")+1);
						personalInformation.id = table+sort;
						statement.execute("UPDATE SQLINFO SET count='"+sort+"' WHERE tablename='"+table+"';");
						System.out.println("while"+sort+table);
						break;
					}			
					System.out.println("while"+rs.getString("count").toString()+rs.getObject("tablename"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(personalInformation.id+":"+personalInformation.name+":"+personalInformation.passwd+":"+personalInformation.telphone);
		//personalInformation. = request.getParameter("verification");
		try {
				db.addUser(table, personalInformation);
				out.println("register");
				db.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("DOPOST");
		out.flush();
		out.close();

		try {
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * 函数名: queryuseraccordtelphone
	 * 功能:   查询数据库中是否存在当前手机号已注册用户
	 * 返回:   存在返回 true，else false*/
	protected boolean queryuseraccordtelphone(String table,String tel) throws SQLException{
		boolean exist;
		dbbasetool db = new dbbasetool(connect);
		ResultSet retResultSet= db.queryData(table,"telphone",tel);
		if (retResultSet.next()) {
			exist = true;
		} else {
			exist = false;
		}
		db.close();
		return exist;
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init(ServletConfig config) throws ServletException {
		//创建sqlinfo 表，用来存储当前用户最大编号，
		personalInformation = new PersonalInformation();
		// Put your code here
		register_driverName=config.getInitParameter("driverName");
		register_url=config.getInitParameter("url");
		
		try {
			Class.forName(register_driverName);
			//register_cnn = DriverManager.getConnection(register_url);
		} catch(Exception e) {
			System.out.println("取得数据库连接错误："+e.getMessage());
		}
		try {
			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/user","root","123456");
			//连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码

			System.out.println("Success connect Mysql server!");
		}
		catch (Exception e) {
			System.out.print("get data error!");
			e.printStackTrace();
		}
	}

}
