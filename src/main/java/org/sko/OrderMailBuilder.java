package org.sko;

import org.apache.camel.Exchange;

import java.util.List;

public class OrderMailBuilder
{
   public String createOrderPlacementMailText( Exchange exchange )
   {
      return "Orders have been placed: " + exchange.getIn().getBody( List.class );
   }
}
