package base;

import java.awt.Image;

public class PersonalInformation {

	public PersonalInformation(){
	
	}
	
	//系统号+表名
	public String iDString = "id";
	public String id;
	//个人情况
	public String iMAString = "image";
	public Image image;
	public String nAMEString = "name";
	public String name;
	public String pASSWDString = "passwd";
	public String passwd;
	//状态
	public Boolean stateBoolean;
	public String sTATEsString = "state";
	//登陆设备名
	public String devicename;
	public String devicenString = "devicename";
	//登录时间
	public String logintimeString;
	public String lOGINTIMEString ="logintime";
	//是否已婚
	public String iSMARRYString = "ismarry";
	public Boolean ismarry;
	public String aGEString = "age";
	public String age;
	//家庭住址
	public String hOMEADDRString = "homeaddr";
	public String home_addr;
	//经纬度
	public String LATITUDE = "latitude";
	public String latitudeString; 
	public String lONGITUDEString = "longitude";
	public String longitude;
	//现居地
	public String pRESENTADDRString = "presentaddr";
	public String present_addr;
	//手机
	public String tELPHONEString = "telphone";
	public String telphone;
	//邮箱
	public String eMAILString = "email";
	public String email;
	//座右铭
	public String mOTTOString = "motto";
	public String motto;
	//身份证号
	public String iDCARDString = "idcard";
	public int id_crd;
	public String iDCRDVALIDString ="idcrdvalid";
	public int id_crd_valid_time;
	public String  iDCRDINVALIDString = "idcrdinvalid";
	public int id_crd_invalid_time;
	public String wORKString = "work";
	public String work;
	//兴趣爱好
	public String iNTEREString = "interest";
	public String []interest;
	//人生值
	public String IFEVALUString = "lifevalus";
	public int lifevalue;
	
/*	//以下为选填
	//家庭情况
	public String fATJERNAMEString = "fathername";
	public String father_name;
	public String fATHERWORKString = "fatherwork";
	public String father_work;
	public String fATHERHEALTHString = "fatherhealth";
	public String father_health;
	public String FATHERAGE = "fatherage";
	public Integer father_age;
	public String mOTHERNAMEString = "mothername";
	public String mother_name;
	public String mOTHERWORKString = "motherwork";
	public String mother_work;
	public String mOTHERHEALTHString = "motherhealth";
	public String mother_health;
	public String mOTHERAGEString = "motherage";
	public Integer mother_age;*/
}
