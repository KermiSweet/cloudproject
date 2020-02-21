package entity;

import lombok.Data;

import java.util.List;

/**
 * @Author : Kermi
 * @Date : 2020/2/12 13:11
 * @Version : 1.0
 */
@Data
public class PageResult<T> {
    private long total;
    private List<T> rows;

    public PageResult(long total, List<T> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }
}
