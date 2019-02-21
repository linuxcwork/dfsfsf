package Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbbasetool.dbbasetool;

import base.PersonalInformation;

public class LoginAction extends HttpServlet {

	private PersonalInformation personalInformation = null;
	protected Connection register_cnn = null;
	private String register_driverName = null;
	//private String register_url = null;
	private Connection connect = null;
	/**
	 * Constructor of the object.
	 */
	public LoginAction() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
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

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml;UTF-8");
		personalInformation.name = request.getParameter("username");
		personalInformation.passwd = request.getParameter("passwd");
		PrintWriter out = response.getWriter();
		out.flush();
		out.close();
		System.out.println("login doget");
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
		Statement statement = null;
		ResultSet rs = null;
		PrintWriter out = response.getWriter();
		String table = null;
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml;UTF-8");
		String address = request.getRemoteAddr();
		System.out.println("长度"+request.getContentLength()+":"+address);
		personalInformation.name = request.getParameter("username");
		personalInformation.passwd = request.getParameter("passwd");
		try {
			statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = statement.executeQuery("SELECT * FROM sqlinfo;");
			while(rs.next()){
				table = rs.getString("tablename");
				if(queryuseraccordtelphone(table, personalInformation.name,personalInformation.passwd))
				{
					out.println("login");
					return;
				}
					
				System.out.println("while"+rs.getString("count").toString()+rs.getObject("tablename"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("fail");
		out.println("fail");
		out.flush();
		out.close();
		try {
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("login doPost");
	}
	
	/*
	 * 函数名: queryuseraccordtelphone
	 * 功能:   查询数据库中是否存在当前手机号已注册用户
	 * 返回:   存在返回 true，else false*/
	protected boolean queryuseraccordtelphone(String table,String tel,String passwd) throws SQLException{
		boolean exist;
		ResultSet retResultSet= null;
		Statement statement = connect.createStatement();
		retResultSet = statement.executeQuery("SELECT * FROM "+table+" WHERE telphone='"+tel+"' AND passwd='"+passwd+"';");
		if (retResultSet.next()) {
			exist = true;
		} else {
			exist = false;
		}
		retResultSet.close();
		statement.close();
		return exist;
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init(ServletConfig config) throws ServletException {
		// Put your code here
		personalInformation = new PersonalInformation();
		// Put your code here
		register_driverName=config.getInitParameter("driverName");
		//register_url=config.getInitParameter("url");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//register_cnn = DriverManager.getConnection(register_url);
		} catch(Exception e) {
			System.out.println("取得数据库连接错误："+e.getMessage());
		}
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/user","root","123456");
			//连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码

			System.out.println("Success connect Mysql server!");
		}
		catch (Exception e) {
			System.out.print("get data error!");
			e.printStackTrace();
		}
	}

}
