package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.modules.morphia.Model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

/** 体检检查项. */
@Entity
public class Physical extends Model {
	// 体检所属档案
	@Reference(ignoreMissing = false)
	public HealthArchives health;
	// 检查项详情
	@Reference(ignoreMissing = true)
	public List<PhysicalInfo> physicalInfo;
	// 检查项名称
	public String name;
	// 创建时间
	public Date createAt;
	// 修改时间
	public Date modifyAt;
	
	/** json序列化. */
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.getId().toString());
		result.put("name", this.name);
		result.put("health", this.health.getId());
		List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
		if (physicalInfo != null && physicalInfo.size() > 0) {
			for (PhysicalInfo children : physicalInfo)
				infoList.add(children.serialize());
			result.put("physicalInfo", infoList);
		}
		result.put("createAt", utils.Utils.formatDate(this.createAt));
		result.put("modifyAt", utils.Utils.formatDate(this.modifyAt));
		return result;
	}
}
