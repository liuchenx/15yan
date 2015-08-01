package org.liuyichen.fifteenyan.model;



import ollie.Model;
import ollie.annotation.Column;
import ollie.annotation.Table;

/**
 * Created by root on 15-3-12.
 */
@SuppressWarnings({"DefaultFileTemplate", "unused"})
@Table("detail_cache")
public class DetailCache extends Model {

    @Column("storyId")
    public String storyId;

    @Column("detail")
    public String detail;


}
