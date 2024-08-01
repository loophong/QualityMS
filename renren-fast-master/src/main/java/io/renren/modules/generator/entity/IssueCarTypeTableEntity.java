package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 车型字典
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 18:55:06
 */
@Data
@TableName("issue_car_type_table")
public class IssueCarTypeTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 车型ID
	 */
	@TableId
	private Integer vehicleTypeId;
	/**
	 * 具体车型
	 */
	private String concreteVehicleType;

}
