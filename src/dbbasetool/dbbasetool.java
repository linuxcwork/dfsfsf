package dbbasetool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sun.swing.internal.plaf.basic.resources.basic;

import base.PersonalInformation;

public class dbbasetool {
	private Connection connect;
	private Statement statement;
	public dbbasetool(Connection connection){
		this.connect = connection;
	}
	
	/*
	 * 函数名 ：adduser
	 * 功能：    注册时，添加用户
	 * 调用：    RegisterActionsql类：doPost */
	public Boolean addUser(String table,PersonalInformation personalInformation) throws SQLException{
		Boolean retBoolean;
		statement = connect.createStatement();
		retBoolean = statement.execute("INSERT INTO "+
				table+
				" (id,name,passwd,telphone) VALUES ('"+
				personalInformation.id+"','"+
				personalInformation.name+"','"+
				personalInformation.passwd+"','" +
				personalInformation.telphone+"');");
		return retBoolean;
	}
	
	/*
	 * 函数名：createdbtable
	 * 功能：创建表
	 * 调用：仅在注册过程调用
	 * */
	public Boolean createdbtable(String table) throws SQLException {
		Boolean retBoolean;
		PersonalInformation personalInformation = new PersonalInformation();
		statement = connect.createStatement();
		retBoolean = statement.execute("CREATE TABLE "+table+" ("+
			personalInformation.iDString+" VARCHAR(18) NOT NULL,"+
			personalInformation.nAMEString+" VARCHAR(32)"+","+
			personalInformation.iMAString+" TEXT,"+
			personalInformation.pASSWDString+" VARCHAR(80),"+
			personalInformation.aGEString+" TINYINT,"+
			personalInformation.iSMARRYString+" TINYINT,"+
			personalInformation.hOMEADDRString+" CHAR,"+
			personalInformation.tELPHONEString+" VARCHAR(60) NOT NULL,"+
			personalInformation.eMAILString+" VARCHAR(80),"+
			personalInformation.mOTTOString+" VARCHAR(40),"+
			personalInformation.iDCARDString+" VARCHAR(24),"+
			personalInformation.iDCRDVALIDString+" VARCHAR(12),"+
			personalInformation.iDCRDINVALIDString+" VARCHAR(12),"+
			personalInformation.wORKString+" VARCHAR(32),"+
			personalInformation.iNTEREString+" VARCHAR(100),"+
			personalInformation.IFEVALUString+" TINYINT"+
			");");
		return retBoolean;
	}
	
	/*
	 * 函数名:queryData
	 * 功能：  查询数据，根据键值查找对应数据
	 * 参数：table  表名
	 *       key    查询键值
	 *       data   该键值数据
	 * 返回：查询结果，如果无数据返回空
	 * */
	public ResultSet queryData(String table,String key,String data) throws SQLException {
		ResultSet ret = null;
		statement = connect.createStatement();
		ret = statement.executeQuery("SELECT "+key+" FROM "+table+" WHERE "+key+"="+data+";");
		return ret;
	}
	
	/*
	 * 函数: updateAccordKey
	 * 功能: 修改数据
	 * 参数: table 表名
	 *       id    用户id
	 *       key   所需修改的键值
	 *       data  改变的数据
	 * 返回: void
	 * */
	public void updateAccordKey(String table,String id,String key,String data) throws SQLException{
		statement = connect.createStatement();
	    statement.executeQuery("UPDATE "+table+" SET "+key+"='"+data+"' WHERE id='"+id+"';");
	}
	/*
	 * 功能：删除指定用户，仅对于 id 键值
	 * 参数: table  表名
	 *       key    根据的键值，仅为id，其余数据可能存在重复
	 *       data   id号
	 * 返回: void
	 * */
	public void deleteUser(String table,String key,String data) throws SQLException{
		statement = connect.createStatement();
		statement.execute("DELETE FROM "+table+" WHERE "+key+"="+data+";");
	}
	
	public void close() throws SQLException{
		statement.close();
	}

}
