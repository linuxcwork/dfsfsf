package Function;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import base.PersonalInformation;

public class GetNearbyFriendPosition extends HttpServlet {

	private PersonalInformation personalInformation = null;
	protected Connection register_cnn = null;
	private String register_driverName = null;
	//private String register_url = null;
	private Connection connect = null;
	/**
	 * Constructor of the object.
	 */
	public GetNearbyFriendPosition() {
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.flush();
		out.close();
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
	@SuppressWarnings("static-access")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Statement statement = null;
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		System.out.println(request.getParameter("latitude")+":"+request.getParameter("longitude"));
		String latitude = request.getParameter("latitude");
		String longitude = request.getParameter("longitude");
		String requestcode = request.getParameter("requestcode"); 
		String userid = request.getParameter("userid"); 
		System.out.println(requestcode.length());
		if (requestcode == null && latitude == null && longitude == null && userid == null) {
			return;
		}
		
		try {
			statement = connect.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (requestcode.equals("getposition")){
			try {
				ResultSet resultSet = statement.executeQuery("SELECT * FROM userposition WHERE latitude<"+latitude+" && latitude>"+latitude+" && longitude<"+longitude+" && longitude>"+longitude+";");
				while (resultSet.next()){
					int i = 0;
					map.put(i,resultSet.getString(i));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//out.append(map);
			
		} else if (requestcode.equals("syncposition")) {
			
			try {
				statement.execute("CREATE TABLE IF NOT EXISTS userposition ("+
					personalInformation.iDString+" VARCHAR(24) NOT NULL,"+
					personalInformation.LATITUDE+" VARCHAR(34) NOT NULL,"+
					personalInformation.lONGITUDEString+" VARCHAR(34) NOT NULL"+
					");");
				ResultSet resultset = statement.executeQuery("SELECT * FROM userposition WHERE id='"+userid+"';");
				if(resultset.next()){
				Boolean retBoolean = statement.execute("UPDATE userposition SET latitude='"+latitude+"',longitude='"+
						longitude+"' WHERE id='"+userid+"';");
				} else {
					statement.execute("INSERT INTO userposition (id,latitude,longitude) VALUES ("+
							userid+","+
							latitude+","+
							longitude+");");
				}
				//System.out.println(retBoolean);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*// 1. 创建配置工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 2. 根据配置工厂创建解析请求中文件上传内容的解析器
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 3. 判断当前请求是不是多段提交
        if (!upload.isMultipartContent(request)) {
            throw new RuntimeException("不是多段提交！");
        }

        try {
            // 4. 解析request对象，将已经分割过的内容放进了List
            List<FileItem> list = upload.parseRequest(request);
            if (list != null) {
                for (FileItem fileItem : list) {
                    // 判断当前段是普通字段还是文件,这个方法是判断普通段
                    if (fileItem.isFormField()) {
                        // 获得name属性对应的值,这里是username
                        String fname = fileItem.getFieldName();
                        // 获得键对应的值
                        String value = fileItem.getString("utf-8");
                        System.out.println(fname +  "=>"+value );
                        // 否则就是文件了
                    } else {
                    	// 获得name属性对应的值,这里是username
                        String fname = fileItem.getFieldName();
                    	//获取路径名  
                        String value = fileItem.getName();  
                        //取到最后一个反斜杠。  
                        int start = value.lastIndexOf("\\");  
                        //截取上传文件的 字符串名字。+1是去掉反斜杠。  
                        String filename = value.substring(start+1);  
                        request.setAttribute(fname, filename); 
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("/yyyy/MM/dd/");
                        String datePath = simpleDateFormat.format(new Date()); // 解析成    /2017/04/15/  的样子, 注意这是三个文件夹
                        String wholePath = "D:/upload"+datePath;
                        第三方提供的方法直接写到文件中。 
                         * item.write(new File(path,filename));  
                        //收到写到接收的文件中。  
                        OutputStream out1 = new FileOutputStream(new File(wholePath,filename));  
                        InputStream in = fileItem.getInputStream();  
                          
                        int length = 0;  
                        byte[] buf = new byte[1024];  
                        System.out.println("获取文件总量的容量:"+ fileItem.getSize());  
                          
                        while((length = in.read(buf))!=-1){  
                            out1.write(buf,0,length);  
                        }  
                        in.close();  
                        out1.close();  
              
                        // 获得文件上传段中，文件的流
                        InputStream in = fileItem.getInputStream();

                        // 使用用户上传的文件名来保存文件的话，文件名可能重复。
                        // 所以保存文件之前，要保证文件名不会重复。使用UUID生成随机字符串
                        String fileName = UUID.randomUUID().toString();
                        System.out.println(fileName);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("/yyyy/MM/dd/");
                        String datePath = simpleDateFormat.format(new Date()); // 解析成    /2017/04/15/  的样子, 注意这是三个文件夹
                        String wholePath = "D:/upload"+datePath;
                        // 字节输出流，用以保存文件，也不需要后缀名，因为我们只是保存用户的数据，不需要查看他们的数据。待用户想下载的时候，再加上后缀名
                        File dir = new File(wholePath);
                        // mkdirs可以建立多级目录。即使所有层级的目录都不存在。这些文件夹都会创建,比如我们事先并没有创建在D盘创建upload和2017等这些文件夹
                        // mkdir只能用于父级目录已经存在的情况下使用，在已存在的父级目录下再新建一级。只能一级！比如File("D:\\upload\\2017\\04")。且D:\\upload\\2017是已经存在的。父级 目录存且只新建一级。故file.makedir()返回true成功创建。
                        // 但是File("D:\\upload\\2017\\04\\15")且D:\\upload\\2017存在，但不存在15文件夹。因为父级目录不存在所以创建失败返回false
                        if (!dir.exists()) {
                            dir.mkdirs(); 
                        }
                        FileOutputStream fos = new FileOutputStream(wholePath+fileName);
                        // 将输入流复制到输出流中
                        IOUtils.copy(in, fos);
                      fos.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
		
		out.append("dddddddddddddddddddddddd");
		out.flush();
		out.close();
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
