package com.yupaits.auth.dto;

import java.time.LocalDate;

import com.yupaits.commons.consts.enums.Gender;
import com.yupaits.commons.core.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * UserCreate
 * </p>
 *
 * @author yupaits
 * @date 2018-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UserCreate", description = "UserCreate对象")
public class UserCreate extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "性别")
    private Gender gender;

    @ApiModelProperty(value = "生日")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "账户可用")
    private boolean enabled = true;

    @Override
    @ApiModelProperty(hidden = true)
    public boolean isValid() {
        return StringUtils.isNotBlank(username);
    }

}
