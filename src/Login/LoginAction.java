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
	public Connection connect = null;
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
		System.out.println("长度"+request.getContentLength()+":"+address+request.getParameter("id"));
		personalInformation.telphone = request.getParameter("id");
		personalInformation.passwd = request.getParameter("passwd");
		personalInformation.devicename = request.getParameter(personalInformation.devicenString);
		try {
			if(connect == null){
				System.out.print("LoginAction: connect is fail;");
			}
			
			statement = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = statement.executeQuery("SELECT * FROM sqlinfo;");
			while(rs.next()){
				table = rs.getString("tablename");
				System.out.println(table);
				if(queryuseraccordtelphone(table, personalInformation))
				{
					dbbasetool db = new dbbasetool(connect);
					db.updateAccordKey(table, personalInformation.id, personalInformation.sTATEsString, "1");
					db.updateAccordKey(table, personalInformation.id, personalInformation.devicenString, personalInformation.devicename);
					SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");     
					String date = sDateFormat.format(new java.util.Date()); 
					System.out.print(date);
					db.updateAccordKey(table, personalInformation.id, personalInformation.lOGINTIMEString, date);
					out.print(personalInformation);
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
	public boolean queryuseraccordtelphone(String table,PersonalInformation personalInformation) throws SQLException{
		boolean exist;
		ResultSet retResultSet= null;
		Statement statement = connect.createStatement();
		retResultSet = statement.executeQuery("SELECT * FROM `"+table+"` WHERE telphone='"+personalInformation.telphone+"' AND passwd='"+personalInformation.passwd+"';");
		if (retResultSet.next()) {
			assignment(personalInformation, retResultSet);
			exist = true;
		} else {
			exist = false;
		}
		retResultSet.close();
		statement.close();
		return exist;
	}
	
	
	/*
	 * 函数名: assignment
	 * 功能:   将查询到的数据填充到用户体中
	 * 返回:   无返回值*/
    public void assignment(PersonalInformation personalInformation,ResultSet retResultSet) throws SQLException{
    	personalInformation.id = retResultSet.getString(personalInformation.iDString);
    	personalInformation.name = retResultSet.getString("name");
    	personalInformation.ismarry = retResultSet.getBoolean("ismarry");
    	personalInformation.age = retResultSet.getString("age");
    	
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
