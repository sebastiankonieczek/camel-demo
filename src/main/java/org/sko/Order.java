package org.sko;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity( name = "SHOP_ORDER" )
@Table( name = "SHOP_ORDER")
public class Order
{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column( name = "item_id",nullable = false )
   private String itemId;
   @Column( name="quantity", nullable = false )
   private int quantity;

   @JsonCreator
   public Order(
      final @JsonProperty( "itemId" ) String itemId,
      final @JsonProperty( "quantity" ) Integer quantity )
   {
      this.itemId = itemId;
      this.quantity = quantity;
   }

   public Order()
   {
   }

   public Long getId()
   {
      return id;
   }

   public void setId( Long id )
   {
      this.id = id;
   }


   public String getItemId()
   {
      return itemId;
   }

   private void setItemId( final String itemId )
   {
      this.itemId = itemId;
   }

   public Integer getQuantity()
   {
      return quantity;
   }

   private void setQuantity( final Integer quantity )
   {
      this.quantity = quantity;
   }

   @Override
   public String toString()
   {
      return ReflectionToStringBuilder.toString( this, ToStringStyle.SHORT_PREFIX_STYLE );
   }

   @Override
   public boolean equals( final Object o )
   {
      if( this == o ) {
         return true;
      }
      if( !( o instanceof Order ) ) {
         return false;
      }
      final Order order = (Order)o;
      return Objects.equals( id, order.id )
             && Objects.equals( itemId, order.itemId )
             && Objects.equals( quantity, order.quantity );
   }

   @Override
   public int hashCode()
   {
      return Objects.hash( id, itemId, quantity );
   }
}
