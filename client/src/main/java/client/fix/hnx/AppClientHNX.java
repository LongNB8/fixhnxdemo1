package client.fix.hnx;

import java.awt.Color;
import java.awt.Component;
import java.time.Year;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


import client.fix.hnx.Dto.NoTopPriceDto;
import client.fix.hnx.Dto.TopNPriceDto;
import edu.emory.mathcs.backport.java.util.concurrent.CountDownLatch;
import hnx.quickfix.fields.BestBidPrice;
import hnx.quickfix.messages.MessageCracker;
import hnx.quickfix.messages.MessageFactory;
import hnx.quickfix.messages.TopNPrice;
import hnx.quickfix.messages.TopNPrice.NoTopPrice;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Group;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.ScreenLogFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;

public class AppClientHNX extends MessageCracker implements Application {

    private static volatile SessionID sessionID;
    private static Queue<TopNPriceDto> qNPriceDtos = new ArrayDeque<>();
    public static Color color;
    
    static Map<String, Integer> map = new HashMap<String, Integer>();
    static int index = 0;
    static int rowChange = -999;
    static int price1BidChange = -999;
    static int price2BidChange = -999;
    static int price3BidChange = -999;
    static int kl1BidChange = -999;
    static int kl2BidChange = -999;
    static int kl3BidChange = -999;
    static int price1OfferChange = -999;
    static int price2OfferChange = -999;
    static int price3OfferChange = -999;
    static int kl1OfferChange = -999;
    static int kl2OfferChange = -999;
    static int kl3OfferChange = -999;
    static int nhayIndex = 0;

    static int rowPrev = -999;

     private static int str1;
    // private static String str2;
    // private static String str3;
    // private static String str4;
    // private static String str5;
    // private static String str6;
    // private static String str7;
    // private static String str8;
    // private static String str9;
    // private static String str10;
    // private static String str11;
    // private static String str12;

    // private static String prev1;
    // private static String prev2;
    // private static String prev3;
    // private static String prev4;
    // private static String prev5;
    // private static String prev6;
    // private static String prev7;
    // private static String prev8;
    // private static String prev9;
    // private static String prev10;
    // private static String prev11;
    // private static String prev12;




    static void setStr1(int s) {
        rowPrev = str1;
        str1 = s;
    }

    // static void setStr2(String s) {
    //     prev2 = str2;
    //     str2 = s;
    // }

    // static void setStr3(String s) {
    //     prev3 = str3;
    //     str3 = s;
    // }

    // static void setStr4(String s) {
    //     prev4 = str4;
    //     str4 = s;
    // }

    // static void setStr5(String s) {
    //     prev5 = str5;
    //     str5 = s;
    // }

    // static void setStr6(String s) {
    //     prev6 = str6;
    //     str6 = s;
    // }


    // static void setStr7(String s) {
    //     prev7 = str7;
    //     str7 = s;
    // }

    // static void setStr8(String s) {
    //     prev8 = str8;
    //     str8 = s;
    // }

    // static void setStr9(String s) {
    //     prev9 = str9;
    //     str9 = s;
    // }

    // static void setStr10(String s) {
    //     prev10 = str10;
    //     str10 = s;
    // }


    // static void setStr11(String s) {
    //     prev11 = str11;
    //     str11 = s;
    // }

    // static void setStr12(String s) {
    //     prev12 = str12;
    //     str12 = s;
    // }


