package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/** 体检检查项内容. */
@Entity
public class PhysicalInfo extends Model {
	// 所属检查项
	@Reference(ignoreMissing = false)
	public Physical physical;
	// 检查项内容
	public String content;
	// 创建时间
	public Date createAt;
	// 修改时间
	public Date modifyAt;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("physical", this.physical.getId());
		result.put("content", this.content);
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
