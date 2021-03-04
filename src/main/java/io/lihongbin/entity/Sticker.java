package io.lihongbin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Li Hong Bin
 * @since 2021-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sticker")
@NoArgsConstructor
public class Sticker implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sticker_id", type = IdType.AUTO)
    private Long stickerId;

    @TableField("set_name")
    private String setName;

    @TableField("emoji")
    private String emoji;

    @TableField("is_animated")
    private Boolean isAnimated;

    @TableField("file_unique_id")
    private String fileUniqueId;

    @TableField("height")
    private Integer height;

    @TableField("width")
    private Integer width;

    @TableField("file_size")
    private Integer fileSize;

    @TableField("localhost_path")
    private String localhostPath;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public Sticker(String setName, String emoji, Boolean isAnimated, String fileUniqueId, Integer height, Integer width, Integer fileSize, String localhostPath) {
        this.setName = setName;
        this.emoji = emoji;
        this.isAnimated = isAnimated;
        this.fileUniqueId = fileUniqueId;
        this.height = height;
        this.width = width;
        this.fileSize = fileSize;
        this.localhostPath = localhostPath;
    }
}
