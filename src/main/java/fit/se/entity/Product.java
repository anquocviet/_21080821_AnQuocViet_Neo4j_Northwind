package fit.se.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@ToString
@JsonIgnoreProperties(value = {"supplierID", "categoryID"})
public class Product {
   @NonNull
   @JsonProperty("productID")
   private String id;

   @NonNull
   @JsonProperty("productName")
   private String name;

   @NonNull
   private double unitPrice;

   @NonNull
   private int unitsInStock;

   @NonNull
   private int reorderLevel;

   @NonNull
   private String quantityPerUnit;

   @NonNull
   private boolean discontinued;

   @NonNull
   private int unitsOnOrder;

   @ToString.Exclude
   private Category category;
   @ToString.Exclude
   private Supplier supplier;
}
