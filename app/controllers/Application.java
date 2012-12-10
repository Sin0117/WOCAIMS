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
    		root.name = "公司工会";
    		root.code = "GSGH";
    		root.type = DepartmentLevel.UNION;
    		root.createAt = new Date();
    		root = root.save();
    	}
    	models.Administrator admin = models.Administrator.find("byDepartment", root).get();
    	if (admin == null) {
    		admin = new models.Administrator();
    		admin.admin = "Administrator";
    		admin.name = "总工会管理员";
    		admin.password = "!2#4QwEr";
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

