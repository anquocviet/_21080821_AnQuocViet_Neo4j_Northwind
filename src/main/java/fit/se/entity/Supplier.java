package fit.se.entity;

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
public class Supplier {
   @JsonProperty("supplierID")
   private String id;
   private String phone;
   private String country;
   private String contactTitle;
   private String address;
   private String city;
   private String contactName;
   private String companyName;
   private String postCode;
   private String region;
   private String fax;
   private String homePage;

}
