package server.fix.hnx;



import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import edu.emory.mathcs.backport.java.util.concurrent.CountDownLatch;
import hnx.quickfix.fields.BestBidPrice;
import hnx.quickfix.fields.BestBidQtty;
import hnx.quickfix.fields.BestOfferPrice;
import hnx.quickfix.fields.BestOfferQtty;
import hnx.quickfix.fields.BoardCode;
import hnx.quickfix.fields.CheckSum;
import hnx.quickfix.fields.NoTopPrice;
import hnx.quickfix.fields.NumTopPrice;
import hnx.quickfix.fields.Password;
import hnx.quickfix.fields.Signature;
import hnx.quickfix.fields.Status;
import hnx.quickfix.fields.Symbol;
import hnx.quickfix.fields.Username;
import hnx.quickfix.messages.Heartbeat;
import hnx.quickfix.messages.Logon;
import hnx.quickfix.messages.MessageFactory;
import hnx.quickfix.messages.TopNPrice;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.UnsupportedMessageType;

public class AppServerHNX implements Application {
    private static volatile SessionID sessionID;

    @Override
    public void fromAdmin(Message arg0, SessionID arg1)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        // TODO Auto-generated method stub

    }

    @Override
    public void fromApp(Message arg0, SessionID arg1)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreate(SessionID arg0) {
        // TODO Auto-generated method stub
        System.out.println("Create session:" + arg0);

    }

    @Override
    public void onLogon(SessionID arg0) {
        // TODO Auto-generated method stub
        System.out.println("Logon");
        AppServerHNX.sessionID = arg0;

        CamelContext camelContext = new DefaultCamelContext();

        try {
            camelContext.addRoutes(new RouteBuilder() {

                @Override
                public void configure() throws Exception {
                    // TODO Auto-generated method stub
                    System.out.println(123123);
                    from("file:src/main/resources/?fileName=hnx.dat&noop=true")
                            .split(body().tokenize("\n")).streaming().filter(body().contains("35=TP"))
                            .process(new Processor() {

                                @Override
                                public void process(Exchange exchange) throws Exception {
                                    // TODO Auto-generated method stub
                                    // String[] token = exchange.getIn().getBody(String.class).split("╔");
                                    String line = exchange.getIn().getBody(String.class);
                                    System.out.println(line);
                                    //String[] tokens = line.split("");
                                    String[] tokenPrices = line.split("556=");
                                    String[] tokens = tokenPrices[0].split("");
                                   
                                    TopNPrice mess = new TopNPrice();   
                                    mess.set(new Symbol(tokens[5].split("=")[1]));
                                    mess.set(new BoardCode(tokens[6].split("=")[1]));
                                    int noTopPrice = Integer.parseInt(tokens[7].split("=")[1]);
                                    mess.set(new NoTopPrice(noTopPrice));
                                    
                                    // for(int i = 0; i<noTopPrice; i++) {
                                    //     TopNPrice.NoTopPrice price = new TopNPrice.NoTopPrice();
                                    //     price.set(new NumTopPrice(i+1));
                                    //     if(tokens[9].split("=")[0].equals("133")) {
                                    //         price.set(new BestOfferPrice(Double.parseDouble(tokens[9].split("=")[1])));
                                    //         price.set(new BestOfferQtty(Double.parseDouble(tokens[10].split("=")[1])));
                                    //     } else {
                                    //         price.set(new BestBidPrice());
                                    //         price.set(new BestBidQtty());
                                    //         price.set(new BestOfferPrice(Double.parseDouble(tokens[9].split("=")[1])));
                                    //         price.set(new BestOfferQtty(Double.parseDouble(tokens[10].split("=")[1])));
                                            
                                    //     }
                                        
                                    //     mess.addGroup(price);
                                    // }

                                    if (noTopPrice > 0) {
                                        
                                        //System.out.println("leng:" + tokenPrices.length);
                                        for (int i = 1 ; i<tokenPrices.length ; i++) {
                                            TopNPrice.NoTopPrice price = new TopNPrice.NoTopPrice();
                                            String[] listSplitS = tokenPrices[i].split("");
                                            //System.out.println("sodongtachduoc:"+listSplitS.length + "--phan tu cuoi:" +listSplitS[listSplitS.length-1]);
                                            price.set(new NumTopPrice(Integer.parseInt(listSplitS[0])));
                                            // if(listSplitS.length > 4) {
                                            //     System.out.println(listSplitS[1] + "--" + listSplitS[2] +"-"+ listSplitS[3]+"-" + listSplitS[4]);
                                            // } else {
                                            //     System.out.println(listSplitS[1] + "--" + listSplitS[2]);
                                            // }
                                            if(listSplitS.length <5) {
                                                if(listSplitS[1].contains("132=")) {
                                                    price.set(new BestBidPrice(Double.parseDouble(listSplitS[1].split("=")[1])));
                                                    price.set(new BestBidQtty(Double.parseDouble(listSplitS[2].split("=")[1])));
                                                } else {
                                                    price.set(new BestOfferPrice(Double.parseDouble(listSplitS[1].split("=")[1])));
                                                    price.set(new BestOfferQtty(Double.parseDouble(listSplitS[2].split("=")[1])));
                                                }
                                            } 
                                            else {
                                                price.set(new BestBidPrice(Double.parseDouble(listSplitS[1].split("=")[1])));
                                                price.set(new BestBidQtty(Double.parseDouble(listSplitS[2].split("=")[1])));
                                                price.set(new BestOfferPrice(Double.parseDouble(listSplitS[3].split("=")[1])));
                                                price.set(new BestOfferQtty(Double.parseDouble(listSplitS[4].split("=")[1])));
                                            }
                                            
                                            mess.addGroup(price);
                                            //System.out.println(tokenPrices[i]);
                                        }
                                        
                                    }                                                                                             
                                    System.out.println("#Mess: " + mess.toString());
                                    Session.sendToTarget(mess, sessionID);
                                    System.out.println("==========");
                                   
                                }
                                

                            });

                }

            });
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        camelContext.start();

        

    }

    @Override
    public void onLogout(SessionID arg0) {
        // TODO Auto-generated method stub
        System.out.println("Logout");
        // AppServerHNX.sessionID = null;

    }

    @Override
    public void toAdmin(Message arg0, SessionID arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void toApp(Message arg0, SessionID arg1) throws DoNotSend {
        // TODO Auto-generated method stub
        //System.out.println("ToApp:" + arg0);

    }

    public static void main(String[] args) throws Exception {

        SessionSettings sessionSettings = new SessionSettings("./server.cfg");
        Application application = new AppServerHNX();
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(sessionSettings);
        LogFactory logFactory = new ScreenLogFactory(true, true, true);
        MessageFactory messageFactory = new MessageFactory();
        // FileLogFactory fileLogFactory = new FileLogFactory(settings);
        // SocketAcceptor acceptor = new SocketAccep
        SocketAcceptor acceptor = new SocketAcceptor(application, messageStoreFactory, sessionSettings, logFactory,
                messageFactory);
        acceptor.start();

        // CamelContext camelContext = new DefaultCamelContext();

        // try {
        //     camelContext.addRoutes(new RouteBuilder() {

        //         @Override
        //         public void configure() throws Exception {
        //             // TODO Auto-generated method stub
        //             System.out.println(123123);
        //             from("file:src/main/resources/?fileName=test1.dat&noop=true").split(body().tokenize("\n"))
        //                     .streaming().filter(body().contains("35=TP")).process(new Processor() {

        //                         @Override
        //                         public void process(Exchange exchange) throws Exception {
        //                             // TODO Auto-generated method stub
        //                             // String[] token = exchange.getIn().getBody(String.class).split("╔");
        //                             String line = exchange.getIn().getBody(String.class);
        //                             System.out.println(line);
        //                             //String[] tokens = line.split("");
        //                             String[] tokenPrices = line.split("556=");
        //                             String[] tokens = tokenPrices[0].split("");
                                   
        //                             TopNPrice mess = new TopNPrice();   
        //                             mess.set(new Symbol(tokens[5].split("=")[1]));
        //                             mess.set(new BoardCode(tokens[6].split("=")[1]));
        //                             int noTopPrice = Integer.parseInt(tokens[7].split("=")[1]);
        //                             mess.set(new NoTopPrice(noTopPrice));
                                    
        //                             // for(int i = 0; i<noTopPrice; i++) {
        //                             //     TopNPrice.NoTopPrice price = new TopNPrice.NoTopPrice();
        //                             //     price.set(new NumTopPrice(i+1));
        //                             //     if(tokens[9].split("=")[0].equals("133")) {
        //                             //         price.set(new BestOfferPrice(Double.parseDouble(tokens[9].split("=")[1])));
        //                             //         price.set(new BestOfferQtty(Double.parseDouble(tokens[10].split("=")[1])));
        //                             //     } else {
        //                             //         price.set(new BestBidPrice());
        //                             //         price.set(new BestBidQtty());
        //                             //         price.set(new BestOfferPrice(Double.parseDouble(tokens[9].split("=")[1])));
        //                             //         price.set(new BestOfferQtty(Double.parseDouble(tokens[10].split("=")[1])));
                                            
        //                             //     }
                                        
        //                             //     mess.addGroup(price);
        //                             // }

        //                             if (noTopPrice > 0) {
                                        
        //                                 //System.out.println("leng:" + tokenPrices.length);
        //                                 for (int i = 1 ; i<tokenPrices.length ; i++) {
        //                                     TopNPrice.NoTopPrice price = new TopNPrice.NoTopPrice();
        //                                     String[] listSplitS = tokenPrices[i].split("");
        //                                     //System.out.println("sodongtachduoc:"+listSplitS.length + "--phan tu cuoi:" +listSplitS[listSplitS.length-1]);
        //                                     price.set(new NumTopPrice(Integer.parseInt(listSplitS[0])));
        //                                     // if(listSplitS.length > 4) {
        //                                     //     System.out.println(listSplitS[1] + "--" + listSplitS[2] +"-"+ listSplitS[3]+"-" + listSplitS[4]);
        //                                     // } else {
        //                                     //     System.out.println(listSplitS[1] + "--" + listSplitS[2]);
        //                                     // }
        //                                     if(listSplitS.length <5) {
        //                                         if(listSplitS[1].contains("132=")) {
        //                                             price.set(new BestBidPrice(Double.parseDouble(listSplitS[1].split("=")[1])));
        //                                             price.set(new BestBidQtty(Double.parseDouble(listSplitS[2].split("=")[1])));
        //                                         } else {
        //                                             price.set(new BestOfferPrice(Double.parseDouble(listSplitS[1].split("=")[1])));
        //                                             price.set(new BestOfferQtty(Double.parseDouble(listSplitS[2].split("=")[1])));
        //                                         }
        //                                     } 
        //                                     else {
        //                                         price.set(new BestBidPrice(Double.parseDouble(listSplitS[1].split("=")[1])));
        //                                         price.set(new BestBidQtty(Double.parseDouble(listSplitS[2].split("=")[1])));
        //                                         price.set(new BestOfferPrice(Double.parseDouble(listSplitS[3].split("=")[1])));
        //                                         price.set(new BestOfferQtty(Double.parseDouble(listSplitS[4].split("=")[1])));
        //                                     }
                                            
        //                                     mess.addGroup(price);
        //                                     //System.out.println(tokenPrices[i]);
        //                                 }
                                        
        //                             }

                                    
                                   

                       

        //                             System.out.println("#Mess: " + mess.toString());

        //                             System.out.println("==========");
                                   
        //                         }

        //                     });

        //         }

        //     });
        // } catch (Exception e1) {
        //     // TODO Auto-generated catch block
        //     e1.printStackTrace();
        // }

        // camelContext.start();

    }

}