    @Override
    public void fromAdmin(Message arg0, SessionID arg1)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        // TODO Auto-generated method stub

    }

    @Override
    public void fromApp(Message arg0, SessionID arg1)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        // TODO Auto-generated method stub
        //System.out.println("From app:" + arg0);
        crack(arg0, arg1);

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
        AppClientHNX.sessionID = arg0;

    }

    @Override
    public void onLogout(SessionID arg0) {
        // TODO Auto-generated method stub
        System.out.println("Logout");
        AppClientHNX.sessionID = null;

    }

    @Override
    public void toAdmin(Message arg0, SessionID arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void toApp(Message arg0, SessionID arg1) throws DoNotSend {
        // TODO Auto-generated method stub
       
    }

    @Override
    public void onMessage(TopNPrice message, SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        
        //System.out.println("###Message receive:" + message.toString());
        TopNPriceDto topNPriceDto = new TopNPriceDto();
        
        topNPriceDto.setSymbol(message.getSymbol().toString().split("=")[1]);
        topNPriceDto.setNoTopPrice(Integer.parseInt(message.getNoTopPrice().toString().split("=")[1]));
        List<NoTopPriceDto> noTopPriceDtos = new ArrayList<>();
        
        List<Group> nPrice =  message.getGroups(555);
        for (int i = 0; i< nPrice.size() ;i++) {
            
            NoTopPriceDto noTopPriceDto = new NoTopPriceDto();
            noTopPriceDto.setNumTopPrice(i+1);


            if(nPrice.get(i).isSetField(132) && !nPrice.get(i).isSetField(133)) {
                //System.out.println("So thu tu " + i + ": " + "BestBidPrice: " + nPrice.get(i).getString(132) + "-BestBidQtty: " + nPrice.get(i).getString(1321) );
                noTopPriceDto.setBestBidPrice((nPrice.get(i).getString(132)));
                noTopPriceDto.setBestBidQtty((nPrice.get(i).getString(1321)));
            } else if(nPrice.get(i).isSetField(133) && !nPrice.get(i).isSetField(132)){
                //System.out.println("So thu tu " + i + ": " + "-BestOfferPrice: " + nPrice.get(i).getString(133) + "-BestOfferQtty: " + nPrice.get(i).getString(1331));
                noTopPriceDto.setBestOfferPrice((nPrice.get(i).getString(133)));
                noTopPriceDto.setBestOfferQtty((nPrice.get(i).getString(1331)));
            }
             else if(nPrice.get(i).isSetField(132) && nPrice.get(i).isSetField(133)){
                //System.out.println("So thu tu " + i + ": " +  "BestBidPrice: " + nPrice.get(i).getString(132) + "-BestBidQtty: " + nPrice.get(i).getString(1321) + "-BestOfferPrice: " + nPrice.get(i).getString(133) + "-BestOfferQtty: " + nPrice.get(i).getString(1331));
                noTopPriceDto.setBestBidPrice((nPrice.get(i).getString(132)));
                noTopPriceDto.setBestBidQtty((nPrice.get(i).getString(1321)));
                noTopPriceDto.setBestOfferPrice((nPrice.get(i).getString(133)));
                noTopPriceDto.setBestOfferQtty((nPrice.get(i).getString(1331)));
            }
            noTopPriceDtos.add(noTopPriceDto);
        }
        topNPriceDto.setListNotopPrices(noTopPriceDtos);
        qNPriceDtos.add(topNPriceDto);
        
    }

    public static void main(String[] args) {
        SessionSettings settings;
        try {
            settings = new SessionSettings("./client.cfg");
            Application application = new AppClientHNX();
            MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);

            FileLogFactory logFactory= new FileLogFactory(settings);
            MessageFactory messageFactory = new MessageFactory();

            SocketInitiator initiator = new SocketInitiator(application, messageStoreFactory, settings, logFactory,
                    messageFactory);
            initiator.start();

            while (sessionID == null) {
                Thread.sleep(1000);
            }

            JFrame frame = new JFrame("HNX");
            // Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            // this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
            frame.pack();
            //frame.setLocationRelativeTo(null);
            
            frame.setSize(1500, 1500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Object[][] data = {};
    
            final DefaultTableModel model = new DefaultTableModel(data, new Object[]{"MaCK","Gia mua 1", "KL mua 1", "Gia mua 2", "KL mua 2", "Gia mua 3", "Kl mua 3", "Gia ban 1", "KL ban 1", "Gia ban 2", "KL ban 2", "Gia ban 3", "Kl ban 3" });
            final JTable table = new JTable(model);
        
        
        
        
            frame.getContentPane().add(new JScrollPane(table));

            final long startTime = System.currentTimeMillis();
            Thread thread = new Thread() {
            @Override
            public void run()
            {
               
                while (true)
                {
                    
                    //color = nhayIndex==1 ? Color.red : Color.green;
                    long now = System.currentTimeMillis();
                    long second = (now - startTime) / 1000;
                    color = second / 2 * 2 == second ? Color.red : Color.white;
                    //System.out.println("FlashCellTable.run");

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            TopNPriceDto test = qNPriceDtos.peek();
                            //if(map.get(test.getMaCp()))
                            List<NoTopPriceDto> list = test.getListNotopPrices();
                            if(map.get(test.getSymbol()) == null) {
                                map.put(test.getSymbol(), index);
                                index = index + 1;
                                nhayIndex = 0;
                                
                                Object[] obj = new Object[13];
                                
                                obj[0] = test.getSymbol();
                                if(list.size() == 0) {
                                    obj[1] = "";
                                    obj[2] = "";
                                    obj[3] = "";
                                    obj[4] = "";
                                    obj[5] = "";
                                    obj[6] = "";
                                    obj[7] = "";
                                    obj[8] = "";
                                    obj[9] = "";
                                    obj[10] = "";
                                    obj[11] = "";
                                    obj[12] = "";
                                } else if(list.size() == 1) {
                                    obj[1] = list.get(0).getBestBidPrice().equals("")  ? "" : list.get(0).getBestBidPrice();
                                    obj[2] = list.get(0).getBestBidQtty().equals("")  ? "" : list.get(0).getBestBidQtty();
                                    // obj[3] = "";
                                    // obj[4] = "";
                                    // obj[5] = "";
                                    // obj[6] = "";
                                    obj[7] = list.get(0).getBestOfferPrice().equals("")  ? "" : list.get(0).getBestOfferPrice();
                                    obj[8] = list.get(0).getBestOfferQtty().equals("")  ? "" : list.get(0).getBestOfferQtty();
                                    // obj[9] = "";
                                    // obj[10] = "";
                                    // obj[11] = "";
                                    // obj[12] = "";
                                } else if(list.size() == 2) {
                                    obj[1] = list.get(0).getBestBidPrice().equals("")  ? "" : list.get(0).getBestBidPrice();
                                    obj[2] = list.get(0).getBestBidQtty().equals("")  ? "" : list.get(0).getBestBidQtty();
                                    obj[3] = list.get(1).getBestBidPrice().equals("")  ? "" : list.get(1).getBestBidPrice();;
                                    obj[4] = list.get(1).getBestBidQtty().equals("") ? "" : list.get(1).getBestBidQtty();;
                                    // obj[5] = "";
                                    // obj[6] = "";
                                    obj[7] = list.get(0).getBestOfferPrice().equals("")  ? "" : list.get(0).getBestOfferPrice();
                                    obj[8] = list.get(0).getBestOfferQtty().equals("") ? "" : list.get(0).getBestOfferQtty();
                                    obj[9] = list.get(1).getBestOfferPrice().equals("")  ? "" : list.get(1).getBestOfferPrice();
                                    obj[10] = list.get(1).getBestOfferQtty().equals("") ? "" : list.get(1).getBestOfferQtty();
                                    // obj[11] = "";
                                    // obj[12] = "";
                                } else {

                                    obj[1] = list.get(0).getBestBidPrice().equals("")  ? "" : list.get(0).getBestBidPrice();
                                    obj[2] = list.get(0).getBestBidQtty().equals("")  ? "" : list.get(0).getBestBidQtty();
                                    obj[3] = list.get(1).getBestBidPrice().equals("")  ? "" : list.get(1).getBestBidPrice();;
                                    obj[4] = list.get(1).getBestBidQtty().equals("")  ? "" : list.get(1).getBestBidQtty();;
                                    obj[5] = list.get(2).getBestBidPrice().equals("")  ? "" : list.get(2).getBestBidPrice();;
                                    obj[6] = list.get(2).getBestBidQtty().equals("") ? "" : list.get(2).getBestBidQtty();;
                                    obj[7] = list.get(0).getBestOfferPrice().equals("")  ? "" : list.get(0).getBestOfferPrice();
                                    obj[8] = list.get(0).getBestOfferQtty().equals("")  ? "" : list.get(0).getBestOfferQtty();
                                    obj[9] = list.get(1).getBestOfferPrice().equals("")  ? "" : list.get(1).getBestOfferPrice();
                                    obj[10] = list.get(1).getBestOfferQtty().equals("")  ? "" : list.get(1).getBestOfferQtty();
                                    obj[11] = list.get(2).getBestOfferPrice().equals("")  ? "" : list.get(2).getBestOfferPrice();
                                    obj[12] = list.get(2).getBestOfferQtty().equals("")  ? "" : list.get(2).getBestOfferQtty();

                                } 
                                model.addRow(obj);
                                
                                
                            } 
                            else {
                                rowChange = (map.get(test.getSymbol()));
                                setStr1(rowChange);
                                nhayIndex = 1;
                                table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                               
                                if(list.size() == 1) {
                                    String gia1 =  (list.get(0).getBestBidPrice().equals("") ? "" : list.get(0).getBestBidPrice());
                                    String kl1 =  (list.get(0).getBestBidQtty().equals("") ? "" : list.get(0).getBestBidQtty());

                                    String giaBan1 =  (list.get(0).getBestOfferPrice().equals("") ? "" : list.get(0).getBestOfferPrice());
                                    String klBan1 =  (list.get(0).getBestOfferQtty().equals("") ? "" : list.get(0).getBestOfferQtty());

                                    if(!gia1.equals(table.getModel().getValueAt(rowChange, 1))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        
                                        model.setValueAt(gia1, rowChange, 1);
                                        price1BidChange = 1;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price1BidChange = -999;
                                        
                                        
                                    }
                                    
                                    if(!kl1.equals(table.getModel().getValueAt(rowChange, 2))) {
                                        model.setValueAt(kl1, rowChange, 2);
                                        kl1BidChange = 2;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl1BidChange = -999;
                                        
                                    }

                                    // setStr1(gia1);
                                    // setStr2(kl1);
                                    // setStr7(giaBan1);
                                    // setStr8(klBan1);


                                    if(!giaBan1.equals(table.getModel().getValueAt(rowChange, 7))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        model.setValueAt(giaBan1, rowChange, 7);
                                        price1OfferChange = 7;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price1OfferChange = -999;
                                        
                                    }
                                    if(!klBan1.equals(table.getModel().getValueAt(rowChange, 8))) {
                                        model.setValueAt(klBan1, rowChange, 8);
                                        kl1OfferChange = 8;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl1OfferChange = -999;
                                        
                                    }

                                    //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());

                                } 
                                else if(list.size() == 2) {
                                    String gia1 =  (list.get(0).getBestBidPrice().equals("") ? "" : list.get(0).getBestBidPrice());
                                    String kl1 =  (list.get(0).getBestBidQtty().equals("") ? "" : list.get(0).getBestBidQtty());
                                    String gia2 =  (list.get(1).getBestBidPrice().equals("") ? "" : list.get(1).getBestBidPrice());;
                                    String kl2 =  (list.get(1).getBestBidQtty().equals("") ? "" : list.get(1).getBestBidQtty());;
                                    String giaBan1 =  (list.get(0).getBestOfferPrice().equals("") ? "" : list.get(0).getBestOfferPrice());
                                    String klBan1 =  (list.get(0).getBestOfferQtty().equals("") ? "" : list.get(0).getBestOfferQtty());
                                    String giaBan2 =  (list.get(1).getBestOfferPrice().equals("") ? "" : list.get(1).getBestOfferPrice());
                                    String klBan2 =  (list.get(1).getBestOfferQtty().equals("") ? "" : list.get(1).getBestOfferQtty());
                                   
                                    // setStr1(gia1);
                                    // setStr2(kl1);
                                    // setStr7(giaBan1);
                                    // setStr8(klBan1);
                                    // setStr3(gia2);
                                    // setStr4(kl2);
                                    // setStr9(giaBan2);
                                    // setStr10(klBan2);

                                    if(!gia1.equals(table.getModel().getValueAt(rowChange, 1))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        
                                        model.setValueAt(gia1, rowChange, 1);
                                        price1BidChange = 1;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price1BidChange = -999;
                                        
                                    }
                                    if(!kl1.equals(table.getModel().getValueAt(rowChange, 2))) {
                                        model.setValueAt(kl1, rowChange, 2);
                                        kl1BidChange = 2;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl1BidChange = -999;
                                        
                                    }


                                    if(!giaBan1.equals(table.getModel().getValueAt(rowChange, 7))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        model.setValueAt(giaBan1, rowChange, 7);
                                        price1OfferChange = 7;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price1OfferChange = -999;
                                        
                                    }
                                    if(!klBan1.equals(table.getModel().getValueAt(rowChange, 8))) {
                                        model.setValueAt(klBan1, rowChange, 8);
                                        kl1OfferChange = 8;
                                       // table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl1OfferChange = -999;
                                        
                                    }

                                    if(!gia2.equals(table.getModel().getValueAt(rowChange, 3))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        model.setValueAt(gia2, rowChange, 3);
                                        price2BidChange = 3;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price2BidChange = -999;
                                        
                                    }
                                    if(!kl2.equals(table.getModel().getValueAt(rowChange, 4))) {
                                        model.setValueAt(kl2, rowChange, 4);
                                        kl2BidChange = 4;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl2BidChange = -999;
                                        
                                    }

                                    if(!giaBan2.equals(table.getModel().getValueAt(rowChange, 9))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        model.setValueAt(giaBan2, rowChange, 9);
                                        price2OfferChange = 9;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        table.getColumnModel().getColumn(9).setCellRenderer(new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price2OfferChange = -999;
                                        
                                    }
                                    if(!klBan2.equals(table.getModel().getValueAt(rowChange, 10))) {
                                        model.setValueAt(klBan2, rowChange, 10);
                                        kl2OfferChange= 10;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl2OfferChange= -999;
                                        
                                    }
                                    //
                                    //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());

                                }
                                //System.out.println("cmm ngu" + rowChange);
                                else if(list.size() == 0) {
                                    System.out.println("tes4t1111");
                                }
                                else {
                                    String gia1 =  (list.get(0).getBestBidPrice().equals("") ? "" : list.get(0).getBestBidPrice());
                                    String kl1 =  (list.get(0).getBestBidQtty().equals("") ? "" : list.get(0).getBestBidQtty());
                                    String gia2 =  (list.get(1).getBestBidPrice().equals("") ? "" : list.get(1).getBestBidPrice());;
                                    String kl2 =  (list.get(1).getBestBidQtty().equals("") ? "" : list.get(1).getBestBidQtty());;
                                    String gia3 =  (list.get(2).getBestBidPrice().equals("") ? "" : list.get(2).getBestBidPrice());;
                                    String kl3 =  (list.get(2).getBestBidQtty().equals("") ? "" : list.get(2).getBestBidQtty());;
                                    String giaBan1 =  (list.get(0).getBestOfferPrice().equals("") ? "" : list.get(0).getBestOfferPrice());
                                    String klBan1 =  (list.get(0).getBestOfferQtty().equals("") ? "" : list.get(0).getBestOfferQtty());
                                    String giaBan2 =  (list.get(1).getBestOfferPrice().equals("") ? "" : list.get(1).getBestOfferPrice());
                                    String klBan2 =  (list.get(1).getBestOfferQtty().equals("") ? "" : list.get(1).getBestOfferQtty());
                                    String giaBan3 =  (list.get(2).getBestOfferPrice().equals("") ? "" : list.get(2).getBestOfferPrice());
                                    String klBan3 =  (list.get(2).getBestOfferQtty().equals("") ? "" : list.get(2).getBestOfferQtty());
                                    

                                    // setStr1(gia1);
                                    // setStr2(kl1);
                                    // setStr7(giaBan1);
                                    // setStr8(klBan1);
                                    // setStr3(gia2);
                                    // setStr4(kl2);
                                    // setStr9(giaBan2);
                                    // setStr10(klBan2);
                                    // setStr5(gia3);
                                    // setStr6(kl3);
                                    // setStr11(giaBan3);
                                    // setStr12(klBan3);

                                    if(!gia1.equals(table.getModel().getValueAt(rowChange, 1))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        
                                        model.setValueAt(gia1, rowChange, 1);
                                        price1BidChange = 1;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price1BidChange = -999;
                                        
                                    }
                                    if(!kl1.equals(table.getModel().getValueAt(rowChange, 2))) {
                                        model.setValueAt(kl1, rowChange, 2);
                                        kl1BidChange = 2;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl1BidChange = -999;
                                        
                                    }


                                    if(!giaBan1.equals(table.getModel().getValueAt(rowChange, 7))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        model.setValueAt(giaBan1, rowChange, 7);
                                        price1OfferChange = 7;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price1OfferChange = -999;
                                        
                                    }
                                    if(!klBan1.equals(table.getModel().getValueAt(rowChange, 8))) {
                                        model.setValueAt(klBan1, rowChange, 8);
                                        kl1OfferChange = 8;
                                         //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl1OfferChange = -999;
                                        
                                    }

                                    if(!gia2.equals(table.getModel().getValueAt(rowChange, 3))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        model.setValueAt(gia2, rowChange, 3);
                                        price2BidChange = 3;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price2BidChange = -999;
                                        
                                    }
                                    if(!kl2.equals(table.getModel().getValueAt(rowChange, 4))) {
                                        model.setValueAt(kl2, rowChange, 4);
                                        kl2BidChange = 4;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        
                                        kl2BidChange = -999;
                                    }

                                    if(!giaBan2.equals(table.getModel().getValueAt(rowChange, 9))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        model.setValueAt(giaBan2, rowChange, 9);
                                        price2OfferChange= 9;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        //MyFlashingCellRenderer flashingCellRenderer = new MyFlashingCellRenderer();
                                        //flashingCellRenderer.set
                                        
                                    } else {
                                        price2OfferChange= -999;
                                        
                                    }
                                    if(!klBan2.equals(table.getModel().getValueAt(rowChange, 10))) {
                                        model.setValueAt(klBan2, rowChange, 10);
                                        kl2OfferChange = 10;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl2OfferChange = -999;
                                        
                                    }

                                    if(!gia3.equals(table.getModel().getValueAt(rowChange, 5))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        model.setValueAt(gia3, rowChange, 5);
                                        price3BidChange = 5;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price3BidChange = -999;
                                        
                                    }
                                    if(!kl3.equals(table.getModel().getValueAt(rowChange, 6))) {
                                        model.setValueAt(kl3, rowChange, 6);
                                        kl3BidChange = 6;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl3BidChange = -999;
                                        
                                    }
    
                                    
                                    //ben ban
    
                                    
                                    if(!giaBan3.equals(table.getModel().getValueAt(rowChange, 11))) {
                                        //System.out.println("ngu cmm" + table.getModel().getValueAt(rowChange, 2));
                                        model.setValueAt(giaBan3, rowChange, 11);
                                        price3OfferChange = 11;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        price3OfferChange = -999;
                                        
                                    }
                                    if(!klBan3.equals(table.getModel().getValueAt(rowChange, 12))) {
                                        model.setValueAt(klBan3, rowChange, 12);
                                        kl3OfferChange = 12;
                                        //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                        
                                    } else {
                                        kl3OfferChange = -999;
                                        
                                    }
                                    //nhayIndex = 1;
                                    //table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());

                                }
                                
                                // nhayIndex = 1;
                                 table.setDefaultRenderer(Object.class, (TableCellRenderer) new MyFlashingCellRenderer());
                                // TimerTask timerTask = new TimerTask() {
                                //     @Override
                                //     public void run() {
                                //         color = Color.red;
                                //     }
                                // };
                               
                                // Timer timer = new Timer("Timer");
                                // timer.schedule(timerTask, 100);;

                                // TimerTask timerTask1 = new TimerTask() {
                                //     @Override
                                //     public void run() {
                                //         color = Color.white;
                                //     }
                                // };
                               
                                // Timer timer1 = new Timer("Timer1");
                                // timer1.schedule(timerTask1, 50);;

                                
                                 
                                
                            }
                            
                            //System.out.println(map.get(test.getMaCp()));
                            //System.out.println("index:" + index);
                            
                            qNPriceDtos.poll();
                            //table.tableChanged(new TableModelEvent(table.getModel(), 2, 2, 2));  
                        }
                    });
                    try
                    {
                        Thread.sleep(500);
                        //table.tableChanged(new TableModelEvent(table.getModel(), 2, 2, 2));

                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        };

        thread.start();

        frame.setVisible(true);

        } catch (ConfigError | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
    }


    public static class MyFlashingCellRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column)
        {
            // System.out.println(row);
            // System.out.println(column);
            
            Component label = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
           

            
                System.out.println("row change: " + rowChange);
                System.out.println("col change: " +  column);
                System.out.println("value: " + value);
                System.out.println(price1BidChange);
                System.out.println(kl1BidChange);
                System.out.println(price2BidChange);
                System.out.println(kl2BidChange);
                System.out.println(price3BidChange);
                System.out.println(kl3BidChange);
                System.out.println(price1OfferChange);
                System.out.println(kl1OfferChange);
                System.out.println(price2OfferChange);
                System.out.println(kl2OfferChange);
                System.out.println(price3OfferChange);
                System.out.println(kl3OfferChange);
                if(row == rowChange) {
                    label.setBackground(color);
                    if(value.toString().equals("")){
                        label.setBackground(null);
                    }
                } else {
                    label.setBackground(null);
                }
                
                // if(row != rowPrev) {
                //     label = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowPrev, column);
                //     label.setBackground(Color.white);
                // }
                
                // if(row == rowChange) {
                    
                //     // String cot1 = table.getModel().getValueAt(row, 1).toString().equals("") ? "0" : table.getModel().getValueAt(row, 1).toString();
                //     // String cot2 = table.getModel().getValueAt(row, 2).toString().equals("") ? "0" : table.getModel().getValueAt(row, 2).toString();
                //     // String cot3 = table.getModel().getValueAt(row, 3).toString().equals("") ? "0" : table.getModel().getValueAt(row, 3).toString();
                //     // String cot4 = table.getModel().getValueAt(row, 4).toString().equals("") ? "0" : table.getModel().getValueAt(row, 4).toString();
                //     // String cot5 = table.getModel().getValueAt(row, 5).toString().equals("") ? "0" : table.getModel().getValueAt(row, 5).toString();
                //     // String cot6 = table.getModel().getValueAt(row, 6).toString().equals("") ? "0" : table.getModel().getValueAt(row, 6).toString();
                //     // String cot7 = table.getModel().getValueAt(row, 7).toString().equals("") ? "0" : table.getModel().getValueAt(row, 7).toString();
                //     // String cot8 = table.getModel().getValueAt(row, 8).toString().equals("") ? "0" : table.getModel().getValueAt(row, 8).toString();
                //     // String cot9 = table.getModel().getValueAt(row, 9).toString().equals("") ? "0" : table.getModel().getValueAt(row, 9).toString();
                //     // String cot10 = table.getModel().getValueAt(row, 10).toString().equals("") ? "0" : table.getModel().getValueAt(row, 10).toString();
                //     // String cot11 = table.getModel().getValueAt(row, 11).toString().equals("") ? "0" : table.getModel().getValueAt(row, 11).toString();
                //     // String cot12 = table.getModel().getValueAt(row, 12).toString().equals("") ? "0" : table.getModel().getValueAt(row, 12).toString();
                    
                //     //System.out.println("kl 1: " + kl1Change);
                   
                //     label.setBackground(color);
                //     if(value.toString().equals("") ){
                //         label.setBackground(null);
                //     }
                    
                // } else {
                //     label.setBackground(null);
                   
                // }
              

                //label.setBackground(color);

                // if(column == price1BidChange) {
                //     label.setBackground(color);
                // } 

                // else if(column == price2BidChange) {
                //     label.setBackground(color);
                // }

                // else if(column == price3BidChange) {
                //     label.setBackground(color);
                // }

                // else if(column == price1OfferChange) {
                //     label.setBackground(color);
                // }

                // else if(column == price2OfferChange) {
                //     label.setBackground(color);
                // }

                // else if(column == price3OfferChange) {
                //     label.setBackground(color);
                // }

                // else if(column == kl1BidChange) {
                //     label.setBackground(color);
                // }

                // else if(column == kl2BidChange) {
                //     label.setBackground(color);
                // }

                // else if(column == kl3BidChange) {
                //     label.setBackground(color);
                // }

                // else if(column == kl1OfferChange) {
                //     label.setBackground(color);
                // }

                // else if(column == kl2OfferChange) {
                //     label.setBackground(color);
                // }

                // else if(column == kl3OfferChange) {
                //     label.setBackground(color);
                // }
                // else if(value.toString().equals("")) {
                //     label.setBackground(null);
                // } else {
                //     label.setBackground(null);
                // }
            return label;
        }

        
        
    }

}
