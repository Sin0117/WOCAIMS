package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import models.enums.DepartmentLevel;

/** 整体界面的总入口. */
public class Application extends Controller {
	
    public static void index() {
    	models.Administrator admin = Secure.getAdmin();
    	if (admin != null) {
    		render(admin);
    	} else {
    		redirect("/login");
    	}
    }
    
    public static void login() {
    	render();
    }
    
    /** 数据初始化. */
    public static void init() {
    	models.Department root = models.Department.find("byType", DepartmentLevel.UNION).get();
    	if (root == null) {
    		root = new models.Department();
    		root.name = "准能公司";
    		root.code = "001";
    		root.codeLength = root.code.length();
    		root.type = DepartmentLevel.UNION;
    		root.createAt = new Date();
    		root = root.save();
    	}
    	models.Administrator admin = models.Administrator.find("byDepartment", root).get();
    	if (admin == null) {
    		admin = new models.Administrator();
    		admin.admin = "admin";
    		admin.name = "管理员";
    		admin.password = "123456";
    		admin.department = root;
    		admin.isAdmin = true;
    		admin.createAt = new Date();
    		admin.lastLoginAt = new Date();
    		admin = admin.save();
    	}
    	session.put("admin", admin.getId());
    	redirect("/");
    }
}

