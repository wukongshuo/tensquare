package entity;

import java.util.List;

public class PageResult<T> {

   private Long total;
   private List<T> rows;

   public PageResult(Long total, List<T> rows) {
      super();
      this.total = total;
      this.rows = rows;
   }
   //getter and setter ....
}