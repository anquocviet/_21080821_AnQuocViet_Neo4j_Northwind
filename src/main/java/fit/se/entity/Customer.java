package fit.se.entity;

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
public class Customer {
   private String id;
   private String contactName;
   private String companyName;
   private String country;
   private String contactTitle;
   private String address;
   private String phone;
   private String city;
   private String postCode;
   private String fax;
   private String region;

}
