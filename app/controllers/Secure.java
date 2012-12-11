package controllers;

import java.util.ArrayList;
import java.util.List;

import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Util;

import com.google.code.morphia.Key;

public class Secure extends Controller {
	
	/**
	 * 检查要调用的Action方法是否可访问.
	 */
	/*
	@Before(unless = { "login", "logout", "loginWithXHR", "authenticate" })
	public static void checkAccess() {
		String adminId = session.get("admin");
		if (adminId == null || "".equals(adminId)) {
			String error = "请登录后进行操作！";
			render("/Application/login.html", error);
		}
	}
	*/
	
	/**
	 * 如果已登陆则跳转回原先路径，如果未登陆，跳转到登陆页面.
	 */
	/*
	public static boolean authenticate() {
		try {
			// 验证成功，跳转回到请求页面
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	*/
	
	public static void login(final String name, final String password) {
		models.Administrator admin = getAdmin(name, password);
		if (admin != null) {
			session.put("admin", admin.getId());
			redirect("/");
		} else {
			String error = "管理员名称或者密码错误！";
			render("/Application/login.html", error);
		}
	}
	
	public static void logout() {
		session.clear();
		render("/Application/login.html");
	}
	
	@Util
	public static models.Administrator getAdmin(String name, String pwd) {
		models.Administrator admin = null;
		if (name != null && !"".equals(name) && pwd != null && !"".equals(pwd)) {
			admin = models.Administrator.find("admin", name).filter("password", pwd).get();
		} else {
			String adminId = session.get("admin");
			admin = models.Administrator.findById(adminId);
		}
		
		return admin;
	}
	
	@Util
	public static models.Administrator getAdmin() {
		String adminId = session.get("admin");
		if (adminId != null && !"".equals(adminId))
			return models.Administrator.findById(adminId);
		return null;
	}
	
	/** 获取当前操作者的部门以及子部门. */
	@Util
	public static List<models.Department> getDepartments() {
		String adminId = session.get("admin");
		if (adminId == null || "".equals(adminId)) {
			String error = "请先进行登录！";
			render("/Application/login.html", error);
		}
		List<models.Department> result = new ArrayList<models.Department>();
		models.Administrator admin = models.Administrator.findById(adminId);
		result.add(admin.department);
		result.addAll(eachDepartments(admin.department));
		return result;
	}
	
	/** 迭代获取全部子部门. */
	@Util
	public static List<models.Department> eachDepartments(models.Department root) {
		List<models.Department> result = new ArrayList<models.Department>();
		List<models.Department> childs = models.Department.find("byParent", root).asList();
		for (models.Department child : childs) {
			result.add(child);
			result.addAll(eachDepartments(child));
		}
		return result;
	}
	
	/** 根据key获取cookie的内容. */
	@Util
	public static String getCookie(String key) {
		Http.Cookie cookie = request.cookies.get(key);
		return cookie != null ? cookie.value : null;
	}
}
