package org.sko;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.jetty.http.HttpStatus;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder
   extends RouteBuilder
{

   /**
    * Let's configure the Camel routing rules using Java code...
    */
   public void configure()
   {
      restConfiguration().component( "jetty" )
                         .host( "localhost" )
                         .port( 8090 )
                         .bindingMode( RestBindingMode.json )
                         .dataFormatProperty( "json.in.enableFeatures", "ACCEPT_SINGLE_VALUE_AS_ARRAY" )
                         .dataFormatProperty( "json.in.disableFeatures",
                                              "FAIL_ON_EMPTY_BEANS,FAIL_ON_UNKNOWN_PROPERTIES" )
                         .dataFormatProperty( "json.out.disableFeatures", "FAIL_ON_EMPTY_BEANS,FAIL_ON_UNKNOWN_PROPERTIES" );

      rest( "shop" )
         .get( "orders" )
            .produces( "application/json" )
            .outType( Order.class )
            .to( "direct:getOrders" )
         .post( "orders" )
            .consumes( "application/json" )
            .type( Order[].class )
            .to( "direct:orderReceived" );

      from( "direct:orderReceived" )
         .log( "Incoming order ${body}" )
         .to( ExchangePattern.InOnly, "direct:processOrders" )
         .transform( constant( null ) )
         .setHeader( Exchange.HTTP_RESPONSE_CODE, constant( HttpStatus.ACCEPTED_202 ) );

      from( "direct:processOrders" )
         .transacted()
         .to( "jpa:org.sko.Order[]" )
         .transform( method( OrderMailBuilder.class, "createOrderPlacementMailText" ) )
         .setHeader( Exchange.CONTENT_TYPE, constant( "text/plain" ) )
         .to( "smtp:localhost:3025?to=exampleCustomer@someMail.com&from=shop@camel.com" );

      from( "direct:getOrders" )
         .pollEnrich( "jpa:org.sko.Order" );
   }
}
