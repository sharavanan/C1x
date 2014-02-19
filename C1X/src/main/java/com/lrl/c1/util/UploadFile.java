/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrl.c1.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.sun.jersey.core.header.FormDataContentDisposition;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

 
public class UploadFile { 
 
    
    
    
    public String uploadCreatives(InputStream uploadedInputStream,
             FormDataContentDisposition fileDetail) {

        try {
           
            String existingBucketName = "c1x";  
            String keyName = "banners/" + fileDetail.getFileName();  
            String amazonFileUploadLocationOriginal = existingBucketName;  
            
            AWSCredentials awsc = new AWSCredentials() { 
                 @Override public String getAWSAccessKeyId() { return "AKIAILCKQVF2DPF3KBCQ"; } 
                 @Override public String getAWSSecretKey() { return "tvWOg3YWbVKgiMpqFS72q8MDTeVeeR7Xtw2Cy+gU"; } };
            String ext = fileDetail.getFileName().split("\\.")[1].toString();
            
            System.out.println(" File size  "+fileDetail.getSize() );
            System.out.println(" File filename  "+fileDetail.getFileName());    
            System.out.println(" File getname  "+fileDetail.getName());
            System.out.println(" File gettype  "+fileDetail.getType()); 
    
   
            
            AmazonS3 s3Client = new AmazonS3Client(awsc);  
            s3Client.getBucketLocation("c1x");
            ObjectMetadata objectMetadata = new ObjectMetadata();
            System.out.println("stage 3"); 
            
            
            
            objectMetadata.setContentType("image/"+ext); 
            PutObjectRequest putObjectRequest = new PutObjectRequest(amazonFileUploadLocationOriginal, keyName, uploadedInputStream, objectMetadata);
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead); 
            PutObjectResult result = s3Client.putObject(putObjectRequest);
            
             GetObjectRequest request = new GetObjectRequest(existingBucketName,
    keyName);
             
              List<Bucket> bck =  s3Client.listBuckets();
              
            
            System.out.println("Etag:" + result.getETag() + "-->" + result);
            
            
            return result.getETag();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


    }
}