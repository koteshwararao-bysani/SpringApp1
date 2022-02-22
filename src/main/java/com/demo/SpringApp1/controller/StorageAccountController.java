package com.demo.SpringApp1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/storageaccountdemo")
public class StorageAccountController {

    //@Value("azure-blob://containerpublic/candlestick-PATTERN.jpg")
    @Value("https://koteshstorageaccount.blob.core.windows.net/containerpublic/candlestick-PATTERN.jpg")
    private Resource blobFile;

//    @Value("${storage-account-key1}")
//    private String storageConnectionStr;

    @GetMapping("/readBlobFile")
    public String readBlobFile() throws IOException {
        System.out.println("i am from StorageAccountController :: readBlobFile()");
        return StreamUtils.copyToString(
                this.blobFile.getInputStream(),
                Charset.defaultCharset());
    }

    @GetMapping("/download/publicfile")
    public ResponseEntity<ByteArrayResource> readBlobFile1() throws IOException {
        System.out.println("i am from StorageAccountController :: readBlobFile1()");
        byte[] data = this.blobFile.getInputStream().readAllBytes();
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("content-type","application/octet-stream")
                .header("content-disposition","attachment; filename= file-image")
                .body(new ByteArrayResource(data));
    }

//    @GetMapping("/getconnectionstr")
//    public ResponseEntity<String> getStorageConnStrFromKeyVaults(){
//        return ResponseEntity.ok("To Connect Azure Storage Account :: Connection URL is --->  "+storageConnectionStr);
//    }
}
