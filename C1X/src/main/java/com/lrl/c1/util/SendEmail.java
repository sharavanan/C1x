/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrl.c1.util;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SendEmail {
    //public static void main(String [] args)

    private Log log = LogFactory.getLog("SendEmail.java");

    public SendEmail() {
    }

    public String sendMail1() {
        // Recipient's email ID needs to be mentioned.
        String to = "pjayam.thee@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "pjayam.thee@gmail.com";

        // Assuming you are sending email from localhost
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties = System.getProperties();
        //properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        // properties.put("mail.smtp.starttls.enable", "true");


        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.user", "pjayam.thee@gmail.com");
        properties.setProperty("mail.password", "jaygmail23");

        properties.put("mail.smtp.starttls.enable", "true");
        // properties.put("mail.smtp.host", host);
        // properties.put("mail.smtp.user", from);
        // properties.put("mail.smtp.password", pass);
        properties.put("mail.smtp.port", "587");
        // properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", "true");




        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);


            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line! from local by jay");

            // Now set the actual message
            message.setText("This is actual message welcom to send mail from local mesage");

            // Send message
            Transport.send(message);

            Transport trans = session.getTransport("smtp");
            trans.connect("smtp.gmail.com", "example@gmail.com", "password");


            System.out.println("Sent .... message successfully....");
            Random n = new Random();

            return "sending success" + n.nextLong();
        } catch (Exception mex) {
            mex.printStackTrace();
        }
        return null;
    }

    public String sendMail(String userName, String passWord, String to) {

        log.info("IN SEND EMAIL");
        Properties myProperties = new Properties();
        // myProperties.put("mail.transport.protocol", "smtp"); // ###
        myProperties.put("mail.smtp.host", "com.sun.mail.smtp.gmail.com");
        myProperties.put("mail.smtp.port", "587");
        myProperties.put("mail.smtp.auth", "true");
        myProperties.put("mail.debug", "true");
        myProperties.put("mail.smtp.starttls.enable", "true");
        myProperties.put("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");// ###

        log.info("MYPORPERTIES DONE");

        //Session ses = Session.getDefaultInstance(myProperties,null); 
        Session ses = Session.getDefaultInstance(myProperties);

        Message msg = new MimeMessage(ses);// old
        //MimeMessage msg=new MimeMessage(ses);// ##
        try {

//            MimeMultipart alternative = new MimeMultipart("alternative");
//            MimeBodyPart text = new MimeBodyPart();
//            MimeBodyPart html = new MimeBodyPart();

            InternetAddress fromAddress = new InternetAddress("c1xproto@gmail.com", "c1xproto");
            InternetAddress toAddress = new InternetAddress(to);/* OSend to one receipient */
            // InternetAddress[] adrss = {new InternetAddress("dhineshkar0707@gmail.com"),new InternetAddress("subamsriram@gmail.com"), new InternetAddress("lrlsharan@gmail.com"),new InternetAddress("logichari@gmail.com")};
            msg.setFrom(fromAddress);
            // msg.setReplyTo(adrss);
            msg.setRecipient(Message.RecipientType.TO, toAddress);    /* OSend to more  receipient */
            //  msg.addRecipients(Message.RecipientType.CC, adrss);  /* OSend   BCC/CC  receipient */
            msg.setSubject("Welcome to C1"); //working

            //String body = "Test Mail from netbeans hai a new message "; /* Simple Message */
            //msg.setContent(body, "text/plain");
            // String htmlContent="<table width= '100%' cellpadding='3' cellspacing='3'  border= '0'  cellpadding= '0'  bgcolor= '#F4F4F4' > \n" +
//"    <tr bgcolor=\"#333334\"> \n" +
            String htmlContent = "<table width= '100%'  border= '0'  cellpadding= '0'  bgcolor= '#FFCC99' > \n"
                    + "    <tr bgcolor=\"#FF0000\"> \n"
                    + "      <td colspan=2 align= center > <h1>Welcome to C1</h1> </td> \n"
                    + "    </tr> \n"
                    + "	<tr> \n"
                    + "      <td colspan=2 align= center > <hr/> </td> \n"
                    + "    </tr> \n"
                    + "    <tr> \n"
                    + "      <td width= 30% ><b>Your User name is</b> </td> \n"
                    + "      <td width= 79% >" + userName + "</td> \n"
                    + "\n"
                    + "    </tr> \n"
                    + "    <tr> \n"
                    + "      <td width= 30% ><b>Your Password is</b> </td> \n"
                    + "      <td>" + passWord + "</td> \n"
                    + "\n"
                    + "    </tr>\n"
                    + "	<tr> \n"
                    + "      <td colspan=2 align= center > <hr/> </td> \n"
                    + "    </tr>  \n"
                    + "    <tr> \n"
                    + "      <td>&nbsp;</td> \n"
                    + "      <td>Click the  link to <a href= #  title= c1.com  target= _blank >login</a>: </td>      \n"
                    + "    </tr> \n"
                    + "  </table>";
            
            
            //html.setContent(htmlContent, "text/html");
            //alternative.addBodyPart(text);
            //msg.setContent(alternative);
           
//            html.setContent(htmlContent, "text/html"); /* HTML Message */
//            alternative.addBodyPart(html);
                msg.setContent(htmlContent,"text/html");
            
            // msg.setText(htmlContent, "utf-8", "html");
            //msg.setContent(htmlContent, "text/html; charset=utf-8");    
            //simpleMessage.setText(text, "utf-8", "html");
            //msg.setText("text/html");




            // Start Attachments

            // String filename = "C:\\printtest.txt";

            /*  if (System.getProperty("os.name").equals("Linux")) {//OS == LINUX
             file = new File(".//printtmp//" + filename);
             FW = new FileWriter(file.getCanonicalPath());
             } else {   //OS == WINDOWS
             file = new File(".\\printtmp\\" + filename);
             FW = new FileWriter(file.getCanonicalPath());
             }
             */
            // File f = new File(filename);
            // MimeBodyPart messageBodyPart = new MimeBodyPart();  
            //messageBodyPart.setContent("<h1><b><font color=red>This is TEST 2 message from local server  </font></b></h1>","text/html");  
            // Multipart multipart = new MimeMultipart();  
            //multipart.addBodyPart(messageBodyPart);  
            //messageBodyPart = new MimeBodyPart();  
            //DataSource source = new FileDataSource(f);  
            //messageBodyPart.setDataHandler(new DataHandler(source));  
            //messageBodyPart.setFileName(new File(filename).getName());  
            //multipart.addBodyPart(messageBodyPart);  
            //msg.setContent(multipart);  
            // END Attachments     			
            msg.setSentDate(new Date());
            javax.mail.Transport trans = ses.getTransport("smtp");
            String password = "c1x123456";

            trans.connect("smtp.gmail.com", "c1xproto@gmail.com", password);
            msg.saveChanges();
            //trans.send(msg); 
            trans.sendMessage(msg, msg.getAllRecipients());
            trans.close();
            Random n = new Random();
            System.out.println(" in send Mail");
            return "sending success" + n.nextLong();

        } catch (AddressException e) {
            e.printStackTrace();
            return "EXE 1";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "EXE 2";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "EXE 3";
        }
        //return "OK";
    }
}
