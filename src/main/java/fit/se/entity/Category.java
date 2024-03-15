package fit.se.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @description
 * @author: vie
 * @date: 11/03/2024
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {
   @JsonProperty("categoryID")
   private String id;
   @JsonProperty("categoryName")
   private String name;
   private String description;
   private String picture;

}
