package server.fix.hnx;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class test {
    public static void main(String[] args) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();

        camelContext.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                // TODO Auto-generated method stub
                System.out.println(123123);
                from("file://src/main/resources/?fileName=test.dat&noop=true")
                .to("file://src/main/resources/data");

            }

        });
    }
    
}
