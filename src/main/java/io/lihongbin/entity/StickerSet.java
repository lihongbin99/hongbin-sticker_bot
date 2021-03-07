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
@TableName("sticker_set")
@NoArgsConstructor
public class StickerSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sticker_set_id", type = IdType.AUTO)
    private Long stickerSetId;

    @TableField("`name`")
    private String name;

    @TableField("title")
    private String title;

    @TableField("is_animated")
    private Boolean isAnimated;

    @TableField("contains_masks")
    private Boolean containsMasks;

    @TableField("zip_path")
    private String zipPath;

    @TableField("directory_name")
    private String directoryName;

    @TableField("is_new")
    private Boolean isNew;

    @TableField("is_refresh")
    private Boolean isRefresh;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public StickerSet(String name, String title, Boolean isAnimated, Boolean containsMasks, String zipPath, String directoryName) {
        this.name = name;
        this.title = title;
        this.isAnimated = isAnimated;
        this.containsMasks = containsMasks;
        this.zipPath = zipPath;
        this.directoryName = directoryName;
    }
}
